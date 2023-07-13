package com.example.testing.paramquery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/parametrized-queries")
public class ParametrizedQueryController {

    @Autowired
    private ParametrizedQueryService queryService;

    @PostMapping
    public ParametrizedQuery createParametrizedQuery(@RequestBody ParametrizedQuery query) {
        return queryService.createParametrizedQuery(query);
    }

    @GetMapping("/{id}")
    public ParametrizedQuery getParametrizedQueryById(@PathVariable String id) {
        return queryService.getParametrizedQueryById(id);
    }

    @PutMapping("/{id}")
    public ParametrizedQuery updateParametrizedQuery(@PathVariable String id, @RequestBody ParametrizedQuery updatedQuery) {
        return queryService.updateParametrizedQuery(id, updatedQuery);
    }

    @DeleteMapping("/{id}")
    public void deleteParametrizedQuery(@PathVariable String id) {
        queryService.deleteParametrizedQuery(id);
    }
}
