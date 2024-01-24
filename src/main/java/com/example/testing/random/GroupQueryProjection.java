package com.example.testing.random;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.util.TablesNamesFinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class GroupQueryProjection {

  @Autowired
  private JdbcTemplate jdbcTemplate;

  public static Set<String> extractProjections(String sqlQuery) throws JSQLParserException {
    Set<String> projections = new HashSet<>();

    // Parse the SQL query
    Select select = (Select) CCJSqlParserUtil.parse(sqlQuery);

    // Extract projections
    if (select.getSelectBody() instanceof PlainSelect) {
      PlainSelect plainSelect = (PlainSelect) select.getSelectBody();
      List<?> selectItems = plainSelect.getSelectItems();

      for (Object selectItem : selectItems) {
        if(!selectItem.toString().equals("*"))
          projections.add(selectItem.toString().replace("entity.","").replace("\"",""));
      }
    }

    return projections;
  }

  @GetMapping(value = "/groupprojectionJdbcTemplate")
  public Object GroupProjection(@RequestParam String query)
      throws IOException, JSQLParserException {

    Set<String> defaultProjection = null;
    Set<String> x = populateProjectionList(query);
    if (x != null) {
      if (defaultProjection == null) {
        defaultProjection = new HashSet<>();
      }
      defaultProjection.addAll(x);
    }
    return defaultProjection;


  }

  public Set<String> populateProjectionList(String query) {
    log.info("========query {}", query);

    Set<String> projectionSet = new HashSet<>();

    // Execute the MySQL query and retrieve the result set.
    List<Map<String, Object>> resultSet = jdbcTemplate.queryForList(query);

    // Extract column names (projections) from the query result.
    if (!resultSet.isEmpty()) {
      Map<String, Object> firstRow = resultSet.get(0);
      firstRow.keySet().forEach(column -> {
        column.replace("entity.", "");
        projectionSet.add(column);
      });
    }
    return projectionSet;
  }

  @GetMapping(value = "/groupprojection")
  public Object GroupProjectionParser(@RequestParam String query)
      throws IOException, JSQLParserException {
    query="select \"entity.emails\",* from wallet_userdetails";
    Set<String> projections = extractProjections(query);

    // Print the projections
    for (String projection : projections) {
      System.out.println(projection);
    }
    return projections;
  }


}
