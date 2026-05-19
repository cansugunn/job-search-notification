package com.jobsearch.notification.service;

public interface SystemTokenService {

  String getToken();

  void evict();
}
