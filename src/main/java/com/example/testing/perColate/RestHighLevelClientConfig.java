//package com.example.testing.perColate;
//
//import lombok.extern.slf4j.Slf4j;
//import org.apache.http.HttpHost;
//import org.apache.http.auth.AuthScope;
//import org.apache.http.auth.UsernamePasswordCredentials;
//import org.apache.http.client.CredentialsProvider;
//import org.apache.http.impl.client.BasicCredentialsProvider;
//import org.elasticsearch.client.RestClient;
//import org.elasticsearch.client.RestClientBuilder;
//import org.elasticsearch.client.RestHighLevelClient;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//
//@Slf4j
//@Configuration
//public class RestHighLevelClientConfig {
//
//    @Value("${elastic.host}")
// private String host;
//
// @Value("${elastic.port}")
// private int port;
//
// @Value("${elastic.username}")
// private String username;
//
// @Value("${elastic.password}")
// private String password;
//
//
// @Value("${elastic.env:dev}")
// private String env;
//
// @Bean
// public RestHighLevelClient client() {
//
//  final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
//  credentialsProvider.setCredentials(AuthScope.ANY,
//      new UsernamePasswordCredentials(username, password));
//
//  RestClientBuilder builder = null;
//  if (env.equalsIgnoreCase("dev")) {
//   builder = RestClient.builder(new HttpHost(host, port, "https"));
//  } else {
//   builder = RestClient.builder(new HttpHost(host, port, "https"))
//       .setHttpClientConfigCallback(httpClientBuilder ->
//           httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider));
//  }
//
//  RestHighLevelClient restHighLevelClient = new RestHighLevelClient(builder);
//  log.info("-----Successfully connected to elastic-search host");
//
//  return restHighLevelClient;
//
// }
//}
//
