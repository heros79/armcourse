package com.course.aca.model.dto;

/**
 * Data Transfer Object for File information.
 *
 * @author Anna
 * @author Norik
 * @author Narek
 */
public class FileItem {
    private int id;
    private String name;

    public FileItem(int id, String name, boolean isPublic) {
        this.id = id;
        this.name = name;
    }

    public FileItem() {
    }

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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FileItem)) return false;

        FileItem fileItem = (FileItem) o;

        if (getId() != fileItem.getId()) return false;
        return getName().equals(fileItem.getName());
    }

    @Override
    public int hashCode() {
        int result = getId();
        result = 31 * result + getName().hashCode();
        return result;
    }
}
