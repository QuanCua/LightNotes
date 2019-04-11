package org.sitf_jica.tot.lightnotes;

import android.app.ActionBar;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.provider.SyncStateContract;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by Asus on 11/3/2017.
 */

public class NoteAdapter extends BaseAdapter {
    Context myContext;
    int myLayout;
    List<Note> arrayNote;
    ArrayList<Note> arrayListNote;
    SharedPreferences mPrefe;
    public static final String MPREF = "notevnshine";
    public static final String COLOR_BAR = "colorbar";
    public static final String KEY_SAVE_COLOR_OF_CIRCLE = "saveColorCircleNote";

    public NoteAdapter(Context context, int layout, List<Note> noteAdapterList){
        myContext = context;
        myLayout = layout;
        arrayNote = noteAdapterList;

        arrayListNote = new ArrayList<Note>();
        arrayListNote.addAll(arrayNote);
    }

    @Override
    public int getCount() {
        return arrayNote.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

        @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater)myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(myLayout, null);

        TextView txtTitle = (TextView) view.findViewById(R.id.textViewTitle);
        txtTitle.setText(arrayNote.get(i).title);
        TextView txtNote = (TextView) view.findViewById(R.id.textViewNote);
        txtNote.setText(arrayNote.get(i).note);
        TextView txtTime = (TextView) view.findViewById(R.id.textViewTime);
        txtTime.setText(arrayNote.get(i).time);

        TextView txtCircle = (TextView) view.findViewById(R.id.textViewCircle);
        if(arrayNote.get(i).getTitle().length() == 1 ){
            txtCircle.setText(arrayNote.get(i).title.substring(0).toUpperCase());
        }else if (arrayNote.get(i).getTitle().length() > 1){
            txtCircle.setText(arrayNote.get(i).title.substring(0,1).toUpperCase());
//        }else if ((arrayNote.get(i).getTitle().length()) == 0){
//            txtCircle.setText("");
        }

        switch(arrayNote.get(i).getColor()){
            case 0:
                txtCircle.setBackgroundResource(R.drawable.circle);
                break;
            case 1:
                txtCircle.setBackgroundResource(R.drawable.circle1);
                break;
            case 2:
                txtCircle.setBackgroundResource(R.drawable.circle2);
                break;
            case 3:
                txtCircle.setBackgroundResource(R.drawable.circle3);
                break;
            case 4:
                txtCircle.setBackgroundResource(R.drawable.circle4);
                break;
            case 5:
                txtCircle.setBackgroundResource(R.drawable.circle5);
                break;
            case 6:
                txtCircle.setBackgroundResource(R.drawable.circle6);
                break;
        }
        return view;
    }



    public void filter(String charText){
        charText = charText.toLowerCase(Locale.getDefault());
        arrayNote.clear();
        if(charText.length() ==0){
            arrayNote.addAll(arrayListNote);
        }else{
            for(Note note : arrayListNote){
                if(note.title.toLowerCase(Locale.getDefault()).contains(charText)) {
                    arrayNote.add(note);
                }
            }
        }
        notifyDataSetChanged();
    }
}
