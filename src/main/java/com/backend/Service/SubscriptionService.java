package com.backend.Service;

import com.backend.Dto.Request.CreateSubscriptionPlanRequest;
import com.backend.Dto.Request.CreateSubscriptionRequest;
import com.backend.Dto.Response.SubscriptionPlanResponse;
import com.backend.Dto.Response.SubscriptionResponse;

import java.util.List;

public interface SubscriptionService {
    SubscriptionPlanResponse createSubscriptionPlan(CreateSubscriptionPlanRequest request);
    SubscriptionPlanResponse updateSubscriptionPlan(String planId, CreateSubscriptionPlanRequest request);
    void deleteSubscriptionPlan(String planId);
    SubscriptionPlanResponse getSubscriptionPlan(String planId);
    List<SubscriptionPlanResponse> getAllSubscriptionPlans();


    SubscriptionResponse purchaseSubscription(CreateSubscriptionRequest request);

}
