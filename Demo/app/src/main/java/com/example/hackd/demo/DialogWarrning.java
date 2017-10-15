package com.example.hackd.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;


/**
 * Created by hackd on 14-Aug-17.
 */

public class DialogWarrning extends DialogFragment  {
    public interface CallBack{
        void CloseDialog(boolean checkAgain);
    }

    private CallBack callBack;
    private String warrning;

    public DialogWarrning(String warrning) {
        this.warrning = warrning;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_warrning, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final CheckBox checkBox = (CheckBox)view.findViewById(R.id.chkNotShowAgain);

        TextView textView = (TextView) view.findViewById(R.id.tvWarrning);
        textView.setText(warrning);

        Button btn = (Button) view.findViewById(R.id.btnOK);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack = (CallBack)getActivity();
                callBack.CloseDialog(checkBox.isChecked());
            }
        });

    }
}
