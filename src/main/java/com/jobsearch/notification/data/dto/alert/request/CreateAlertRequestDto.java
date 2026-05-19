package com.jobsearch.notification.data.dto.alert.request;

public record CreateAlertRequestDto(String position,
                                    String town,
                                    String city,
                                    String country,
                                    String workingPreference) {

}
