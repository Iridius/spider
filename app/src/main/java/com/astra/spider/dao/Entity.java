package com.astra.spider.dao;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class Entity implements Serializable {

    private int id;
    private String name;
    private String description;

    public Entity(){
    }

    Entity(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Entity(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @NotNull
    @Override
    public String toString()  {
        return this.name;
    }
}
