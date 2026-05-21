package com.jobsearch.notification.data.repository;

import com.jobsearch.notification.data.dto.client.response.JobSearchHistoryDto;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.stereotype.Repository;

import static com.mongodb.client.model.Filters.gte;
import static com.mongodb.client.model.Sorts.descending;

@Repository
@RequiredArgsConstructor
public class JobSearchHistoryMongoRepository {

  private static final String DATABASE_NAME = "job-search";
  private static final String COLLECTION_NAME = "job_searches";

  private final MongoDatabaseFactory mongoDatabaseFactory;

  public List<JobSearchHistoryDto> findSearchesSince(LocalDateTime since) {
    Date searchedAtAfter = Date.from(since.atZone(ZoneId.systemDefault()).toInstant());
    List<JobSearchHistoryDto> searches = new ArrayList<>();

    mongoDatabaseFactory.getMongoDatabase(DATABASE_NAME)
                        .getCollection(COLLECTION_NAME)
                        .find(gte("searchedAt", searchedAtAfter))
                        .sort(descending("searchedAt"))
                        .forEach(document -> searches.add(toDto(document)));

    return searches;
  }

  private JobSearchHistoryDto toDto(Document document) {
    return new JobSearchHistoryDto(document.getString("userId"),
                                   document.getString("position"),
                                   document.getString("town"),
                                   document.getString("city"),
                                   document.getString("country"),
                                   document.getString("workingPreference"),
                                   toLocalDateTime(document.get("searchedAt")));
  }

  private LocalDateTime toLocalDateTime(Object value) {
    if (value instanceof Date date) {
      return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }
    if (value instanceof LocalDateTime localDateTime) {
      return localDateTime;
    }
    return null;
  }
}
