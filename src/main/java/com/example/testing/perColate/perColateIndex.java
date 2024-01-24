package com.example.testing.perColate;

import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.util.EntityUtils;
import org.checkerframework.checker.index.qual.SameLen;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.cluster.metadata.MappingMetadata;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.elasticsearch.client.RequestOptions;
import java.io.IOException;
import java.util.Map;
import org.springframework.web.client.RestTemplate;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.expression.Expression;


@Slf4j
@RestController
public class perColateIndex {
  @Value("${elastic.host}")
  private String host;

    @Autowired
    private final RestHighLevelClient client = null;

//   @Autowired
//    public perColateIndex(RestHighLevelClient client) {
//        this.client = client;
//    }

    @PostMapping(value = "/createIndex")
    public boolean createIndex(@RequestParam String indexName, @RequestBody HashMap<String,Object> indexMapping,@RequestParam String queryType) throws IOException {
       boolean created=false;
        CreateIndexRequest request = new CreateIndexRequest(indexName);
        request.settings(Settings.builder()
                .put("index.number_of_shards", 1)
                .put("index.number_of_replicas", 0));

//        request.source(indexMapping, XContentType.JSON);
        request.mapping(queryType,indexMapping);
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

  @GetMapping(value = "/index")
  public Object getIndex(@RequestParam String indexName) throws IOException {
    try {
      GetIndexRequest request = new GetIndexRequest(indexName);
      GetIndexResponse response = client.indices().get(request, RequestOptions.DEFAULT);

      // Process the response
      Map<String, MappingMetadata> mappings = response.getMappings();
      Settings settings = response.getSettings().get(indexName);

      // You can now work with the mappings and settings
      System.out.println("Mappings: " + mappings);
      System.out.println("Settings: " + settings);
      return response.getMappings();

    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  @GetMapping(value = "/index2")
  public Object getIndex2(@RequestParam String indexName) throws IOException {
    String elasticsearchUrl = "https://"+host+"/bookindex";

    log.info("----------elasticsearchUrl: {}------",elasticsearchUrl);
    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<String> response = restTemplate.getForEntity(elasticsearchUrl, String.class);

    if (response.getStatusCode().is2xxSuccessful()) {
      String responseBody = response.getBody();
      System.out.println(responseBody);
      return responseBody;
    } else {
      System.out.println("Request failed. Status code: " + response.getStatusCode());
    }
    return null;
  }

  @GetMapping(value = "/index3")
  public Object getIndexInfo(String indexName) {
    try {
      Request request = new Request("GET", "/" + indexName);
      Response response = client.getLowLevelClient().performRequest(request);

      int statusCode = response.getStatusLine().getStatusCode();
      if (statusCode == 200) {
        String responseBody = EntityUtils.toString(response.getEntity());
        System.out.println(responseBody);
        return  responseBody;
      } else {
        System.out.println("Request failed. Status code: " + statusCode);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  @PostMapping(value = "/query")
  public Object getIndex4(@RequestBody Map<String,String> query) {
      log.info("------query {}",query.get("query"));
//      String regex = "^SELECT\\s+\\*\\s+FROM\\s+\\w+";
//
//      Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
//
//      if (pattern.matcher(query).find()) {
//        throw new IllegalArgumentException("Invalid query: Projection contains *");
//      }
    try {
      validateQuery(query.get("query"));

    }
    catch (Exception e)
    {
      return "invalid: "+e.getMessage();
    }
      return query;

  }

  public static void validateQuery(String query) throws IllegalArgumentException {
    try {
      net.sf.jsqlparser.statement.Statement stmt = CCJSqlParserUtil.parse(query);
      log.info("-----stmt {}", stmt);
      if (stmt instanceof Select) {
        SelectBody selectBody = ((Select) stmt).getSelectBody();
        if (selectBody instanceof PlainSelect) {
          PlainSelect plainSelect = (PlainSelect) selectBody;
          for (SelectItem selectItem : plainSelect.getSelectItems()) {
            if (selectItem instanceof AllColumns) {
              throw new IllegalArgumentException("Invalid query: Outermost projection contains *");
            } else if (selectItem instanceof AllTableColumns) {
              AllTableColumns allTableColumns = (AllTableColumns) selectItem;
              if (allTableColumns.getTable().getAlias() == null) {
                throw new IllegalArgumentException(
                    "Invalid query: Outermost projection contains *");
              }
            } else if (selectItem instanceof SelectExpressionItem) {
              Expression expression = ((SelectExpressionItem) selectItem).getExpression();
              if (expression instanceof Column) {
                Column column = (Column) expression;
                if (column.getColumnName().equals("*")) {
                  throw new IllegalArgumentException(
                      "Invalid query: Outermost projection contains *");
                }
              }
            }
          }
        }
      }
    } catch (JSQLParserException e) {
      throw new IllegalArgumentException("Invalid query: Unable to parse the query");
    }
  }

}
