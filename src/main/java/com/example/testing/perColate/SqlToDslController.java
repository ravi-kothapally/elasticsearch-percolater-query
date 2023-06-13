package com.example.testing.perColate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.json.JsonObject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class SqlToDslController {
    private final RestHighLevelClient client;

    @Autowired
    public SqlToDslController(RestHighLevelClient client) {
        this.client = client;
    }


    @PostMapping("/sql2dsl")
    public Object convertSqlToDsl(@RequestParam  String index, @RequestBody String sqlQuery,@RequestParam String docId,@RequestParam String query_type) throws JsonProcessingException {
        // Set the Elasticsearch SQL Translate API URL
        String url = "http://localhost:9200/_sql/translate";

        // Create the request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String nestedQuery = "select * from (" + sqlQuery + ") where query_type='"+query_type+"'";


        // Create the request body
        String requestBody = "{\"query\": \"" + nestedQuery + "\", \"fetch_size\": 10}";

        // Create the HTTP entity
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        // Send the HTTP POST request to the Elasticsearch SQL Translate API
        RestTemplate restTemplate=new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        // Get the translated Elasticsearch Query DSL
        String dslQuery = response.getBody();

        try {
            // Parse the Elasticsearch Query DSL string into a JsonNode object
            ObjectMapper objectMapper=new ObjectMapper();
            JsonNode dslQueryNode = objectMapper.readTree(dslQuery);
            System.out.println(dslQuery);
            // Check if the "query" property exists
            if (dslQueryNode.has("query")) {
                // Get the "query" object
                JsonNode queryNode = dslQueryNode.get("query");

                HashMap<String,JsonNode> query=new HashMap<>();
                query.put("query",queryNode);
                // Store the query in the Elasticsearch index
                IndexRequest indexRequest = new IndexRequest(index).id(docId)
                        .source(objectMapper.writeValueAsString(query), XContentType.JSON);
                IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);

                // Return the stored query
                return indexResponse;
            } else {
                return "No 'query' property found in the Elasticsearch Query DSL.";
            }
        } catch (JsonProcessingException e) {
            // Handle the JSON parsing exception
            return "Error parsing Elasticsearch Query DSL: " + e.getMessage();
        } catch (IOException e) {
            // Handle the Elasticsearch indexing exception
            return "Error storing query in Elasticsearch: " + e.getMessage();
        }
    }

    @PostMapping("/sql2dsl2")
    public String convertSqlToDsl(@RequestParam String modifiedQuery) {

        // Set the Elasticsearch SQL Translate API URL
        String url = "http://localhost:9200/_sql/translate";

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            // Create the HTTP POST request
            HttpPost request = new HttpPost(url);

            // Set the request headers
            request.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

            // Create the request body
            String requestBody = "{\"query\": \"" + modifiedQuery + "\", \"fetch_size\": 10}";

            // Set the request body
            request.setEntity(new StringEntity(requestBody));

            // Send the HTTP POST request to the Elasticsearch SQL Translate API
            HttpResponse response = httpClient.execute(request);

            // Extract the response body
            org.apache.http.HttpEntity entity = response.getEntity();
            String responseBody = EntityUtils.toString(entity);

            // Get the translated Elasticsearch Query DSL
            String dslQuery = responseBody;

            return dslQuery;
        } catch (Exception e) {
            // Handle any exceptions
            e.printStackTrace();
            return null;
        }
    }
}
