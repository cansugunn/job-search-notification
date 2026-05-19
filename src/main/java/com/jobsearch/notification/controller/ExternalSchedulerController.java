package com.jobsearch.notification.controller;

import com.jobsearch.notification.service.ExternalSchedulerService;
import com.jobsearch.notification.validator.ExternalSchedulerValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.jobsearch.notification.data.constants.ExternalSchedulerConstants.SCHEDULER_HEADER;

@Slf4j
@RestController
@RequestMapping("/api/v1/external-scheduler")
@RequiredArgsConstructor
@Tag(name = "Scheduler",
     description = "Endpoints triggered by external scheduler")
public class ExternalSchedulerController {

  private final ExternalSchedulerValidator externalSchedulerValidator;

  private final ExternalSchedulerService externalSchedulerService;

  @Operation(summary = "Process job alert notifications")
  @PostMapping("/job-alerts")
  public ResponseEntity<Void>
  triggerJobAlerts(@RequestHeader(value = SCHEDULER_HEADER) String secret) {
    externalSchedulerValidator.validateSecret(secret);
    externalSchedulerService.processJobAlerts();
    return ResponseEntity.ok().build();
  }

  @Operation(summary = "Process related-job notifications")
  @PostMapping("/related-jobs")
  public ResponseEntity<Void>
  triggerRelatedJobs(@RequestHeader(value = SCHEDULER_HEADER) String secret) {
    externalSchedulerValidator.validateSecret(secret);
    externalSchedulerService.processRelatedJobs();
    return ResponseEntity.ok().build();
  }
}
