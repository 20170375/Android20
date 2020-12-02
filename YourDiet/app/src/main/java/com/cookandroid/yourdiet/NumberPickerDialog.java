package com.cookandroid.yourdiet;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import androidx.fragment.app.DialogFragment;

public class NumberPickerDialog extends DialogFragment {
    NumberPicker npAge, npWeight;

    public String strs[] = {"",};

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View dialog = inflater.inflate(R.layout.dialog, null);

        builder.setTitle("사용자 정보 수정");
        builder.setIcon(R.drawable.perm_group_personal_info);

        npAge = (NumberPicker) dialog.findViewById(R.id.npAge);
        npWeight = (NumberPicker) dialog.findViewById(R.id.npWeight);

        npAge.setMinValue(1);
        npAge.setMaxValue(100);
        npAge.setValue(Integer.parseInt(strs[0]));
        npAge.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        npWeight.setMinValue(30);
        npWeight.setMaxValue(120);
        npWeight.setValue(Integer.parseInt(strs[1]));
        npWeight.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setNegativeButton("취소",null);

        builder.setView(dialog);
        return builder.create();
    }
}