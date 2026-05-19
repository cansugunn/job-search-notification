package com.jobsearch.notification.client;

import com.jobsearch.notification.service.SystemTokenService;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
@RequiredArgsConstructor
public class ClientConfig {

  @Value("${job-search.api.base-url}")
  private String jobSearchApiBaseUrl;

  private final SystemTokenService systemTokenService;

  @Bean
  public JobSearchAdminClient jobSearchAdminClient() {
    RestClient restClient = RestClient.builder()
                                      .baseUrl(jobSearchApiBaseUrl)
                                      .requestInterceptor(tokenInterceptor())
                                      .build();
    RestClientAdapter adapter = RestClientAdapter.create(restClient);
    HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
    return factory.createClient(JobSearchAdminClient.class);
  }

  @Bean
  public JobSearchPublicClient jobSearchPublicClient() {
    RestClient restClient = RestClient.builder()
                                      .baseUrl(jobSearchApiBaseUrl)
                                      .build();
    RestClientAdapter adapter = RestClientAdapter.create(restClient);
    HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
    return factory.createClient(JobSearchPublicClient.class);
  }

  private ClientHttpRequestInterceptor tokenInterceptor() {
    return (request, body, execution) -> {
      request.getHeaders().setBearerAuth(systemTokenService.getToken());
      ClientHttpResponse clientHttpResponse = execution.execute(request, body);
      if (Objects.equals(clientHttpResponse.getStatusCode(), HttpStatus.UNAUTHORIZED)) {
        systemTokenService.evict();
        request.getHeaders().setBearerAuth(systemTokenService.getToken());
        clientHttpResponse = execution.execute(request, body);
      }
      return clientHttpResponse;
    };
  }
}
