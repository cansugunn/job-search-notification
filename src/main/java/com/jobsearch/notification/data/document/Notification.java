package com.jobsearch.notification.data.document;

import com.jobsearch.notification.data.enums.NotificationType;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "notifications")
@Getter
@Setter
@Builder
public class Notification {

  @Id
  private String id;

  @Indexed
  private String userId;

  private String jobId;

  private String jobTitle;

  private String message;

  private NotificationType type;

  @Builder.Default
  private Boolean sent = false;

  private LocalDateTime sentAt;

  @CreatedDate
  private LocalDateTime createdAt;
}
