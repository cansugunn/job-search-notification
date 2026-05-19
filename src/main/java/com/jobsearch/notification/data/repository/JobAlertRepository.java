package com.jobsearch.notification.data.repository;

import com.jobsearch.notification.data.document.JobAlert;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface JobAlertRepository extends MongoRepository<JobAlert, String> {

  Page<JobAlert> findByUserIdAndActiveTrue(String userId, Pageable pageable);

  List<JobAlert> findByActiveTrue();
}
