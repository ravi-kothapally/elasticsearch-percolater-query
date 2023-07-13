package com.example.testing.paramquery;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Map;

@Data
@Document(collection = "ParametrizedQuery")
public class ParametrizedQuery implements  Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    private String id;
    private String name;
    private String description;
    private String tenantId;
    private String query;
    private Map<String ,String> placeHolders;
}
