package com.jobsearch.notification.service.impl;

import com.jobsearch.notification.client.JobSearchAdminClient;
import com.jobsearch.notification.client.JobSearchPublicClient;
import com.jobsearch.notification.data.document.JobAlert;
import com.jobsearch.notification.data.document.Notification;
import com.jobsearch.notification.data.dto.client.response.JobPostingSummaryDto;
import com.jobsearch.notification.data.dto.client.response.JobSearchHistoryDto;
import com.jobsearch.notification.data.dto.client.response.PageResponse;
import com.jobsearch.notification.data.enums.NotificationType;
import com.jobsearch.notification.data.event.NewJobPostingEvent;
import com.jobsearch.notification.data.repository.JobAlertRepository;
import com.jobsearch.notification.data.repository.NotificationRepository;
import com.jobsearch.notification.messaging.JobPostingConsumer;
import com.jobsearch.notification.service.ExternalSchedulerService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.util.Objects.isNull;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExternalSchedulerServiceImpl implements ExternalSchedulerService {

  private static final int SEARCH_HISTORY_DAYS = 7;

  private final JobPostingConsumer jobPostingConsumer;

  private final JobAlertRepository jobAlertRepository;

  private final NotificationRepository notificationRepository;

  private final JobSearchAdminClient jobSearchAdminClient;

  private final JobSearchPublicClient jobSearchPublicClient;

  @Transactional
  @Async
  @Override
  public void processJobAlerts() {
    log.info("Processing job alerts at {}", LocalDateTime.now());
    List<NewJobPostingEvent> newJobPostingEventList = jobPostingConsumer.drainQueue();
    if (newJobPostingEventList.isEmpty()) {
      log.info("No pending job posting events in queue");
      return;
    }

    log.info("Processing {} new job posting event(s)", newJobPostingEventList.size());

    List<JobAlert> jobAlertList = jobAlertRepository.findByActiveTrue();
    for (NewJobPostingEvent newJobPostingEvent : newJobPostingEventList) {
      for (JobAlert jobAlert : jobAlertList) {
        if (newJobPostingEvent.matches(jobAlert)) {
          if (notificationRepository.existsByUserIdAndJobId(jobAlert.getUserId(), newJobPostingEvent.jobId())) {
            continue;
          }

          String message = String.format("New job matching your alert: \"%s\" in %s (%s)",
                                         newJobPostingEvent.title(),
                                         newJobPostingEvent.location(),
                                         newJobPostingEvent.workingPreference());
          Notification notification = Notification.builder()
                                                  .userId(jobAlert.getUserId())
                                                  .jobId(newJobPostingEvent.jobId())
                                                  .jobTitle(newJobPostingEvent.title())
                                                  .message(message)
                                                  .type(NotificationType.JOB_ALERT)
                                                  .sent(true)
                                                  .sentAt(LocalDateTime.now())
                                                  .build();
          notificationRepository.save(notification);
          log.info("Notification created for userId={}, jobId={}", jobAlert.getUserId(), newJobPostingEvent.jobId());
        }
      }
    }

    log.info("Job alert processing completed");
  }

  @Transactional
  @Async
  @Override
  public void processRelatedJobs() {
    log.info("Processing related jobs at {}", LocalDateTime.now());
    try {
      List<JobSearchHistoryDto> jobSearchHistoryDtoList = fetchAllSearchPages();

      Map<String, List<JobSearchHistoryDto>> jobSearchHistoryDtoMapByUserId = new HashMap<>();
      for (JobSearchHistoryDto jobSearchHistoryDto : jobSearchHistoryDtoList) {
        jobSearchHistoryDtoMapByUserId.computeIfAbsent(jobSearchHistoryDto.userId(), key -> new ArrayList<>())
                                      .add(jobSearchHistoryDto);
      }

      for (Map.Entry<String, List<JobSearchHistoryDto>> entry : jobSearchHistoryDtoMapByUserId.entrySet()) {
        String userId = entry.getKey();
        for (JobSearchHistoryDto jobSearchHistoryDto : entry.getValue()) {
          if (isNull(jobSearchHistoryDto.position())
              && isNull(jobSearchHistoryDto.city())
              && isNull(jobSearchHistoryDto.town())) {
            continue;
          }

          try {
            List<JobPostingSummaryDto> jobPostingSummaryDtoList =
                fetchAllPages(jobSearchHistoryDto.position(), jobSearchHistoryDto.city(), jobSearchHistoryDto.town());
            for (JobPostingSummaryDto job : jobPostingSummaryDtoList) {
              if (notificationRepository.existsByUserIdAndJobId(userId, job.id())) {
                continue;
              }

              String message = String.format("Based on your search for \"%s\" in %s, check out: \"%s\"",
                                             jobSearchHistoryDto.position(),
                                             jobSearchHistoryDto.location(),
                                             job.title());
              Notification notification = Notification.builder()
                                                      .userId(userId)
                                                      .jobId(job.id())
                                                      .jobTitle(job.title())
                                                      .message(message)
                                                      .type(NotificationType.RELATED_JOB)
                                                      .sent(true)
                                                      .sentAt(LocalDateTime.now())
                                                      .build();
              notificationRepository.save(notification);
            }
          } catch (Exception e) {
            log.error("Error processing related jobs for userId={}: {}", userId, e.getMessage());
          }
        }
      }
    } catch (Exception e) {
      log.error("Related job processing failed: {}", e.getMessage());
    }

    log.info("Related job processing completed");
  }

  private List<JobSearchHistoryDto> fetchAllSearchPages() {
    List<JobSearchHistoryDto> all = new ArrayList<>();
    int page = 0;
    int size = 250;
    PageResponse<JobSearchHistoryDto> response;
    do {
      response = jobSearchAdminClient.getAllJobSearches(SEARCH_HISTORY_DAYS, page++, size);
      all.addAll(response.content());
    } while (!response.last());
    return all;
  }

  private List<JobPostingSummaryDto> fetchAllPages(String position, String city, String townId) {
    List<JobPostingSummaryDto> all = new ArrayList<>();
    int page = 0;
    int size = 250;
    PageResponse<JobPostingSummaryDto> response;
    do {
      response = jobSearchPublicClient.searchJobs(position, city, townId, page++, size);
      all.addAll(response.content());
    } while (!response.last());
    return all;
  }
}
