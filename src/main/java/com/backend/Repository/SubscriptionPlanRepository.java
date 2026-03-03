package com.backend.Repository;

import com.backend.Entity.Subscription;
import com.backend.Entity.SubscriptionPlan;
import org.jspecify.annotations.NullMarked;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@NullMarked
@Repository
public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, String> {

}
