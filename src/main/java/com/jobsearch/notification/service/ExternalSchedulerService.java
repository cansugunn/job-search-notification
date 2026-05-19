package com.jobsearch.notification.service;

import jakarta.validation.constraints.NotEmpty;

public interface ExternalSchedulerService {

  void processJobAlerts(@NotEmpty String schedulerSecret);

  void processRelatedJobs(@NotEmpty String schedulerSecret);
}
