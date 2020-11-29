package com.cookandroid.dietmanager;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import androidx.fragment.app.DialogFragment;

import java.io.FileOutputStream;

public class NPDialog extends DialogFragment {
    public String strs[] = {"23","60"};

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View dialog = inflater.inflate(R.layout.dialog, null);

        builder.setTitle("사용자 정보 수정");
        builder.setIcon(R.drawable.perm_group_personal_info);

        final NumberPicker npAge = (NumberPicker) dialog.findViewById(R.id.npAge);
        final NumberPicker npWeight = (NumberPicker) dialog.findViewById(R.id.npWeight);

        npAge.setMinValue(1);
        npAge.setMaxValue(100);
        npAge.setValue(Integer.parseInt(strs[0]));

        npWeight.setMinValue(30);
        npWeight.setMaxValue(120);
        npWeight.setValue(Integer.parseInt(strs[1]));

        builder.setView(dialog);
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
//                    FileOutputStream outFs = openFileOutput("user_info.txt", Context.MODE_PRIVATE);
//                    String str = npAge.getValue() + " " + npWeight.getValue();
//                    outFs.write(str.getBytes());
//                    outFs.close();
                } catch (Exception e){
                }
            }
        });
        builder.setNegativeButton("취소",null);

        return builder.create();
    }
}