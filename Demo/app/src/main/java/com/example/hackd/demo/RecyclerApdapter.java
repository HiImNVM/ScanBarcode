package com.example.hackd.demo;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.hackd.demo.databinding.ActivityHeaderBinding;
import com.example.hackd.demo.databinding.ActivityItemBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hackd on 21-Aug-17.
 */

public class RecyclerApdapter extends RecyclerView.Adapter {
    private List<Subject> listSubJect;
    private boolean isMorning = false;
    private boolean isAfternoon = false;
    private boolean isEvening = false;
    private ICallBack callBack;

    public RecyclerApdapter(List<Subject> listSubject, ICallBack callBack) {
        this.callBack = callBack;
        this.listSubJect = new ArrayList<>();

        for (int i = 0; i < listSubject.size(); ++i) {
            if ("1,2,3,4,5".contains(listSubject.get(i).getNumbers())) {
                if (!isMorning) {
                    listSubJect.add(null);
                    isMorning = true;
                }
            } else if ("11,12,13,14,15".contains(listSubject.get(i).getNumbers())) {
                if (!isEvening) {
                    listSubJect.add(null);
                    isEvening = true;
                }
            } else {
                if (!isAfternoon) {
                    listSubJect.add(null);
                    isAfternoon = true;
                }

            }
            listSubJect.add(listSubject.get(i));
        }
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (listSubJect.get(position) == null) {
            return 0;
        }
        return 1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        MyViewHolder viewHolder;

        if (viewType == 0) {
            ActivityHeaderBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.activity_header, parent, false);
            viewHolder = new MyViewHolder(binding.getRoot());
        } else {
            ActivityItemBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.activity_item, parent, false);
            viewHolder = new MyViewHolder(binding.getRoot());
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        Subject subject = listSubJect.get(position);
        if (subject == null) {
            ActivityHeaderBinding binding = DataBindingUtil.findBinding(holder.itemView);
            if (isMorning) {
                binding.tvNameDay.setText("Sáng");
                isMorning = false;
            } else if (isAfternoon) {
                binding.tvNameDay.setText("Chiều");
                isAfternoon = false;
            } else {
                binding.tvNameDay.setText("Tối");
            }
        } else {
            final ActivityItemBinding binding = DataBindingUtil.findBinding(holder.itemView);
            binding.tvTenMon.setText(subject.getName());
            binding.tvRoom.setText(subject.getName_room());
            binding.tvThoiGian.setText(subject.getTimebegin());
            binding.tvSoTiet.setText(subject.getNumbers());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callBack.showListStudent(listSubJect.get(position));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return listSubJect.size();
    }
}
