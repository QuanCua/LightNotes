package org.sitf_jica.tot.lightnotes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by Asus on 11/24/2017.
 */

public class TrashActivity extends AppCompatActivity {
    ListView lvTrash;
    MyDatabaseHelper db;
    ArrayList<Note> arrayListTrash;
    NoteAdapter adapter;
    ImageButton imgButton;
    Note note = new Note();
    int saveSelect = 0;

    public static final String KEY_SAVE_CHOOSE_SORT = "saveChooseSort";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Trash");
        setContentView(R.layout.activity_trash);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        lvTrash = (ListView) findViewById(R.id.listViewTrash);
        imgButton = (ImageButton) findViewById(R.id.imageButton);

//        if(arrayListTrash.size()==0){
//            imgTrashEmptry.setVisibility(View.VISIBLE);
//        }
//        else {
//            imgTrashEmptry.setVisibility(View.INVISIBLE);
//        }

        arrayListTrash = new ArrayList<Note>();
        db = new MyDatabaseHelper(this);
        arrayListTrash = db.getTrash();

        timeStyle();

        adapter = new NoteAdapter(
                TrashActivity.this,
                R.layout.dong_ghi_chu,
                arrayListTrash
        );
        lvTrash.setAdapter(adapter);

        lvTrash.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(TrashActivity.this, RestoreActivity.class);
                Note note = arrayListTrash.get(i);
                intent.putExtra("id", note.getId());
                intent.putExtra("title", note.getTitle());
                intent.putExtra("note", note.getNote());
                intent.putExtra("color", note.getColor());
                intent.putExtra("time", note.getTime());
                startActivity(intent);
            }
        });

        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(TrashActivity.this);
                myDB.deleteTrash(note);
                Toast.makeText(TrashActivity.this, "Deleted!", Toast.LENGTH_SHORT).show();
                arrayListTrash.clear();
                adapter.notifyDataSetChanged();
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void timeStyle(){
        DateFormat date1 = new SimpleDateFormat("HH:mm:ss");
        DateFormat date2 = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendar = Calendar.getInstance();
        long timeCurrent = System.currentTimeMillis();
        long miliSecondOfOneDay = 86400000;

        for (Note note : arrayListTrash) {
            String timeOfDB = note.getTime();
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            try {
                Date date = format.parse(timeOfDB);
                long millis = date.getTime();

                if ((timeCurrent - millis) < miliSecondOfOneDay) {
                    calendar.setTimeInMillis(millis);
                    System.out.println("CHECK DATE: " + date1.format(calendar.getTimeInMillis()));
                    note.setTime(date1.format(calendar.getTimeInMillis()));
                } else {
                    note.setTime(date2.format(calendar.getTimeInMillis()));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        addTrashItem(menu);
        addSearchItem(menu);
        return true;
    }

    public boolean addTrashItem(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_trash, menu);
        return true;
    }

    public boolean addSearchItem(Menu menu){
        getMenuInflater().inflate(R.menu.search_view, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.mnSearch).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String s) {
                adapter.filter(s.trim());
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
            switch(item.getItemId()){
                case R.id.mnHome:
                    Intent intent = new Intent(TrashActivity.this, MainActivity.class);
                    startActivity(intent);
                    break;
                case R.id.mnSort:
                    final DialogSort dialogSort = new DialogSort(this);
                    dialogSort.initDialog();
                    RadioGroup group = dialogSort.getDialog().findViewById(R.id.radioGroup);

                    SharedPreferences sharedPreferences = getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);
                    int savedRadioIndex = sharedPreferences.getInt(KEY_SAVE_CHOOSE_SORT, 0);

                    RadioButton rb = (RadioButton) group.getChildAt(savedRadioIndex);
                    rb.setChecked(true);

                    group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup radioGroup, int i) {
                            View radioButton = radioGroup.findViewById(i);
                            int index = radioGroup.indexOfChild(radioButton);
                            saveSelect = index;
                            switch (index) {
                                case 0:
                                    Comparator<Note> comparator0 = new Comparator<Note>() {
                                        @Override
                                        public int compare(Note obj1, Note obj2) {
                                            return obj2.getTime().compareToIgnoreCase(obj1.getTime());
                                        }
                                    };
                                    Collections.sort(arrayListTrash, comparator0);

                                    adapter.notifyDataSetChanged();

                                    saveSort(KEY_SAVE_CHOOSE_SORT, 0);

                                    dialogSort.hideDialog();
                                    Toast.makeText(TrashActivity.this, "Recently updated", Toast.LENGTH_SHORT).show();
                                    break;
                                case 1:
                                    db = new MyDatabaseHelper(TrashActivity.this);
                                    arrayListTrash = db.getTrash();

                                    timeStyle();

                                    adapter = new NoteAdapter(
                                            TrashActivity.this,
                                            R.layout.dong_ghi_chu,
                                            arrayListTrash
                                    );
                                    lvTrash.setAdapter(adapter);

                                    adapter.notifyDataSetChanged();

                                    saveSort(KEY_SAVE_CHOOSE_SORT, 1);

                                    dialogSort.hideDialog();
                                    Toast.makeText(TrashActivity.this, "Date created", Toast.LENGTH_SHORT).show();
                                    break;
                                case 2:
                                    Comparator<Note> comparator = new Comparator<Note>() {
                                        @Override
                                        public int compare(Note obj1, Note obj2) {
                                            return obj1.getTitle().compareToIgnoreCase(obj2.getTitle());
                                        }
                                    };
                                    Collections.sort(arrayListTrash, comparator);

                                    adapter.notifyDataSetChanged();

                                    saveSort(KEY_SAVE_CHOOSE_SORT, 2);

                                    dialogSort.hideDialog();
                                    Toast.makeText(TrashActivity.this, "By title", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }
                    });
                    break;
            }
        return super.onOptionsItemSelected(item);
    }

    private void saveSort(String key, int value){
        SharedPreferences sharedPreferences = getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

}
