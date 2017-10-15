package com.example.hackd.demo;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import com.example.hackd.demo.databinding.ActivityViewstudentBinding;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hackd on 26-Aug-17.
 */

public class ViewStudentActivity extends AppCompatActivity{
    private ActivityViewstudentBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewstudent);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_viewstudent);
        List<StudentObject> listStudentObject = new ArrayList<>();

        Intent intent = getIntent();
        int size = intent.getIntExtra("size", 0);
        if (size != 0) {
            for (int i = 0; i < size; ++i) {
                StudentObject studentObject = new StudentObject();
                studentObject.setId(intent.getStringExtra("id" + i));
                studentObject.setName(intent.getStringExtra("name" + i));
                try {
                    studentObject.setArrJSONCheckDay(new JSONArray(intent.getStringExtra("jsonarr" + i)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                listStudentObject.add(studentObject);
            }
        }

        // fill data
        binding.recyclerView.setAdapter(new RecyclerViewStudent(listStudentObject));
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
    }
}
