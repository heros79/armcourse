package com.course.aca.model.dto;

import java.util.List;

/**
 * Dedicated to store combined data about categories
 * and subCategories. It's objects are Data Transfer Objects.
 *
 * @author narek
 */
public class CategoryInformation {

    /**
     * This field stores category's information (id, name).
     */
    private Category category;

    /**
     * This field stores subCategories' information, associated with category.
     */
    private List<SubCategory> subCategories;

    public CategoryInformation() {
    }

    public CategoryInformation(Category category, List<SubCategory> subCategories) {
        this.category = category;
        this.subCategories = subCategories;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<SubCategory> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(List<SubCategory> subCategories) {
        this.subCategories = subCategories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CategoryInformation that = (CategoryInformation) o;

        if (!category.equals(that.category)) return false;
        return subCategories.equals(that.subCategories);
    }

    @Override
    public int hashCode() {
        int result = category.hashCode();
        result = 31 * result + subCategories.hashCode();
        return result;
    }
}
