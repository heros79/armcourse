package com.course.aca.model.entity;

/**
 * Entity class. Contains file's full information.
 *
 * @author Anna
 * @author Norik
 * @author Narek
 */
public class FileEntity {

    private int id;
    private boolean isPublic;
    private String name;
    private String resourceType;
    private String resourceName;
    private int sectionId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public int getSectionId() {
        return sectionId;
    }

    public void setSectionId(int sectionId) {
        this.sectionId = sectionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FileEntity that = (FileEntity) o;

        if (id != that.id) return false;
        if (isPublic != that.isPublic) return false;
        if (sectionId != that.sectionId) return false;
        if (!name.equals(that.name)) return false;
        if (!resourceType.equals(that.resourceType)) return false;
        return resourceName.equals(that.resourceName);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (isPublic ? 1 : 0);
        result = 31 * result + name.hashCode();
        result = 31 * result + resourceType.hashCode();
        result = 31 * result + resourceName.hashCode();
        result = 31 * result + sectionId;
        return result;
    }
}
