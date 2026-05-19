package com.jobsearch.notification.data.mapper;

import com.jobsearch.notification.data.document.Notification;
import com.jobsearch.notification.data.dto.notification.response.NotificationResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

  NotificationResponseDto toNotificationResponseDto(Notification notification);
}
