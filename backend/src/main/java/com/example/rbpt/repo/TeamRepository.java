package com.example.rbpt.repo;
import com.example.rbpt.model.Team;
import org.springframework.data.mongodb.repository.MongoRepository;
public interface TeamRepository extends MongoRepository<Team, String> {}
