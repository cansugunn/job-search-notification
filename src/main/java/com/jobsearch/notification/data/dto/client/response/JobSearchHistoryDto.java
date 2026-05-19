package com.jobsearch.notification.data.dto.client.response;

import java.time.LocalDateTime;

import static org.springframework.util.StringUtils.hasText;

public record JobSearchHistoryDto(String userId,
                                  String position,
                                  String town,
                                  String city,
                                  String country,
                                  String workingPreference,
                                  LocalDateTime searchedAt) {

  public String location() {
    if (hasText(town)) {
      return town;
    }
    return city;
  }
}
