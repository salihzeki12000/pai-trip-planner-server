package edu.hanyang.trip_planning.optimize.constraints.categoryConstraint;

public class CategoryConstraintFactory {
    // Lunch constraints       시간 11:30~13:30, 횟수 1
    public static CategoryConstraint createLunchConstraint() {
        CategoryConstraint categoryConstraint = new CategoryConstraint(12.00, 13.50, 1, 1, "음식점");
        return categoryConstraint;
    }
    // Dinner constraints       시간 17:00~20:00, 횟수 1
    public static CategoryConstraint createDinnerConstraint() {
        CategoryConstraint categoryConstraint = new CategoryConstraint(17.50, 19.00, 1, 1, "음식점");
        return categoryConstraint;
    }
    // Shopping constraints     시간 09:00~16:00, 횟수 1
    public static CategoryConstraint createShoppingConstraint() {
        CategoryConstraint categoryConstraint = new CategoryConstraint(09.00, 18.00, 1, 1, "쇼핑");
        return categoryConstraint;
    }
}
