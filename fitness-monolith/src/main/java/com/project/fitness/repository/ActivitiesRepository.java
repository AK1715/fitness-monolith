package com.project.fitness.repository;

import com.project.fitness.modal.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivitiesRepository extends JpaRepository<Activity, String> {
    // Explaining this method in sql query (find) means it's a Select (By) means Where (UserId) means it's a column name
    // Full query is Select * from Activity where `userId` = $userId
    List<Activity> findByUserId(String userId);
}

