package com.astra.spider;

import java.io.Serializable;

public class Entity /*implements Serializable*/ {

    private int id;
    private String name;
    private String description;

    Entity(){

    }

    Entity(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    Entity(String name, String description) {
        this.name = name;
        this.description = description;
    }

    int getId() {
        return id;
    }

    void setId(int id) {
        this.id = id;
    }

    String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    String getDescription() {
        return description;
    }

    void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString()  {
        return this.name;
    }
}
