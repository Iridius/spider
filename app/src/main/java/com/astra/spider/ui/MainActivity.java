package com.astra.spider.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.astra.spider.R;
import com.facebook.stetho.Stetho;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Основной фрагмент, в котором происходит просмотр данных */
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