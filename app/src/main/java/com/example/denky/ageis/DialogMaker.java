package com.example.denky.ageis;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ArrayAdapter;

/**
 * Created by WINDOWS7 on 2017-05-31.
 */

public class DialogMaker extends DialogFragment {
    public interface Callback{
        void callbackMethod();
    }

    private String message="";
    private String positiveMsg="";
    private String negativeMsg="";
    private Callback callback_positive=null;
    private Callback callback_negative=null;

    private ArrayAdapter<String> arrayAdapter=null;
    private DialogInterface.OnClickListener listListener=null;

    private View childView=null;

    //Before using this class as instance, must call this method.
    public DialogMaker setValue(String message, String positiveMsg, String negativeMsg, Callback callback_positive, Callback callback_negative){
        this.message=message;
        this.positiveMsg=positiveMsg;
        this.negativeMsg=negativeMsg;
        this.callback_positive=callback_positive;
        this.callback_negative=callback_negative;

        return this;
    }

    public DialogMaker setValue(String message, String positiveMsg, String negativeMsg, Callback callback_positive, Callback callback_negative, View childView){
        this.setValue(message, positiveMsg, negativeMsg, callback_positive, callback_negative);
        this.childView=childView;

        return this;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message)
                .setPositiveButton(positiveMsg, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                })
                .setNegativeButton(negativeMsg, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

        if(arrayAdapter!=null && listListener!=null){
            builder.setAdapter(arrayAdapter, listListener);
        }

        if(childView!=null){
            builder.setView(childView);
        }

        // Create the AlertDialog object and return it
        final AlertDialog dialog=builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface d) {
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        //Prevent to close dialog when positive button is clicked.
                        if(callback_positive!=null){
                            callback_positive.callbackMethod();
                        }
                    }
                });
                dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Prevent to close dialog when negative button is clicked.
                        if(callback_negative!=null){
                            callback_negative.callbackMethod();
                        }
                    }
                });
            }
        });

        return dialog;
    }

}
