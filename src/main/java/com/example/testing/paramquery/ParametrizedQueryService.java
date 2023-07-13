package com.example.testing.paramquery;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ParametrizedQueryService {

    @Autowired
    private ParametrizedQueryRepository queryRepository;

    public ParametrizedQuery createParametrizedQuery(ParametrizedQuery query) {
        return queryRepository.save(query);
    }

    public ParametrizedQuery getParametrizedQueryById(String id) {
        return queryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Parametrized query not found"));
    }

    public ParametrizedQuery updateParametrizedQuery(String id, ParametrizedQuery updatedQuery) {
        ParametrizedQuery query = queryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Parametrized query not found"));

        query.setName(updatedQuery.getName());
        query.setDescription(updatedQuery.getDescription());
        query.setTenantId(updatedQuery.getTenantId());
        query.setQuery(updatedQuery.getQuery());
        query.setPlaceHolders(updatedQuery.getPlaceHolders());

        return queryRepository.save(query);
    }

    public void deleteParametrizedQuery(String id) {
        ParametrizedQuery query = queryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Parametrized query not found"));

        queryRepository.delete(query);
    }

    public class QueryProcessingService {
        public String fillPlaceholders(String query, Map<String, Object> placeholders) {
            String filledQuery = query;
            for (Map.Entry<String, Object> entry : placeholders.entrySet()) {
                String placeholder = "{" + entry.getKey() + "}";
                Object value = entry.getValue();
                filledQuery = filledQuery.replace(placeholder, value.toString());
            }
            return filledQuery;
        }
    }
}

