package com.example.testing.perColate;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.http.*;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


@RestController
public class perColateQuery {



    private final RestHighLevelClient elasticsearchClient;

    public perColateQuery(RestHighLevelClient elasticsearchClient) {
        this.elasticsearchClient = elasticsearchClient;
    }


    @PostMapping(value = "/executePercolateQuery")
    public Object executePercolateQuery(
            @RequestParam String indexName,
            @RequestParam(required = false) String queryType,
            @RequestBody Map<String, Object> document,
            @RequestParam(required = false) List<String> groups,
            @RequestParam(required = false) List<String> contexts) {
        Map<String,List<String>> response=new HashMap<>();
        if (!StringUtils.isEmpty(queryType) && queryType.equalsIgnoreCase("group"))
            response.put("group",executePercolateQuery(indexName, queryType, document, groups));
        else if (!StringUtils.isEmpty(queryType) && queryType.equalsIgnoreCase("context"))
            response.put("context",executePercolateQuery(indexName, queryType, document, contexts));
        else {
            response.put("groups",executePercolateQuery(indexName, "group", document, groups));
            response.put("contexts",executePercolateQuery(indexName, "context", document, contexts));
        }
        return response;

    }

    private List<String> executePercolateQuery(String indexName, String queryType, Map<String, Object> document, List<String> filter) {

        String url = "http://localhost:9200/" + indexName + "/_search";

        // Request headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Request body
        Map<String, Object> requestBody = new HashMap<>();
        Map<String, Object> query = new HashMap<>();
        Map<String, Object> percolate = new HashMap<>();
        document.put("query_type",queryType);
        percolate.put("field", "query");
        percolate.put("document", document);
        query.put("percolate", percolate);
        requestBody.put("query", query);

        System.out.println(requestBody.toString());

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
        RestTemplate restTemplate=new RestTemplate();
        // Send the request

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

        if(!Objects.isNull(filter))
            return getIntersection(filter,extractHitIds(response.getBody()));
        return extractHitIds(response.getBody());
    }

    public static List<String> extractHitIds(String response) {
        List<String> hitIds = new ArrayList<>();

        // Parse the response string
        JSONObject jsonResponse = new JSONObject(response);
        JSONObject hitsObject = jsonResponse.getJSONObject("hits");
        JSONArray hitsArray = hitsObject.getJSONArray("hits");

        // Check if there are no hits
        if (hitsArray == null || hitsArray.length() == 0) {
            return hitIds;
        }

        // Iterate over each hit and extract the ID
        for (int i = 0; i < hitsArray.length(); i++) {
            JSONObject hitObject = hitsArray.getJSONObject(i);
            String hitId = hitObject.getString("_id");
            hitIds.add(hitId);
        }

        return hitIds;
    }
    public static List<String> getIntersection(List<String> list1, List<String> list2) {
        List<String> intersection = new ArrayList<>();

        for (String element : list1) {
            if (list2.contains(element)) {
                intersection.add(element);
            }
        }

        return intersection;
    }

}
