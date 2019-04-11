package org.sitf_jica.tot.lightnotes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.Window;

/**
 * Created by Asus on 11/29/2017.
 */

public class DialogSort {
    private Dialog dialog;
    private Context context;


    public DialogSort(Context context){
        this.context = context;
    }


    public void initDialog(){
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_sort);
        dialog.show();
    }
    public void hideDialog(){
        dialog.dismiss();
    }

    public Dialog getDialog() {
        return dialog;
    }
}


