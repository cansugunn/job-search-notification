package com.jobsearch.notification.controller;

import com.jobsearch.notification.data.dto.alert.request.CreateAlertRequestDto;
import com.jobsearch.notification.data.dto.alert.response.AlertResponseDto;
import com.jobsearch.notification.service.AlertService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/alerts")
@RequiredArgsConstructor
@Tag(name = "Job Alerts",
     description = "Create and manage job posting alerts")
@SecurityRequirement(name = "bearerAuth")
public class AlertController {

  private final AlertService alertService;

  @Operation(summary = "Create a new job alert")
  @PostMapping
  public ResponseEntity<AlertResponseDto> create(@RequestBody CreateAlertRequestDto dto,
                                                 @AuthenticationPrincipal Jwt jwt) {
    return ResponseEntity.status(HttpStatus.CREATED)
                         .body(alertService.create(jwt.getSubject(), dto));
  }

  @Operation(summary = "Get my job alerts")
  @GetMapping
  public ResponseEntity<Page<AlertResponseDto>> getMyAlerts(
      @AuthenticationPrincipal Jwt jwt,
      @ParameterObject @PageableDefault(size = 10) Pageable pageable) {
    return ResponseEntity.ok(alertService.getAlerts(jwt.getSubject(), pageable));
  }

  @Operation(summary = "Delete (deactivate) a job alert")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable String id,
                                     @AuthenticationPrincipal Jwt jwt) {
    alertService.delete(jwt.getSubject(), id);
    return ResponseEntity.noContent().build();
  }
}
