package com.backend.Controller;

import com.backend.Dto.Request.CreateSubscriptionPlanRequest;
import com.backend.Dto.Request.CreateSubscriptionRequest;
import com.backend.Dto.Response.ApiResponse;
import com.backend.Dto.Response.SubscriptionPlanResponse;
import com.backend.Dto.Response.SubscriptionResponse;
import com.backend.Service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/subscriptions")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping("/create-subscription-plan")
    public ResponseEntity<ApiResponse<SubscriptionPlanResponse>> createSubscriptionPlan(
            @RequestBody CreateSubscriptionPlanRequest request) {

        SubscriptionPlanResponse subscriptionPlan = subscriptionService.createSubscriptionPlan(request);
        return ResponseEntity.ok(ApiResponse.success(subscriptionPlan, "Subscription plan created successfully"));
    }

    @PutMapping("/update-subscription-plan/{planId}")
    public ResponseEntity<ApiResponse<SubscriptionPlanResponse>> updateSubscriptionPlan(
            @PathVariable String planId,
            @RequestBody CreateSubscriptionPlanRequest request) {

        SubscriptionPlanResponse updatedPlan = subscriptionService.updateSubscriptionPlan(planId, request);
        return ResponseEntity.ok(ApiResponse.success(updatedPlan, "Subscription plan updated successfully"));
    }

    @DeleteMapping("/delete-subscription-plan/{planId}")
    public ResponseEntity<ApiResponse<Void>> deleteSubscriptionPlan(@PathVariable String planId) {
        subscriptionService.deleteSubscriptionPlan(planId);
        return ResponseEntity.ok(ApiResponse.success(null, "Subscription plan deleted successfully"));
    }

    @GetMapping("/get-subscription-plan/{planId}")
    public ResponseEntity<ApiResponse<SubscriptionPlanResponse>> getSubscriptionPlan(@PathVariable String planId) {
        SubscriptionPlanResponse plan = subscriptionService.getSubscriptionPlan(planId);
        return ResponseEntity.ok(ApiResponse.success(plan, "Subscription plan retrieved successfully"));
    }

    @GetMapping("/get-all-subscription-plans")
    public ResponseEntity<ApiResponse<List<SubscriptionPlanResponse>>> getAllSubscriptionPlans() {
        List<SubscriptionPlanResponse> plans = subscriptionService.getAllSubscriptionPlans();
        return ResponseEntity.ok(ApiResponse.success(plans, "All subscription plans retrieved successfully"));
    }



    //Purchase
    @PostMapping("/purchase/subscription")
    public ResponseEntity<ApiResponse<SubscriptionResponse>> purchaseSubscription(
            @RequestBody CreateSubscriptionRequest request) {

        SubscriptionResponse subscription = subscriptionService.purchaseSubscription(request);
        return ResponseEntity.ok(ApiResponse.success(subscription, "Subscription purchased successfully"));
    }
}