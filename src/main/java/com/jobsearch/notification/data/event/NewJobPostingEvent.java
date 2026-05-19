package com.jobsearch.notification.data.event;

import com.jobsearch.notification.data.document.JobAlert;

import static java.util.Objects.isNull;
import static org.springframework.util.StringUtils.hasText;

public record NewJobPostingEvent(String jobId,
                                 String title,
                                 String town,
                                 String city,
                                 String country,
                                 String workingPreference) {

  public boolean matches(JobAlert jobAlert) {
    if (isNull(jobAlert)) {
      return false;
    }

    boolean positionMatch = !hasText(jobAlert.getPosition()) || containsIgnoreCase(title, jobAlert.getPosition());
    boolean townMatch = !hasText(jobAlert.getTown()) || containsIgnoreCase(town, jobAlert.getTown());
    boolean cityMatch = !hasText(jobAlert.getCity()) || containsIgnoreCase(city, jobAlert.getCity());
    boolean prefMatch = !hasText(jobAlert.getWorkingPreference())
                        || equalsIgnoreCase(workingPreference, jobAlert.getWorkingPreference());
    return positionMatch && townMatch && cityMatch && prefMatch;
  }

  private static boolean containsIgnoreCase(String source, String target) {
    return hasText(source)
           && hasText(target)
           && source.toLowerCase().contains(target.toLowerCase());
  }

  private static boolean equalsIgnoreCase(String source, String target) {
    return hasText(source)
           && hasText(target)
           && source.equalsIgnoreCase(target);
  }

  public String location() {
    if (hasText(town)) {
      return town;
    }
    if (hasText(city)) {
      return city;
    }
    return country;
  }
}
