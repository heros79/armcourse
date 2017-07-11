package com.course.aca.model.entity;

/**
 * Entity class. Contains File's main information.
 *
 * @author Anna
 * @author Norik
 * @author Narek
 */
public class FileItemEntity {
    private int id;
    private boolean isPublic;
    private String name;

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
}
