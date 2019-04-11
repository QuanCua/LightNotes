package org.sitf_jica.tot.lightnotes;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.security.Permission;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

/**
 * Created by Asus on 10/27/2017.
 */

public class SettingActivity extends AppCompatActivity {
    ActionBar bar;
    int saveSelectColor = 0;
    TextView txtCycle;
    RelativeLayout btnColor;
    RelativeLayout btnSort;
    RadioGroup group;
    RelativeLayout rlBackup;
    RelativeLayout rlRestore;

    SharedPreferences mPrefe;
    SharedPreferences.Editor myEditor;

    public String KEY_SAVE_CHOOSE_RADIO_BUTTON = "saveChooseRadioButton";
    public String KEY_SAVE_CHOOSE_SORT = "saveChooseSort";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        setTitle("Settings");
        setContentView(R.layout.activity_setting);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rlBackup = (RelativeLayout) findViewById(R.id.layoutBackup);
        rlRestore = (RelativeLayout) findViewById(R.id.layoutRestore);
        group = (RadioGroup) findViewById(R.id.radioGroupColor);
        txtCycle = (TextView) findViewById(R.id.textViewCircle);
        btnColor = (RelativeLayout) findViewById(R.id.layoutColor);
        btnSort = (RelativeLayout) findViewById(R.id.layoutSort);

        bar = getSupportActionBar();

        mPrefe = getBaseContext().getSharedPreferences(MainActivity.MPREF, MODE_PRIVATE);
        myEditor = mPrefe.edit();

        String colorBarSetting = mPrefe.getString(MainActivity.COLOR_BAR, "notset");
        if (!colorBarSetting.equalsIgnoreCase("notset")) {
            int savedRadioIndex = mPrefe.getInt(KEY_SAVE_CHOOSE_RADIO_BUTTON, 10);
            if (savedRadioIndex != 10) {
                loadcircleview(savedRadioIndex, colorBarSetting);
            }
            bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor(colorBarSetting)));
        }

        btnColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogColor dialogColor = new DialogColor(SettingActivity.this);
                dialogColor.initDialog();
                group = dialogColor.getDialog().findViewById(R.id.radioGroupColor);

                RadioButton rb = (RadioButton) group.getChildAt(saveSelectColor);
                rb.setChecked(true);
                loadChooseColor();

                group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        View radioButton = radioGroup.findViewById(i);
                        int index = radioGroup.indexOfChild(radioButton);
                        saveSelectColor = index;

                        switch (index) {
                            case 0:


                                bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#26c668")));
                                txtCycle.setBackgroundResource(R.drawable.circle);
                                //Color of Add Button
                                myEditor.putInt(MainActivity.KEY_SAVE_COLOR_OF_BUTTON, 0);

                                //ActionBar of Setting layout
                                myEditor.putString(MainActivity.COLOR_BAR, "#26c668");
                                myEditor.commit();

                                //ActionBar of Archive layout
                                saveColorArchive(ArchiveActivity.KEY_COLOR_OF_ARCHIVE, "#26c668");

                                //ActionBar of Add layout
                                saveColorAdd(AddActivity.COLOR_BAR_ADD, "#26c668");

                                //save choose radio button color
                                saveColorButtonAdd(AddActivity.KEY_SAVE_CHOOSE_COLOR, 0);

                                //save choose radio button
                                savePreferences(KEY_SAVE_CHOOSE_RADIO_BUTTON, 0);
                                dialogColor.hideDialog();
                                break;
                            case 1:
                                saveChooseColorAdd(AddActivity.KEY_SET_COLOR, 1);
                                bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#d76060")));
                                txtCycle.setBackgroundResource(R.drawable.circle1);

                                myEditor.putInt(MainActivity.KEY_SAVE_COLOR_OF_BUTTON, 1);

                                myEditor.putString(MainActivity.COLOR_BAR, "#d76060");
                                myEditor.commit();

                                saveColorArchive(ArchiveActivity.KEY_COLOR_OF_ARCHIVE, "#d76060");
                                saveColorAdd(AddActivity.COLOR_BAR_ADD, "#d76060");
                                saveColorButtonAdd(AddActivity.KEY_SAVE_CHOOSE_COLOR, 1);
                                savePreferences(KEY_SAVE_CHOOSE_RADIO_BUTTON, 1);

                                dialogColor.hideDialog();
                                break;
                            case 2:
                                bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#5ac9da")));
                                txtCycle.setBackgroundResource(R.drawable.circle2);

                                myEditor.putInt(MainActivity.KEY_SAVE_COLOR_OF_BUTTON, 2);

                                myEditor.putString(MainActivity.COLOR_BAR, "#5ac9da");
                                myEditor.commit();

                                saveColorArchive(ArchiveActivity.KEY_COLOR_OF_ARCHIVE, "#5ac9da");
                                saveColorAdd(AddActivity.COLOR_BAR_ADD, "#5ac9da");
                                saveColorButtonAdd(AddActivity.KEY_SAVE_CHOOSE_COLOR, 2);
                                savePreferences(KEY_SAVE_CHOOSE_RADIO_BUTTON, 2);

                                dialogColor.hideDialog();
                                break;
                            case 3:
                                bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#cb5ae7")));
                                txtCycle.setBackgroundResource(R.drawable.circle3);

                                myEditor.putInt(MainActivity.KEY_SAVE_COLOR_OF_BUTTON, 3);

                                myEditor.putString(MainActivity.COLOR_BAR, "#cb5ae7");
                                myEditor.commit();

                                saveColorArchive(ArchiveActivity.KEY_COLOR_OF_ARCHIVE, "#cb5ae7");
                                saveColorAdd(AddActivity.COLOR_BAR_ADD, "#cb5ae7");
                                saveColorButtonAdd(AddActivity.KEY_SAVE_CHOOSE_COLOR, 3);
                                savePreferences(KEY_SAVE_CHOOSE_RADIO_BUTTON, 3);

                                dialogColor.hideDialog();
                                break;
                            case 4:
                                bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#454343")));
                                txtCycle.setBackgroundResource(R.drawable.circle4);

                                myEditor.putInt(MainActivity.KEY_SAVE_COLOR_OF_BUTTON, 4);

                                myEditor.putString(MainActivity.COLOR_BAR, "#454343");
                                myEditor.commit();

                                saveColorArchive(ArchiveActivity.KEY_COLOR_OF_ARCHIVE, "#454343");
                                saveColorAdd(AddActivity.COLOR_BAR_ADD, "#454343");
                                saveColorButtonAdd(AddActivity.KEY_SAVE_CHOOSE_COLOR, 4);
                                savePreferences(KEY_SAVE_CHOOSE_RADIO_BUTTON, 4);

                                dialogColor.hideDialog();
                                break;
                            case 5:
                                bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ff9422")));
                                txtCycle.setBackgroundResource(R.drawable.circle5);

                                myEditor.putInt(MainActivity.KEY_SAVE_COLOR_OF_BUTTON, 5);

                                myEditor.putString(MainActivity.COLOR_BAR, "#ff9422");
                                myEditor.commit();

                                saveColorArchive(ArchiveActivity.KEY_COLOR_OF_ARCHIVE, "#ff9422");
                                saveColorAdd(AddActivity.COLOR_BAR_ADD, "#ff9422");
                                saveColorButtonAdd(AddActivity.KEY_SAVE_CHOOSE_COLOR, 5);
                                savePreferences(KEY_SAVE_CHOOSE_RADIO_BUTTON, 5);

                                dialogColor.hideDialog();
                                break;
                            case 6:
                                bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ecacc6")));
                                txtCycle.setBackgroundResource(R.drawable.circle6);

                                myEditor.putInt(MainActivity.KEY_SAVE_COLOR_OF_BUTTON, 6);

                                myEditor.putString(MainActivity.COLOR_BAR, "#ecacc6");
                                myEditor.commit();

                                saveColorArchive(ArchiveActivity.KEY_COLOR_OF_ARCHIVE, "#ecacc6");
                                saveColorAdd(AddActivity.COLOR_BAR_ADD, "#ecacc6");
                                saveColorButtonAdd(AddActivity.KEY_SAVE_CHOOSE_COLOR, 6);
                                savePreferences(KEY_SAVE_CHOOSE_RADIO_BUTTON, 6);

                                dialogColor.hideDialog();
                                break;
                        }
                    }
                });
            }
        });

        btnSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogSort dialogSort = new DialogSort(SettingActivity.this);
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
                        saveSelectColor = index;

                        switch (index){
                            case 0:
                                saveSort(KEY_SAVE_CHOOSE_SORT, 0);
                                saveSort(MainActivity.KEY_SAVE_CHOOSE_SORT, 0);
                                saveSort(ArchiveActivity.KEY_SAVE_CHOOSE_SORT, 0);
                                saveSort(TrashActivity.KEY_SAVE_CHOOSE_SORT, 0);
                                dialogSort.hideDialog();
                                Toast.makeText(SettingActivity.this, "Recently updated", Toast.LENGTH_SHORT).show();
                                break;
                            case 1:
                                saveSort(KEY_SAVE_CHOOSE_SORT, 1);
                                saveSort(MainActivity.KEY_SAVE_CHOOSE_SORT, 1);
                                saveSort(ArchiveActivity.KEY_SAVE_CHOOSE_SORT, 1);
                                saveSort(TrashActivity.KEY_SAVE_CHOOSE_SORT, 1);
                                dialogSort.hideDialog();
                                Toast.makeText(SettingActivity.this, "Date created", Toast.LENGTH_SHORT).show();
                                break;
                            case 2:
                                saveSort(KEY_SAVE_CHOOSE_SORT, 2);
                                saveSort(MainActivity.KEY_SAVE_CHOOSE_SORT, 2);
                                saveSort(ArchiveActivity.KEY_SAVE_CHOOSE_SORT, 2);
                                saveSort(TrashActivity.KEY_SAVE_CHOOSE_SORT, 2);
                                dialogSort.hideDialog();
                                Toast.makeText(SettingActivity.this, "By title", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
            }
        });

        rlBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backUp();
            }
        });

        rlRestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restore();

            }
        });
    }


    public void backUp() {
        tedPermissonal();
        try {
            File sd = Environment.getExternalStorageDirectory();
            String curentDBPath = "lightNotes.db";
            String backupDBPath = "backUp.db";

            File currentDB = getApplicationContext().getDatabasePath(curentDBPath);
            File backupDB = new File(sd, backupDBPath);

            Log.d("backupDB path", "" + currentDB.getAbsolutePath());

            if (currentDB.exists()) {
                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                Toast.makeText(SettingActivity.this, "Backup is successful to SD card", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(SettingActivity.this, "not exists", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void restore() {
        try {
            File sd = Environment.getExternalStorageDirectory();
            String currentDBPath = "lightNotes.db";
            String backupDBPath = "backUp.db";

            File currentDB = getApplicationContext().getDatabasePath(currentDBPath);
            File backupDB = new File(sd, backupDBPath);

            if (currentDB.exists()) {
                FileChannel src = new FileInputStream(backupDB).getChannel();
                FileChannel dst = new FileOutputStream(currentDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                Toast.makeText(SettingActivity.this, "Restore is successful! ", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(SettingActivity.this, "not exists", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveColorArchive(String key, String value) {
        myEditor.putString(key, value);
        myEditor.commit();
    }

    private void saveColorButtonAdd(String key, int value){
        SharedPreferences sharedPreferences = getSharedPreferences("MY_SHARED_PREF_COLOR", MODE_PRIVATE);
        SharedPreferences.Editor mEditor = sharedPreferences.edit();
        mEditor.putInt(key, value);
        mEditor.commit();
    }

    private void saveColorAdd(String key, String value) {
        myEditor.putString(key, value);
        myEditor.commit();
    }

    private void savePreferences(String key, int value) {
        myEditor.putInt(key, value);
        myEditor.commit();
    }

    private void saveSort(String key, int value){
        SharedPreferences sharedPreferences = getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    private void loadChooseColor() {
        int savedRadioIndex = mPrefe.getInt(MainActivity.KEY_SAVE_COLOR_OF_BUTTON, 10);
        if (savedRadioIndex != 10) {
            RadioButton savedCheckedRadioButton = (RadioButton) group.getChildAt(savedRadioIndex);
            savedCheckedRadioButton.setChecked(true);
        }
    }

    private void tedPermissonal() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
//                Toast.makeText(SettingActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
//                Toast.makeText(SettingActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .check();
    }

    private void saveChooseColorAdd(String key, int value){
        SharedPreferences sharedPreferences = getSharedPreferences(AddActivity.KEY_SET_COLOR, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SettingActivity.this,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        super.onBackPressed();
    }

    private void loadcircleview(int savedRadioIndex, String colorBarSetting) {
        switch (savedRadioIndex) {
            case 0:
                txtCycle.setBackgroundResource(R.drawable.circle);
                break;
            case 1:
                txtCycle.setBackgroundResource(R.drawable.circle1);
                break;
            case 2:
                txtCycle.setBackgroundResource(R.drawable.circle2);
                break;
            case 3:
                txtCycle.setBackgroundResource(R.drawable.circle3);
                break;
            case 4:
                txtCycle.setBackgroundResource(R.drawable.circle4);
                break;
            case 5:
                txtCycle.setBackgroundResource(R.drawable.circle5);
                break;
            case 6:
                txtCycle.setBackgroundResource(R.drawable.circle6);
                break;
            default:
                break;
        }
    }
}
