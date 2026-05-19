package com.jobsearch.notification.client;

import com.jobsearch.notification.data.dto.client.response.JobSearchHistoryDto;
import com.jobsearch.notification.data.dto.client.response.PageResponse;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange
public interface JobSearchAdminClient {

  @GetExchange("/api/v1/searches/all")
  PageResponse<JobSearchHistoryDto> getAllJobSearches(
      @RequestParam(defaultValue = "7") int days,
      @RequestParam int page,
      @RequestParam int size);
}
