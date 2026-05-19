package com.jobsearch.notification.data.dto.supabase;

import jakarta.validation.constraints.NotEmpty;

public record SupabaseAuthRequest(@NotEmpty String email,
                                  @NotEmpty String password) {

}
