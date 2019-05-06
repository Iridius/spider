package com.astra.spider.dao;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class Entity implements Serializable {

    private int id;
    private String name;
    private String description;
    private String tag;

    public Entity(){
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
        String _tag = "";
        if(tag != null && !tag.equals("0")){
            _tag = " (" + this.tag + ")";
        }

        return this.name + _tag;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
