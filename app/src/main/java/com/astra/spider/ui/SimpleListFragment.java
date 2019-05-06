package com.astra.spider.ui;

import android.R.layout;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.astra.spider.R;
import com.astra.spider.dao.CurrentEntity;
import com.astra.spider.dao.Entity;
import com.astra.spider.database.MyDatabaseHelper;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SimpleListFragment extends Fragment{
    private MainActivity activity;
    private MyDatabaseHelper db;

    private ListView listView;
    private ArrayAdapter listAdapter;
    List<Entity> list;

    private static final int MENU_ITEM_VIEW = 111;
    private static final int MENU_ITEM_EDIT = 222;
    private static final int MENU_ITEM_CREATE = 333;
    private static final int MENU_ITEM_DELETE = 444;
    private static final int MY_REQUEST_CODE = 1000;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_simple, container, false);

        activity = (MainActivity) this.getActivity();
        db = new MyDatabaseHelper(activity);

        list = getData();
        listAdapter = new ArrayAdapter<>(activity, layout.simple_list_item_1, android.R.id.text1, list);
        listView = view.findViewById(R.id.listView);
        listView.setAdapter(listAdapter);

        /* Контекстное меню */
        registerForContextMenu(listView);

        return view;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, view, menuInfo);

        menu.setHeaderTitle("Действие");
        menu.add(0, MENU_ITEM_VIEW , 0, "Просмотр");
        menu.add(0, MENU_ITEM_CREATE , 1, "Создание...");
        menu.add(0, MENU_ITEM_EDIT , 2, "Редактирование...");
        menu.add(0, MENU_ITEM_DELETE, 4, "Удаление");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        Intent intent;
        final Entity currentEntity = (Entity) this.listView.getItemAtPosition(info.position);

        switch(item.getItemId()){
            case MENU_ITEM_VIEW:
                if(CurrentEntity.getInstance().getEntityName().length() == 0){
                    /* Выбрана сущность для просмотра и редактирования */
                    CurrentEntity.getInstance().setEntityName(currentEntity.getName());
                    list.clear();
                    list.addAll(getData());
                    listAdapter.notifyDataSetChanged();
                    //Toast.makeText(activity.getApplicationContext(), currentEntity.getName(), Toast.LENGTH_LONG).show();
                } else {
                    /* Выбрана какая-то запись текущей сущности */
                    Toast.makeText(activity.getApplicationContext(), currentEntity.getDescription(), Toast.LENGTH_LONG).show();
                }
                break;
            case MENU_ITEM_CREATE:
                intent = new Intent(activity, EntityActivity.class);

                this.startActivityForResult(intent, MY_REQUEST_CODE);
                break;
            case MENU_ITEM_EDIT:
                intent = new Intent(activity, EntityActivity.class);
                intent.putExtra(String.valueOf(R.string.ACTIVITY_ENTITY), currentEntity);

                this.startActivityForResult(intent,MY_REQUEST_CODE);
                break;
            case MENU_ITEM_DELETE:
                new AlertDialog.Builder(activity)
                        .setMessage(currentEntity.getName()+". Вы уверены, что хотите удалить запись?")
                        .setCancelable(false)
                        .setPositiveButton("Да", new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id) {deleteEntity(currentEntity);
                            }
                        })
                        .setNegativeButton("Нет", null)
                        .show();
                break;
            default:
                return false;
        }
        return true;
    }

    private void deleteEntity(Entity entity)  {
        db.deleteEntity(entity);

        list.remove(entity);
        listAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == MY_REQUEST_CODE ) {
            boolean needRefresh = data.getBooleanExtra(String.valueOf(R.string.ACTIVITY_NEED_REFRESH),true);

            if(needRefresh) {
                list.clear();
                list.addAll(db.getEntities(CurrentEntity.getInstance().getEntityName()));
                listAdapter.notifyDataSetChanged();
            }
        }
    }

    /* Получение данных из базы данных, - либо заданная таблица, либо список доступных таблиц */
    private List<Entity> getData() {
        String entityName = CurrentEntity.getInstance().getEntityName();

        return new ArrayList<>(db.getEntities(entityName));
    }
}