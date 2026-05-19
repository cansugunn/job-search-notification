package com.jobsearch.notification.data.repository;

import com.jobsearch.notification.data.document.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationRepository extends MongoRepository<Notification, String> {

  Page<Notification> findByUserIdOrderByCreatedAtDesc(String userId, Pageable pageable);

  boolean existsByUserIdAndJobId(String userId, String jobId);
}
