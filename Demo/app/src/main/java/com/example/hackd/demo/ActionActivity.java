package com.example.hackd.demo;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.example.hackd.demo.databinding.ActivityActionBinding;
import com.google.zxing.Result;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by hackd on 24-Aug-17.
 */

public class ActionActivity extends AppCompatActivity {
    private Intent intent;
    private ZXingScannerView scannerView;
    private List<String> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action);
        ActivityActionBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_action);
        intent = getIntent();
        list = intent.getStringArrayListExtra("listId");
    }

    @Override
    protected void onStart() {
        super.onStart();
        scannerView = new ZXingScannerView(ActionActivity.this);
        scannerView.setAutoFocus(true);
        scannerView.setFlash(true);
        scannerView.setResultHandler(new RecevieData());
        setContentView(scannerView);
        scannerView.startCamera();
    }

    @Override
    public void onBackPressed() {
        scannerView.stopCamera();
        Intent resultCallBack = new Intent();
        resultCallBack.putStringArrayListExtra("listId", (ArrayList<String>) list);
        setResult(RESULT_OK, resultCallBack);
        finish();
    }



    private class RecevieData implements ZXingScannerView.ResultHandler {

        @Override
        public void handleResult(Result result) {
            String idStudent = result.getText();
            final JSONObject JSONobj = new JSONObject();
            try {
                JSONobj.put("day", intent.getIntExtra("day", 0));
                JSONobj.put("name", intent.getStringExtra("name"));
                JSONobj.put("id", idStudent);
                JSONobj.put("date", intent.getStringExtra("time"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (SocketClient.create(ActionActivity.this).connectServer()) {
                        SocketClient.create(ActionActivity.this).sendData("updateData", JSONobj.toString());
                    }
                }
            }).start();

            // add id to list
            list.add(idStudent);
            try {
                Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                Ringtone r = RingtoneManager.getRingtone(ActionActivity.this, notification);

                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

                v.vibrate(500);
                r.play();
            } catch (Exception e) {
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            scannerView.resumeCameraPreview(this);
        }
    }
}
