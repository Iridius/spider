package com.astra.spider.dao;

public class CurrentEntity {
    private String mEntityName; // текущая редактируемая сущность (в активном фрагменте)
    private int mID; // ключ текущей сущности
    private static final CurrentEntity ourInstance = new CurrentEntity();

    public String getEntityName() {
        return mEntityName;
    }

    public void setEntityName(String mEntityName) {
        this.mEntityName = mEntityName;
    }

    public int getID() {
        return mID;
    }

    public void setID(int mID) {
        this.mID = mID;
    }

    public static CurrentEntity getInstance() {
        return ourInstance;
    }

    private CurrentEntity() {
        mEntityName = "";
        mID = 0;
    }
}
