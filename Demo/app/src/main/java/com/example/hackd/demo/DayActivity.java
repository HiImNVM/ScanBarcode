package com.example.hackd.demo;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.example.hackd.demo.databinding.ActivityDayBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

/**
 * Created by hackd on 20-Aug-17.
 */

public class DayActivity extends Fragment implements ICallBack, DialogMenu.ICallBack1 {
    private ActivityDayBinding binding;
    private List<Subject> listSubJect;
    private Subject subjectItem;
    private DialogMenu dialogMenu;
    private int day;
    private static final int KEY = 1;
    private List<String> listId;

    public DayActivity(List<Subject> data, int day) {
        this.listSubJect = data;
        this.day = day;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_day, container, false);
        listId = new ArrayList<>();
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Setting Dialog
        dialogMenu = new DialogMenu(this);

        binding.recyviewDay.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        binding.recyviewDay.setAdapter(new RecyclerApdapter(listSubJect, this));

        binding.recyviewDay.setHasFixedSize(true);
        binding.recyviewDay.setItemAnimator(new SlideInUpAnimator());
    }

    @Override
    public void showListStudent(Subject subject) {
        this.subjectItem = subject;
        dialogMenu.show(getFragmentManager(), null);
    }

    @Override
    public void backtoDayActivity(int choose) {
        Intent intent;
        // 1 scan, 2 showList
        if (choose == 1) {
            intent = new Intent(binding.getRoot().getContext(), ActionActivity.class);
            intent.putExtra("day", day);
            intent.putExtra("name", subjectItem.getName());
            intent.putExtra("time", getDate());
            intent.putStringArrayListExtra("listId", (ArrayList<String>) listId);
            startActivityForResult(intent, KEY);
        } else {
            intent = new Intent(binding.getRoot().getContext(), ViewStudentActivity.class);
            int size = subjectItem.getListStudentObject().size();
            intent.putExtra("size", size);
            for (int i=0;i<size;++i){
                StudentObject studentObject = subjectItem.getListStudentObject().get(i);
                intent.putExtra("id" + i, studentObject.getId());
                intent.putExtra("name" + i, studentObject.getName());

                JSONArray jsonArray = studentObject.getArrJSONCheckDay();
                intent.putExtra("jsonarr" + i, jsonArray.toString());
            }
            startActivity(intent);
        }
    }

    public String getDate() {
        Date today = new Date(System.currentTimeMillis());
        SimpleDateFormat timeFormat = new SimpleDateFormat("dd/MM/yyyy");
        return timeFormat.format(today.getTime());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == KEY && resultCode == Activity.RESULT_OK) {

            listId = data.getStringArrayListExtra("listId");
            List<StudentObject> listStudent = subjectItem.getListStudentObject();

            for (int i = 0; i < listStudent.size(); ++i) {
                for (int j = 0; j < listId.size(); ++j) {
                    if (listStudent.get(i).getId().compareTo(listId.get(j)) == 0) {
                        JSONArray JSONarr = listStudent.get(i).getArrJSONCheckDay();
                        JSONObject object = new JSONObject();
                        try {
                            object.put("date", getDate());
                            object.put("value", true);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        JSONarr.put(object);
                        break;
                    }
                }
            }
        }
    }


}
