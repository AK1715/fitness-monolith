package com.project.fitness.service;

import com.project.fitness.dto.RecommendationRequest;
import com.project.fitness.dto.RecommendationResponse;
import com.project.fitness.modal.Activity;
import com.project.fitness.modal.Recommendation;
import com.project.fitness.modal.User;
import com.project.fitness.repository.ActivitiesRepository;
import com.project.fitness.repository.RecommendationRepository;
import com.project.fitness.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecommendationService {

    private final UserRepository userRepository;
    private final ActivitiesRepository activitiesRepository;
    private final RecommendationRepository recommendationRepository;

    public Recommendation generateRecommendation(RecommendationRequest request){
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(()-> new RuntimeException("User Not Found: " + request.getUserId()));

        Activity activity = activitiesRepository.findById(request.getActivityId())
                .orElseThrow(()-> new RuntimeException("Activity Not Found: " + request.getActivityId()));

        Recommendation recommendation = Recommendation.builder()
                .user(user)
                .activity(activity)
                .type(request.getType())
                .recommendation(request.getRecommendation())
                .improvements(request.getImprovements())
                .suggestions(request.getSuggestions())
                .safety(request.getSafety())
                .build();

        return recommendationRepository.save(recommendation);

    }

    private RecommendationResponse toMapResponse(Recommendation recommendation) {
        RecommendationResponse response = new RecommendationResponse();
        response.setId(recommendation.getId());
        response.setUserId(recommendation.getUser().getId());
        response.setActivityId(recommendation.getActivity().getId());
        response.setType(recommendation.getType());
        response.setRecommendation(recommendation.getRecommendation());
        response.setImprovements(recommendation.getImprovements());
        response.setSuggestions(recommendation.getSuggestions());
        response.setSafety(recommendation.getSafety());

        return response;
    }

//    public List<RecommendationResponse> getRecommendationByUserId(String userId) {
//        List<Recommendation> recommendationList = recommendationRepository.findByUserId(userId);
//
//        return recommendationList.stream()
//                .map(this::toMapResponse)
//                .collect(Collectors.toList());
//    }

    public List<Recommendation> getUserRecommendation(String userId){
        return recommendationRepository.findByUserId(userId);
    }


    public List<RecommendationResponse> getRecommendationByActivityId(String activityId) {
        List<Recommendation> recommendationsActivityId = recommendationRepository.findByActivityId(activityId);

        return recommendationsActivityId.stream()
                .map(this::toMapResponse)
                .collect(Collectors.toList());
    }
}
