package com.example.testing.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ObjectUtils;

/**
 * Elastic search configuration
 *
 * @author Debsankar Jana (GSIHYD-1413) <p> Implemented On: 8 July, 2019 <p>
 * @version 1.0
 * @since 1.0
 */

@Slf4j
@Configuration
public class ElasticConfiguration {

  @Value("${elastic.host}")
  private String host;

  @Value("${elastic.port}")
  private int port;

  @Value("${elastic.username}")
  private String username;

  @Value("${elastic.password}")
  private String password;

  @Bean
  public RestHighLevelClient client() {

    log.debug("Connecting to elastic-search host {}", host);
    final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
    credentialsProvider.setCredentials(AuthScope.ANY,
        new UsernamePasswordCredentials(username, password));
    RestClientBuilder builder = null;
    log.info("-------elastic host : {}", host);
    log.info("-------elastic port : {}", port);
    log.info("-------elastic username : {}", username);
    log.info("-------elastic password : {}", password);

    if (ObjectUtils.isEmpty(username) && ObjectUtils.isEmpty(password)) {
      builder = RestClient.builder(new HttpHost(host, port, "http"));
    } else {
      log.info("-------credentials provided, trying to connect to elastic");
      builder = RestClient.builder(new HttpHost(host, port, "https"))
          .setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder
              .setDefaultCredentialsProvider(credentialsProvider));
    }

    RestHighLevelClient restHighLevelClient = new RestHighLevelClient(builder);
    log.info("-----Successfully connected to elastic-search host");
    return restHighLevelClient;

  }
}
