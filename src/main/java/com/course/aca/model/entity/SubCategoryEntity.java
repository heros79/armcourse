package com.course.aca.model.entity;

/**
 * Entity class. Contains Subcategory's information.
 *
 * @author Anna
 * @author Norik
 * @author Narek
 */
public class SubCategoryEntity {

    private int id;
    private String name;
    private int categoryId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SubCategoryEntity that = (SubCategoryEntity) o;

        if (id != that.id) return false;
        if (categoryId != that.categoryId) return false;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + name.hashCode();
        result = 31 * result + categoryId;
        return result;
    }
}
