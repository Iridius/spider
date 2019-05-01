package com.astra.spider.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.astra.spider.R;
import com.astra.spider.dao.Entity;
import com.astra.spider.database.MyDatabaseHelper;
import com.facebook.stetho.Stetho;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private MyDatabaseHelper db;

    private static final int MENU_ITEM_VIEW = 111;
    private static final int MENU_ITEM_EDIT = 222;
    private static final int MENU_ITEM_CREATE = 333;
    private static final int MENU_ITEM_DELETE = 444;
    private static final int MY_REQUEST_CODE = 1000;

    private final List<Entity> entities = new ArrayList<>();
    private ArrayAdapter<Entity> listViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listView);

        db = new MyDatabaseHelper(this);
        List<Entity> list = db.getEntities();
        entities.addAll(list);

        this.listViewAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, entities);
        this.listView.setAdapter(this.listViewAdapter);
        registerForContextMenu(this.listView);

        Stetho.initializeWithDefaults(this);
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
        final Entity selectedNote = (Entity) this.listView.getItemAtPosition(info.position);

        switch(item.getItemId()){
            case MENU_ITEM_VIEW:
                Toast.makeText(getApplicationContext(),selectedNote.getDescription(),Toast.LENGTH_LONG).show();
                break;
            case MENU_ITEM_CREATE:
                intent = new Intent(this, EntityActivity.class);

                this.startActivityForResult(intent, MY_REQUEST_CODE);
                break;
            case MENU_ITEM_EDIT:
                intent = new Intent(this, EntityActivity.class);
                intent.putExtra(String.valueOf(R.string.ACTIVITY_ENTITY), selectedNote);

                this.startActivityForResult(intent,MY_REQUEST_CODE);
                break;
            case MENU_ITEM_DELETE:
                new AlertDialog.Builder(this)
                        .setMessage(selectedNote.getName()+". Вы уверены, что хотите удалить запись?")
                        .setCancelable(false)
                        .setPositiveButton("Да", new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id) {deleteEntity(selectedNote);
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

        this.entities.remove(entity);
        this.listViewAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == MY_REQUEST_CODE ) {
            boolean needRefresh = data.getBooleanExtra(String.valueOf(R.string.ACTIVITY_NEED_REFRESH),true);

            if(needRefresh) {
                this.entities.clear();
                this.entities.addAll(db.getEntities());
                this.listViewAdapter.notifyDataSetChanged();
            }
        }
    }
}