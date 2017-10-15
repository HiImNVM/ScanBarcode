package com.example.hackd.demo;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hackd.demo.databinding.ActivityItemstBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by hackd on 26-Aug-17.
 */

public class RecyclerViewStudent extends RecyclerView.Adapter {
    private List<StudentObject> listStudentObject;

    public RecyclerViewStudent(List<StudentObject> listStudentObject) {
        this.listStudentObject = listStudentObject;
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        MyViewHolder viewHolder = null;
        ActivityItemstBinding binding = DataBindingUtil.inflate(inflater, R.layout.activity_itemst, parent, false);
        viewHolder = new MyViewHolder(binding.getRoot());
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        ActivityItemstBinding binding = DataBindingUtil.findBinding(holder.itemView);
//        StudentObject studentObject = null;
//        JSONObject jsonObject = null;
//        String date = "";
//
//        for (int i = 0; i < listStudentObject.size(); ++i) {
//            studentObject = listStudentObject.get(i);
//            binding.tvID.setText(studentObject.getId());
//            binding.tvName.setText(studentObject.getName());
//
//            JSONArray JSONarr = studentObject.getArrJSONCheckDay();
//            for (int j = 0; j < JSONarr.length(); ++j) {
//                jsonObject = JSONarr.optJSONObject(j);
//                try {
//                    date = jsonObject.getString("date");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                if (date.compareTo(getDate()) == 0) {
//                    holder.itemView.setBackgroundColor(Color.GREEN);
//                } else {
//                    holder.itemView.setBackgroundColor(Color.RED);
//                }
//            }
//        }


        StudentObject studentObject = listStudentObject.get(position);
        ActivityItemstBinding binding = DataBindingUtil.findBinding(holder.itemView);

        binding.tvID.setText(studentObject.getId());
        binding.tvName.setText(studentObject.getName());
        JSONArray JSONarr = studentObject.getArrJSONCheckDay();
        for (int i = 0; i < JSONarr.length(); ++i) {
            JSONObject jsonObject = JSONarr.optJSONObject(i);
            String date = "";
            try {
                date = jsonObject.getString("date");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (date.compareTo(getDate()) == 0) {
                holder.itemView.setBackgroundColor(Color.GREEN);
            } else {
                holder.itemView.setBackgroundColor(Color.RED);
            }
        }

    }

    public String getDate() {
        Date today = new Date(System.currentTimeMillis());
        SimpleDateFormat timeFormat = new SimpleDateFormat("dd/MM/yyyy");
        return timeFormat.format(today.getTime());
    }

    @Override
    public int getItemCount() {
        return listStudentObject.size();
    }
}
