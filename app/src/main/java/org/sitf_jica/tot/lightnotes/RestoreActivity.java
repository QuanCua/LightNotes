package org.sitf_jica.tot.lightnotes;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Asus on 2/6/2018.
 */

public class RestoreActivity extends AppCompatActivity {
    TextView txtTitle;
    TextView txtNote;
    TextView txtTime;
    Button btnRestore;
    TextView txtCircle;
    int saveSelectColor = 0;
    ArrayList<Note> arrayListRestore;
    Note note = new Note();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("");
        setContentView(R.layout.activity_restore);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtTitle = (TextView) findViewById(R.id.textViewTitleRestore);
        txtNote = (TextView) findViewById(R.id.textViewNoteRestore);
        txtTime = (TextView) findViewById(R.id.textViewTimeRestore);
        btnRestore = (Button) findViewById(R.id.buttonRestore);
        txtCircle = (TextView) findViewById(R.id.textViewCircle);

        extraData();

        btnRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                Date currentLocalTime = cal.getTime();
                DateFormat date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                String localTime = date.format(currentLocalTime);

                note.setTime(localTime);
                note.setTitle(txtTitle.getText().toString());
                note.setNote(txtNote.getText().toString());
                note.setColor(saveSelectColor);

                MyDatabaseHelper myDB = new MyDatabaseHelper(RestoreActivity.this);
                myDB.addOrUpdate(note);
                myDB.deleteObjectOfTrash(note);

                Intent it = new Intent(RestoreActivity.this, TrashActivity.class);
                startActivity(it);
            }
        });
    }


    private void extraData() {
        Intent i = getIntent();
        if (i != null) {
            String title = i.getStringExtra("title");
            String note = i.getStringExtra("note");
            int id = i.getIntExtra("id", 0);
            String time = i.getStringExtra("time");
            int color = i.getIntExtra("color", 0);
            saveSelectColor = color;
            this.note.setId(id);
            this.note.setTitle(title);
            this.note.setNote(note);
            this.note.setTime(time);
            this.note.setColor(color);

            txtTitle.setText(title);
            txtNote.setText(note);
            txtTime.setText("Edited " + time);
            txtTime.setTextSize(12);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_delete, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        MyDatabaseHelper myDB = new MyDatabaseHelper(RestoreActivity.this);
        switch (item.getItemId()) {
            case R.id.mnDelete:
                myDB.deleteObjectOfTrash(note);
                Intent it = new Intent(RestoreActivity.this, TrashActivity.class);
                startActivity(it);
                Toast.makeText(RestoreActivity.this, "Deleted!", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
