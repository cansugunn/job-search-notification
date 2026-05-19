package com.jobsearch.notification.data.mapper;

import com.jobsearch.notification.data.document.JobAlert;
import com.jobsearch.notification.data.dto.alert.response.AlertResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AlertMapper {

  AlertResponseDto toAlertResponseDto(JobAlert alert);
}
