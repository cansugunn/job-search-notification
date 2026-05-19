package com.jobsearch.notification.service.impl;

import com.jobsearch.notification.common.exception.BusinessException;
import com.jobsearch.notification.data.document.JobAlert;
import com.jobsearch.notification.data.dto.alert.request.CreateAlertRequestDto;
import com.jobsearch.notification.data.dto.alert.response.AlertResponseDto;
import com.jobsearch.notification.data.mapper.AlertMapper;
import com.jobsearch.notification.data.repository.JobAlertRepository;
import com.jobsearch.notification.service.AlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlertServiceImpl implements AlertService {

  private final JobAlertRepository jobAlertRepository;

  private final AlertMapper alertMapper;

  @Override
  public AlertResponseDto create(String userId, CreateAlertRequestDto dto) {
    JobAlert alert = JobAlert.builder()
                             .userId(userId)
                             .position(dto.position())
                             .town(dto.town())
                             .city(dto.city())
                             .country(dto.country())
                             .workingPreference(dto.workingPreference())
                             .active(true)
                             .build();
    return alertMapper.toAlertResponseDto(jobAlertRepository.save(alert));
  }

  @Override
  public Page<AlertResponseDto> getAlerts(String userId, Pageable pageable) {
    return jobAlertRepository.findByUserIdAndActiveTrue(userId, pageable)
                             .map(alertMapper::toAlertResponseDto);
  }

  @Override
  public void delete(String userId, String alertId) {
    JobAlert alert = jobAlertRepository.findById(alertId)
                                       .orElseThrow(() -> new BusinessException("Alert not found"));
    if (!alert.getUserId().equals(userId)) {
      throw new BusinessException("You can only delete your own alerts");
    }
    jobAlertRepository.delete(alert);
  }
}
