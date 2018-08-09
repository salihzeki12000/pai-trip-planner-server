package edu.hanyang.trip_planning.optimize.constraints.categoryConstraint;

public class CategoryConstraintFactory {
    // Lunch constraints       시간 11:30~13:30, 횟수 1
    public static CategoryConstraint createLunchConstraint() {
        return new CategoryConstraint(12.00, 13.50, 1, 1, "음식점");
    }

    // Dinner constraints       시간 17:00~20:00, 횟수 1
    public static CategoryConstraint createDinnerConstraint() {
        return new CategoryConstraint(17.50, 19.00, 1, 1, "음식점");
    }

    // Shopping constraints     시간 09:00~16:00, 횟수 1
    public static CategoryConstraint createShoppingConstraint() {
        return new CategoryConstraint(09.00, 18.00, 1, 1, "쇼핑");
    }
}
