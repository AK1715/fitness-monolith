package com.project.fitness.service;

import com.project.fitness.dto.ActivityRequest;
import com.project.fitness.dto.ActivityResponse;
import com.project.fitness.modal.Activity;
import com.project.fitness.modal.User;
import com.project.fitness.repository.ActivitiesRepository;
import com.project.fitness.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ActivitiesService {

    private final ActivitiesRepository activitiesRepository;
    private final UserRepository userRepository;

    public ActivityResponse trackActivity(ActivityRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() ->  new RuntimeException("Invalid User: " + request.getUserId()));
        Activity activity = Activity.builder()
                .user(user)
                .type(request.getType())
                .duration(request.getDuration())
                .startTime(request.getStartTime())
                .caloriesBurned(request.getCaloriesBurned())
                .additionalMetrics(request.getAdditionalMetrics())
                .build();

        Activity savedActivity = activitiesRepository.save(activity);

        return toMapResponse(savedActivity);

    }

    private ActivityResponse toMapResponse(Activity activity) {
        ActivityResponse response = new ActivityResponse();
        response.setId(activity.getId());
        response.setUserId(activity.getUser().getId());
        response.setType(activity.getType());
        response.setDuration(activity.getDuration());
        response.setCaloriesBurned(activity.getCaloriesBurned());
        response.setStartTime(activity.getStartTime());
        response.setAdditionalMetrics(activity.getAdditionalMetrics());
        response.setCreatedAt(activity.getCreatedAt());
        response.setUpdatedAt(activity.getUpdatedAt());

        return response;
    }

    public @Nullable List<ActivityResponse> getUserActivities(String userId) {
        List<Activity> activityList = activitiesRepository.findByUserId(userId);
        // 1. Activity ko ActivityResponse mai transform karna hai
        // 2. List mai collect karna hai and usko return kr dena hai
        return  activityList.stream()
                .map(this::toMapResponse)
                .collect(Collectors.toList());

    }
}
