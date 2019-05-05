package com.astra.spider.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.astra.spider.R;
import com.facebook.stetho.Stetho;

public class MainActivity extends AppCompatActivity {
    private String mEntityName; // текущая редактируемая сущность (в активном фрагменте)
    private int mID; // ключ текущей сущности

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEntityName = "";
        mID = 0;

        FragmentManager fm = getSupportFragmentManager();
        SimpleListFragment fragment = (SimpleListFragment) fm.findFragmentById(R.id.fragment_container);
        if(fragment == null){
            fragment = new SimpleListFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }

        Stetho.initializeWithDefaults(this);
    }
}