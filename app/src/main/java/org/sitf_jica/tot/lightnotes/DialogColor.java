package org.sitf_jica.tot.lightnotes;

import android.app.Dialog;
import android.content.Context;

/**
 * Created by Asus on 1/11/2018.
 */

public class DialogColor {
    private Dialog dialog;
    private Context context;


    public DialogColor(Context context){
        this.context = context;
    }


    public void initDialog(){
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_color);
        dialog.show();
    }
    public void hideDialog(){
        dialog.dismiss();
    }

    public Dialog getDialog() {
        return dialog;
    }
}
