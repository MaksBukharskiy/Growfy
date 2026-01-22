package com.example.interaction.repository;

import com.example.interaction.model.HRManager;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HRManagerRepository extends MongoRepository<HRManager, String> {
}