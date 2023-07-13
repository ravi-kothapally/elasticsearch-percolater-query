package com.example.testing.paramquery;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParametrizedQueryRepository extends MongoRepository<ParametrizedQuery,String> {


    Optional<ParametrizedQuery> findById(String id);
}
