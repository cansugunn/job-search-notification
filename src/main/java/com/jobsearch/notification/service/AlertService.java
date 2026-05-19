package com.jobsearch.notification.service;

import com.jobsearch.notification.data.dto.alert.request.CreateAlertRequestDto;
import com.jobsearch.notification.data.dto.alert.response.AlertResponseDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AlertService {

  AlertResponseDto create(@NotEmpty String userId,
                          @NotNull @Valid CreateAlertRequestDto dto);

  Page<AlertResponseDto> getAlerts(@NotEmpty String userId,
                                   @NotNull Pageable pageable);

  void delete(@NotEmpty String userId,
              @NotEmpty String alertId);
}
