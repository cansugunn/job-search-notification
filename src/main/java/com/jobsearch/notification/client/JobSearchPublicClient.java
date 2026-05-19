package com.jobsearch.notification.client;

import com.jobsearch.notification.data.dto.client.response.JobPostingSummaryDto;
import com.jobsearch.notification.data.dto.client.response.PageResponse;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange
public interface JobSearchPublicClient {

  @GetExchange("/api/v1/jobs")
  PageResponse<JobPostingSummaryDto> searchJobs(
      @RequestParam(required = false) String position,
      @RequestParam(required = false) String countryName,
      @RequestParam(required = false) String cityName,
      @RequestParam(required = false) String townName,
      @RequestParam int page,
      @RequestParam int size);
}
