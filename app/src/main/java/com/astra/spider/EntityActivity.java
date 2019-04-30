package com.astra.spider;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class EntityActivity extends AppCompatActivity {
    private static final int MODE_CREATE = 1;
    private static final int MODE_EDIT = 2;
    private int mode;

    private Entity entity;
    private EditText textName;
    private EditText textDescription;

    private boolean needRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_theme);

        this.textName = this.findViewById(R.id.text_note_title);
        this.textDescription = this.findViewById(R.id.text_note_content);

        Intent intent = this.getIntent();
        this.entity = (Entity) intent.getSerializableExtra(String.valueOf(R.string.ACTIVITY_ENTITY));
        if(entity == null)  {
            this.mode = MODE_CREATE;
        } else  {
            this.mode = MODE_EDIT;
            this.textName.setText(entity.getName());
            this.textDescription.setText(entity.getDescription());
        }

    }

    public void buttonSaveClicked(View view)  {
        MyDatabaseHelper db = new MyDatabaseHelper(this);

        String name = this.textName.getText().toString();
        String description = this.textDescription.getText().toString();

        if(name.equals("") || description.equals("")) {
            Toast.makeText(getApplicationContext(),"Please enter name & description", Toast.LENGTH_LONG).show();
            return;
        }

        if(mode == MODE_CREATE ) {
            this.entity = new Entity(name, description);
            db.addEntity(entity);
        } else {
            this.entity.setName(name);
            this.entity.setDescription(description);
            db.updateNote(entity);
        }

        this.needRefresh = true;
        this.onBackPressed();
    }

    public void buttonCancelClicked(View view)  {
        this.onBackPressed();
    }

    @Override
    public void finish() {
        Intent data = new Intent();
        data.putExtra(String.valueOf(R.string.ACTIVITY_NEED_REFRESH), needRefresh);

        this.setResult(Activity.RESULT_OK, data);
        super.finish();
    }
}