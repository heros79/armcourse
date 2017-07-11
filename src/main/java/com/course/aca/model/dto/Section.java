package com.course.aca.model.dto;

/**
 * Data Transfer Object for Section information.
 *
 * @author Anna
 * @author Norik
 * @author Narek
 */
public class Section {

    private int id;
    private String name;

    public Section(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Section() {
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
}
