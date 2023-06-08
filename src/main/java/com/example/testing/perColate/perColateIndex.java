package com.example.testing.perColate;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;

@RestController
public class perColateIndex {

    @Autowired
    private final RestHighLevelClient client;

   @Autowired
    public perColateIndex(RestHighLevelClient client) {
        this.client = client;
    }

    @PostMapping(value = "/createIndex")
    public boolean createIndex(@RequestParam String indexName, @RequestBody String indexMapping) throws IOException {
       boolean created=false;
        CreateIndexRequest request = new CreateIndexRequest(indexName);
        request.settings(Settings.builder()
                .put("index.number_of_shards", 1)
                .put("index.number_of_replicas", 0));

        request.source(indexMapping, XContentType.JSON);

        CreateIndexResponse response = client.indices().create(request, RequestOptions.DEFAULT);
        boolean acknowledged = response.isAcknowledged();
        boolean shardsAcknowledged = response.isShardsAcknowledged();

        if (acknowledged && shardsAcknowledged) {
            System.out.println("Index created successfully.");
            created=true;
        } else {
            System.err.println("Failed to create index.");
        }
        return created;
    }
}
