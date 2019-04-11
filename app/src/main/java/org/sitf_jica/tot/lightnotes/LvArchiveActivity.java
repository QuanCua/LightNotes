package org.sitf_jica.tot.lightnotes;

import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class LvArchiveActivity extends AppCompatActivity {
    Button btnAddArchive;
    EditText edtTitleArchive;
    EditText edtNoteArchive;
    TextView txtTimeArchive;

    ArrayList<Note> arrayListNoteArchive;
    int saveSelectColor = 0;
    Note note = new Note();
    ActionBar bar;

    SharedPreferences mPrefe;
    SharedPreferences.Editor myEditor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("");
        setContentView(R.layout.activity_lvarchive);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnAddArchive = (Button)findViewById(R.id.buttonAddArchive);
        edtTitleArchive = (EditText) findViewById(R.id.editTextTitleArchive);
        edtNoteArchive = (EditText) findViewById(R.id.editTextNoteArchive);
        txtTimeArchive = (TextView) findViewById(R.id.textViewTimeArchive);
        arrayListNoteArchive = new ArrayList<Note>();
        bar = getSupportActionBar();

        Calendar cal = Calendar.getInstance();
        Date currentLocalTime = cal.getTime();
        DateFormat date0 = new SimpleDateFormat("HH:mm");
        String localTimeAdd = date0.format(currentLocalTime);
        txtTimeArchive.setText("Edited "+ localTimeAdd);
        txtTimeArchive.setTextSize(12);

        extraData();

        DateFormat date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        final String localTime = date.format(currentLocalTime);


        btnAddArchive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                note.setTime(localTime);
                note.setTitle(edtTitleArchive.getText().toString());
                note.setNote(edtNoteArchive.getText().toString());
                note.setColor(saveSelectColor);

                MyDatabaseHelper myDB = new MyDatabaseHelper(LvArchiveActivity.this);
                myDB.updateArchive(note);

                Intent it = new Intent(LvArchiveActivity.this, ArchiveActivity.class);
                startActivity(it);
            }
        });


    }

    private void extraData(){
        Intent i = getIntent();
        if(i != null){
            String title = i.getStringExtra("title");
            String noteContent = i.getStringExtra("note");
            int id = i.getIntExtra("id",0);
            String time = i.getStringExtra("time");
            edtTitleArchive.setText(title);
            edtNoteArchive.setText(noteContent);
            note.setId(id);
            note.setTitle(title);
            note.setNote(noteContent);
            note.setTime(time);

            saveSelectColor= i.getIntExtra("color", 0);
            switch(saveSelectColor){
                case 0:
                    bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#26c668")));
                    break;
                case 1:
                    bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#d76060")));
                    break;
                case 2:
                    bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#5ac9da")));
                    break;
                case 3:
                    bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#cb5ae7")));
                    break;
                case 4:
                    bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#454343")));
                    break;
                case 5:
                    bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ff9422")));
                    break;
                case 6:
                    bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#56da8d")));
                    break;
            }
            note.setColor(saveSelectColor);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_addarchive, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        MyDatabaseHelper myDB = new MyDatabaseHelper(LvArchiveActivity.this);
        switch (item.getItemId()){
            case R.id.mnDelete:
                myDB.deleteArchive(note);
                Intent it = new Intent(LvArchiveActivity.this, ArchiveActivity.class);
                startActivity(it);
                Toast.makeText(LvArchiveActivity.this, "Deleted!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.mnArchive:
                myDB.addOrUpdate(note);
                myDB.deleteArchive(note);
                Intent it2 = new Intent(LvArchiveActivity.this, MainActivity.class);
                startActivity(it2);
                Toast.makeText(LvArchiveActivity.this, "Note archive!", Toast.LENGTH_SHORT).show();
                break;

            case R.id.mnColor:
                final DialogColor dialogColor = new DialogColor(LvArchiveActivity.this);
                dialogColor.initDialog();
                RadioGroup group = dialogColor.getDialog().findViewById(R.id.radioGroupColor);
                RadioButton rb = (RadioButton) group.getChildAt(saveSelectColor);
                rb.setChecked(true);

                group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        View radioButton = radioGroup.findViewById(i);
                        int index = radioGroup.indexOfChild(radioButton);
                        saveSelectColor = index;
                        switch(index){
                            case 0:
                                dialogColor.hideDialog();
                                bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#26c668")));
                                break;
                            case 1:
                                dialogColor.hideDialog();
                                bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#d76060")));
                                break;
                            case 2:
                                dialogColor.hideDialog();
                                bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#5ac9da")));
                                break;
                            case 3:
                                dialogColor.hideDialog();
                                bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#cb5ae7")));
                                break;
                            case 4:
                                dialogColor.hideDialog();
                                bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#454343")));
                                break;
                            case 5:
                                dialogColor.hideDialog();
                                bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ff9422")));
                                break;
                            case 6:
                                dialogColor.hideDialog();
                                bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ecacc6")));
                                break;
                        }
                    }
                });
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
