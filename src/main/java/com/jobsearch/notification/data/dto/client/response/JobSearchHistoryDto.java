package com.jobsearch.notification.data.dto.client.response;

import java.time.LocalDateTime;
import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.springframework.util.StringUtils.hasText;

public record JobSearchHistoryDto(String userId,
                                  String position,
                                  String town,
                                  String city,
                                  String country,
                                  String workingPreference,
                                  LocalDateTime searchedAt) {

    public Optional<String> location() {
        if (hasText(town)) {
            return of(town);
        }
        if (hasText(city)) {
            return of(city);
        }
        if (hasText(country)) {
            return of(country);
        }
        return empty();
    }
}
