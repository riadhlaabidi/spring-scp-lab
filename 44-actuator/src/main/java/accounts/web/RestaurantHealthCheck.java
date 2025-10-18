package accounts.web;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import rewards.internal.restaurant.RestaurantRepository;

@Component
public class RestaurantHealthCheck implements HealthIndicator {

    private final RestaurantRepository restaurantRepository;

    public RestaurantHealthCheck(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public Health health() {
        long count = restaurantRepository.getRestaurantCount();
        return count > 0L
                ? Health.up().withDetail("restaurant_count", count).build()
                : Health.status("NO_RESTAURANTS").withDetail("restaurantCount", count).build();
    }
}
