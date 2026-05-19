package com.jobsearch.notification.data.dto.client.response;

import java.util.List;

public record PageResponse<T>(List<T> content, boolean last) {

}
