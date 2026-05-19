package com.jobsearch.notification.data.dto.supabase;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SupabaseAuthResponse(@JsonProperty("access_token") String accessToken,
                                   @JsonProperty("expires_in") int expiresIn) {

}
