package com.jobsearch.notification.data.dto.alert.response;

import java.time.LocalDateTime;

public record AlertResponseDto(String id,
                               String position,
                               String town,
                               String city,
                               String country,
                               String workingPreference,
                               Boolean active,
                               LocalDateTime createdAt) {

}
