package com.backend.Service.Impl;

import com.backend.Dto.Request.CreateSubscriptionPlanRequest;
import com.backend.Dto.Request.CreateSubscriptionRequest;
import com.backend.Dto.Response.SubscriptionPlanResponse;
import com.backend.Dto.Response.SubscriptionResponse;
import com.backend.Entity.Subscription;
import com.backend.Entity.SubscriptionPlan;
import com.backend.Entity.User;
import com.backend.Enum.SubscriptionStatus;
import com.backend.Repository.SubscriptionPlanRepository;
import com.backend.Repository.SubscriptionRepository;
import com.backend.Repository.UserRepository;
import com.backend.Service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public SubscriptionPlanResponse createSubscriptionPlan(CreateSubscriptionPlanRequest request) {
        SubscriptionPlan plan = SubscriptionPlan.builder()
                .name(request.getName())
                .planType(request.getPlanType())
                .totalWashes(request.getTotalWashes())
                .durationDays(request.getDurationDays())
                .price(request.getPrice())
                .active(request.getActive())
                .description(request.getDescription())
                .build();
        SubscriptionPlan savedPlan = subscriptionPlanRepository.save(plan);
        return SubscriptionPlanResponse.convertTo(savedPlan);
    }

    @Override
    @Transactional
    public SubscriptionPlanResponse updateSubscriptionPlan(String planId, CreateSubscriptionPlanRequest request) {
        SubscriptionPlan plan = subscriptionPlanRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("Subscription plan not found"));
        plan.setName(request.getName());
        plan.setPlanType(request.getPlanType());
        plan.setTotalWashes(request.getTotalWashes());
        plan.setDurationDays(request.getDurationDays());
        plan.setPrice(request.getPrice());
        plan.setActive(request.getActive());
        plan.setDescription(request.getDescription());
        SubscriptionPlan updatedPlan = subscriptionPlanRepository.save(plan);
        return SubscriptionPlanResponse.convertTo(updatedPlan);
    }

    @Override
    @Transactional
    public void deleteSubscriptionPlan(String planId) {
        SubscriptionPlan plan = subscriptionPlanRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("Subscription plan not found"));
        subscriptionPlanRepository.delete(plan);
    }

    @Override
    @Transactional(readOnly = true)
    public SubscriptionPlanResponse getSubscriptionPlan(String planId) {
        SubscriptionPlan plan = subscriptionPlanRepository.findById(planId)
                .orElseThrow(() -> new IllegalArgumentException("Subscription plan not found"));
        return SubscriptionPlanResponse.convertTo(plan);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubscriptionPlanResponse> getAllSubscriptionPlans() {
        return subscriptionPlanRepository.findAll()
                .stream()
                .map(SubscriptionPlanResponse::convertTo)
                .collect(Collectors.toList());
    }

    @Override
    public SubscriptionResponse purchaseSubscription(CreateSubscriptionRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        SubscriptionPlan plan = subscriptionPlanRepository.findById(request.getPlanId())
                .filter(SubscriptionPlan::getActive)
                .orElseThrow(() -> new IllegalArgumentException("Plan not available"));

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = plan.getDurationDays() == null ? null : startDate.plusDays(plan.getDurationDays());
        Subscription subscription = Subscription.builder()
                .user(user)
                .plan(plan)
                .planName(plan.getName())
                .planType(plan.getPlanType())
                .totalWashes(plan.getTotalWashes())
                .usedWashes(0)
                .startDate(startDate)
                .endDate(endDate)
                .price(plan.getPrice())
                .subStatus(SubscriptionStatus.ACTIVE)
                .build();
        Subscription savedSubscription = subscriptionRepository.save(subscription);

        return SubscriptionResponse.builder()
                .subscriptionId(savedSubscription.getSubscriptionId())
                .planId(plan.getPlanId())
                .planName(plan.getName())
                .planType(plan.getPlanType())
                .totalWashes(plan.getTotalWashes())
                .usedWashes(savedSubscription.getUsedWashes())
                .startDate(savedSubscription.getStartDate())
                .endDate(savedSubscription.getEndDate())
                .price(savedSubscription.getPrice())
                .subStatus(savedSubscription.getSubStatus())
                .build();
    }
}