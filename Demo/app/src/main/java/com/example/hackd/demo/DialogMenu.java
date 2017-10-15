package com.example.hackd.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by hackd on 22-Aug-17.
 */

public class DialogMenu extends DialogFragment {
    private Button btnScan, btnShowListStudent;
    private ICallBack1 callBack;

    public DialogMenu(ICallBack1 callBack) {
        this.callBack = callBack;
    }

    public interface ICallBack1{
        public void backtoDayActivity(int choose);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_menu, container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        btnScan = (Button)view.findViewById(R.id.btnScan);

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.backtoDayActivity(1);
            }
        });

        btnShowListStudent = (Button)view.findViewById(R.id.btnShowListStudent);
        btnShowListStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBack.backtoDayActivity(2);
            }
        });
    }
}
