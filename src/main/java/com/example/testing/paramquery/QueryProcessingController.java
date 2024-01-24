package com.example.testing.paramquery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/query-processing")
public class QueryProcessingController {

    @Autowired
    private ParametrizedQueryRepository queryRepository;

    @PostMapping("/{queryId}")
    public String processQuery(@PathVariable String queryId, @RequestBody Map<String, Object> placeholders) {
        ParametrizedQuery query = queryRepository.findById(queryId)
                .orElseThrow(() -> new RuntimeException("Parametrized query not found"));

        String cleanQuery = query.getQuery();

        for (Map.Entry<String, Object> entry : placeholders.entrySet()) {
            String placeholder = "{" + entry.getKey() + "}";
            Object value = entry.getValue();
            cleanQuery = cleanQuery.replaceAll("\\Q" + placeholder + "\\E", value.toString());
        }

        return cleanQuery;
    }

    @GetMapping("/testing")
    public String testing1(@RequestParam Map<String, List<String>> queryParams){

        queryParams.forEach((s, strings) -> {
            System.out.println(s+" "+strings);
        });

        return "done";
    }
}
