package com.jobsearch.notification.service;

public interface ExternalSchedulerService {

  void processJobAlerts();

  void processRelatedJobs();
}
