package com.example.hackd.demo;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hackd.demo.databinding.ActivityStudentBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by hackd on 25-Aug-17.
 */

public class ApdapterStudent extends RecyclerView.Adapter  {
    private List<StudentObject> listStudent;
    private String dateNow;

    public ApdapterStudent(List<StudentObject> listStudent, String dateNow) {
        this.listStudent = listStudent;
        this.dateNow = dateNow;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder viewHolder;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ActivityStudentBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.activity_student, parent, false);
        viewHolder = new MyViewHolder(binding.getRoot());
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        StudentObject object = listStudent.get(position);
        ActivityStudentBinding binding = DataBindingUtil.findBinding(holder.itemView);
        JSONArray JSONarr = object.getArrJSONCheckDay();
        for (int i=0;i<JSONarr.length();++i){
            JSONObject obj = JSONarr.optJSONObject(i);
            String date = null;
            try {
                 date = obj.getString("date");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(date.compareTo(dateNow) == 0){
                holder.itemView.setBackgroundColor(Color.GREEN);
            }
            else{
                holder.itemView.setBackgroundColor(Color.RED);
            }
        }

        binding.tvId.setText(object.getId());
        binding.tvName.setText(object.getName());
    }

    @Override
    public int getItemCount() {
        return listStudent.size();
    }
}
