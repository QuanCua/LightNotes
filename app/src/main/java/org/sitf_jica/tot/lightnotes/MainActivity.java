package org.sitf_jica.tot.lightnotes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.support.v7.widget.SearchView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;


public class MainActivity extends AppCompatActivity {
    TextView textHelp;
    Button btnAdd;
    ListView lvNote;
    ArrayList<Note> arrayListNote;
    MyDatabaseHelper db;
    NoteAdapter adapter;
    RadioGroup group;
    int saveSelect = 0;

    SharedPreferences mPrefe;
    public static final String MPREF = "notevnshine";
    public static final String COLOR_BAR = "colorbar";
    public static final String KEY_SAVE_COLOR_OF_BUTTON = "saveColorAddButton";
    public static final String KEY_SAVE_CHOOSE_SORT = "saveChooseSort";

    ActionBar bar;
    int pos=0;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textHelp = (TextView) findViewById(R.id.text_help);
        lvNote = (ListView) findViewById(R.id.listViewNote);
        btnAdd = (Button) findViewById(R.id.button_Add);
        arrayListNote = new ArrayList<Note>();
        db = new MyDatabaseHelper(this);
        arrayListNote = db.getAllNote();
        bar = getSupportActionBar();

        if (arrayListNote.size() == 0) {
            textHelp.setVisibility(View.VISIBLE);
        } else {
            textHelp.setVisibility(View.INVISIBLE);
        }

        mPrefe = getBaseContext().getSharedPreferences(MPREF, MODE_PRIVATE);
        String color = mPrefe.getString(COLOR_BAR, "notset");
        if (!color.equalsIgnoreCase("notset")) {
            bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(color)));
            int savedColorButton = mPrefe.getInt(KEY_SAVE_COLOR_OF_BUTTON, 10);
            if (savedColorButton != 10) {
                loadColorButton(savedColorButton, color);
            }
        }

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });

        timeStyle();

        adapter = new NoteAdapter(
                MainActivity.this,
                R.layout.dong_ghi_chu,
                arrayListNote
        );
        lvNote.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        //sorting by new created
        Comparator<Note> comparator0 = new Comparator<Note>() {
            @Override
            public int compare(Note obj1, Note obj2) {
                return obj2.getTime().compareToIgnoreCase(obj1.getTime());
            }
        };
        Collections.sort(arrayListNote, comparator0);


        lvNote.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                Note note = arrayListNote.get(i);
                intent.putExtra("id", note.getId());
                intent.putExtra("title", note.getTitle());
                intent.putExtra("note", note.getNote());
                intent.putExtra("color", note.getColor());
                intent.putExtra("time", note.getTime());
                intent.setAction("listView");
                startActivity(intent);
            }
        });

        lvNote.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
               if(i-pos>0){
                   btnAdd.setVisibility(View.INVISIBLE);
               }
               else {
                   btnAdd.setVisibility(View.VISIBLE);
                   pos=i;
               }

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

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

        for (Note note : arrayListNote) {
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
    public boolean onCreateOptionsMenu(Menu menu) {
        addSortItem(menu);
        addMenuItem(menu);
        addSearchItem(menu);
        return true;
    }

    public boolean addMenuItem(Menu menu) {
        menu.add(0, 1, 1, "Archive");
        menu.add(1, 2, 2, "Trash");
        menu.add(2, 3, 3, "Settings");
        return true;
    }

    public boolean addSortItem(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_sort, menu);
        return true;
    }

    public boolean addSearchItem(Menu menu) {
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

    public void loadColorButton(int savedColorButton, String color) {
        switch (savedColorButton) {
            case 0:
                btnAdd.setBackgroundResource(R.drawable.circle);
                break;
            case 1:
                btnAdd.setBackgroundResource(R.drawable.circle1);
                break;
            case 2:
                btnAdd.setBackgroundResource(R.drawable.circle2);
                break;
            case 3:
                btnAdd.setBackgroundResource(R.drawable.circle3);
                break;
            case 4:
                btnAdd.setBackgroundResource(R.drawable.circle4);
                break;
            case 5:
                btnAdd.setBackgroundResource(R.drawable.circle5);
                break;
            case 6:
                btnAdd.setBackgroundResource(R.drawable.circle6);
                break;
            default:
                break;
        }
    }

    public boolean onOptionsItemSelected(final MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.mnSort:
                final DialogSort dialogSort = new DialogSort(this);
                dialogSort.initDialog();
                group = dialogSort.getDialog().findViewById(R.id.radioGroup);

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
                                Collections.sort(arrayListNote, comparator0);

                                adapter.notifyDataSetChanged();

                                saveSort(KEY_SAVE_CHOOSE_SORT, 0);

                                dialogSort.hideDialog();
                                Toast.makeText(MainActivity.this, "Recently updated", Toast.LENGTH_SHORT).show();
                                break;
                            case 1:
                                db = new MyDatabaseHelper(MainActivity.this);
                                arrayListNote = db.getAllNote();

                                timeStyle();

                                adapter = new NoteAdapter(
                                        MainActivity.this,
                                        R.layout.dong_ghi_chu,
                                        arrayListNote
                                );
                                lvNote.setAdapter(adapter);

                                adapter.notifyDataSetChanged();

                                saveSort(KEY_SAVE_CHOOSE_SORT, 1);

                                dialogSort.hideDialog();

                                Toast.makeText(MainActivity.this, "Date created", Toast.LENGTH_SHORT).show();
                                break;
                            case 2:
                                Comparator<Note> comparator2 = new Comparator<Note>() {
                                    @Override
                                    public int compare(Note obj1, Note obj2) {
                                        return obj1.getTitle().compareToIgnoreCase(obj2.getTitle());
                                    }
                                };
                                Collections.sort(arrayListNote, comparator2);

                                adapter.notifyDataSetChanged();

                                saveSort(KEY_SAVE_CHOOSE_SORT, 2);

                                dialogSort.hideDialog();
                                Toast.makeText(MainActivity.this, "By title", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
                break;
            case 3:
                Intent intent3 = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent3);
                break;
            case 2:
                Intent intent2 = new Intent(MainActivity.this, TrashActivity.class);
                startActivity(intent2);
                break;
            case 1:
                Intent intent1 = new Intent(MainActivity.this, ArchiveActivity.class);
                startActivity(intent1);
                break;
        }
        return true;
    }

    private void saveSort(String key, int value){
        SharedPreferences sharedPreferences = getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }
}
