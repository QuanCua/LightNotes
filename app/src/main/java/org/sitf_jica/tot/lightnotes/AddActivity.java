package org.sitf_jica.tot.lightnotes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.Preference;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SoundEffectConstants;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AddActivity extends AppCompatActivity {
    Button btnAdd;
    EditText edtTitle;
    EditText edtNote;
    TextView txtTime;
    RadioGroup group;
    ArrayList<Note> arrayListNote;
    int saveSelectColor = 0;
    Note note = new Note();
    ActionBar bar;

    SharedPreferences mPrefe;
    SharedPreferences.Editor myEditor;
    public static final String COLOR_BAR_ADD = "colorBarOfAdd";
    public static final String KEY_SAVE_CHOOSE_COLOR = "saveChooseColor";
    public static final String KEY_SET_COLOR = "setColor";
    public static final String KEY_SAVE_BUTTON_COLOR = "saveButtonColor";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("");
        setContentView(R.layout.activity_add);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnAdd = (Button)findViewById(R.id.buttonAdd);
        edtTitle = (EditText) findViewById(R.id.editTextTitle);
        edtNote = (EditText) findViewById(R.id.editTextNote);
        txtTime = (TextView) findViewById(R.id.textViewTimeAdd);
        arrayListNote = new ArrayList<Note>();
        bar = getSupportActionBar();

        mPrefe = getBaseContext().getSharedPreferences(MainActivity.MPREF, MODE_PRIVATE);

        String colorBarAdd = mPrefe.getString(MainActivity.COLOR_BAR, "notset");
        if (!colorBarAdd.equalsIgnoreCase("notset")){
            bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(colorBarAdd)));
        }

        Calendar cal = Calendar.getInstance();
        Date currentLocalTime = cal.getTime();

        DateFormat date0 = new SimpleDateFormat("HH:mm");
        String localTimeAdd = date0.format(currentLocalTime);
        txtTime.setText("Edited "+ localTimeAdd);
        txtTime.setTextSize(12);

        extraData();

        DateFormat date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        final String localTime = date.format(currentLocalTime);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                note.setTime(localTime);
                note.setTitle(edtTitle.getText().toString());
                note.setNote(edtNote.getText().toString());
                    note.setColor(saveSelectColor);

                MyDatabaseHelper myDB = new MyDatabaseHelper(AddActivity.this);
                myDB.addOrUpdate(note);

                Intent it = new Intent(AddActivity.this, MainActivity.class);
                startActivity(it);
            }
        });
    }

    private void extraData(){
        Intent i = getIntent();
        if(i != null&& "listView".equalsIgnoreCase(i.getAction())){
            String title = i.getStringExtra("title");
            String noteContent = i.getStringExtra("note");
            int id = i.getIntExtra("id",0);
            String time = i.getStringExtra("time");
            edtTitle.setText(title);
            edtNote.setText(noteContent);
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
                    bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ecacc6")));
                    break;
            }
            note.setColor(saveSelectColor);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Calendar cal = Calendar.getInstance();
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        final String localTime = date.format(currentLocalTime);

        MyDatabaseHelper myDB = new MyDatabaseHelper(AddActivity.this);
        switch (item.getItemId()){
            case R.id.mnDelete:
                note.setTime(localTime);
                note.setTitle(edtTitle.getText().toString());
                note.setNote(edtNote.getText().toString());
                note.setColor(saveSelectColor);

                myDB.addIntoTrash(note);
                myDB.delete(note);
                myDB.deleteArchive(note);
                Intent it = new Intent(AddActivity.this, MainActivity.class);
                startActivity(it);
                Toast.makeText(AddActivity.this, "Deleted!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.mnArchive:
                note.setTime(localTime);
                note.setTitle(edtTitle.getText().toString());
                note.setNote(edtNote.getText().toString());
                note.setColor(saveSelectColor);

                myDB.addIntoArchive(note);
                myDB.delete(note);
                Intent it2 = new Intent(AddActivity.this, MainActivity.class);
                startActivity(it2);
                Toast.makeText(AddActivity.this, "Note archive!", Toast.LENGTH_SHORT).show();
                break;

            case R.id.mnColor:
                final DialogColor dialogColor = new DialogColor(AddActivity.this);
                dialogColor.initDialog();
                RadioGroup group = dialogColor.getDialog().findViewById(R.id.radioGroupColor);
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


