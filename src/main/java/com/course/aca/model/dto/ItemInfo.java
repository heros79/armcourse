package com.course.aca.model.dto;

/**
 * Data Transfer Object for full File information.
 *
 * @author Anna
 * @author Norik
 * @author Narek
 */
public class ItemInfo {
    private boolean isPublic;
    private String name;
    private String resourceName;
    private String resourceType;
    private int sectionId;

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

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
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

        ItemInfo itemInfo = (ItemInfo) o;

        if (isPublic != itemInfo.isPublic) return false;
        if (sectionId != itemInfo.sectionId) return false;
        if (!name.equals(itemInfo.name)) return false;
        if (!resourceName.equals(itemInfo.resourceName)) return false;
        return resourceType.equals(itemInfo.resourceType);
    }

    @Override
    public int hashCode() {
        int result = (isPublic ? 1 : 0);
        result = 31 * result + name.hashCode();
        result = 31 * result + resourceName.hashCode();
        result = 31 * result + resourceType.hashCode();
        result = 31 * result + sectionId;
        return result;
    }
}
