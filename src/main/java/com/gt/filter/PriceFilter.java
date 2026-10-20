package com.gt.filter;

import com.gt.model.Course;

public class PriceFilter
        implements CourseFilterStrategy {

    private final double minPrice;
    private final double maxPrice;

    public PriceFilter(
            double minPrice,
            double maxPrice) {

        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    @Override
    public boolean matches(Course course) {

        return course.getPrice() >= minPrice
                && course.getPrice() <= maxPrice;
    }
}