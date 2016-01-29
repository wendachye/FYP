package com.example.wenda.tarucnfc;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wenda.tarucnfc.Domains.ClassSchedule;

import java.util.List;

public class AdapterClassSchedule extends RecyclerView.Adapter<AdapterClassSchedule.ViewHolder>{

    Context context;
    List<ClassSchedule> items;
    int itemLayout;

    public AdapterClassSchedule(Context context, List<ClassSchedule> items, int itemLayout) {
        this.context = context;
        this.items = items;
        this.itemLayout = itemLayout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.faculty.setText(items.get(position).getFaculty());
        holder.programme.setText(items.get(position).getProgramme());
        holder.groupNo.setText(items.get(position).getGroupNo());
        holder.subject.setText(items.get(position).getSubject());
        holder.tutorlecturer.setText(items.get(position).getTutorlecturer());
        holder.location.setText(items.get(position).getLocation());
        holder.day.setText(items.get(position).getDay());
        holder.startTime.setText(items.get(position).getStartTime());
        holder.endTime.setText(items.get(position).getEndTime());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView faculty, programme, groupNo, subject, tutorlecturer, location, day, startTime, endTime;

        public ViewHolder(final View itemView) {
            super(itemView);
            faculty = (TextView) itemView.findViewById(R.id.faculty);
            programme = (TextView) itemView.findViewById(R.id.programme);
            groupNo = (TextView) itemView.findViewById(R.id.groupNo);
            subject = (TextView) itemView.findViewById(R.id.subject);
            tutorlecturer = (TextView) itemView.findViewById(R.id.tutorlecturer);
            location = (TextView) itemView.findViewById(R.id.location);
            day = (TextView) itemView.findViewById(R.id.day);
            startTime = (TextView) itemView.findViewById(R.id.startTime);
            endTime = (TextView) itemView.findViewById(R.id.endTime);
        }

        @Override
        public void onClick(View view) {


        }
    }

}
