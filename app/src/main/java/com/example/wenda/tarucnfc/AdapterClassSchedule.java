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
        holder.group.setText(items.get(position).getGroup());
        holder.subject.setText(items.get(position).getSubject());
        holder.tutorlecturer.setText(items.get(position).getTutorlecturer());
        holder.location.setText(items.get(position).getLocation());
        holder.date.setText(items.get(position).getDate());
        holder.time.setText(items.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView faculty, programme, group, subject, tutorlecturer, location, date, time;

        public ViewHolder(final View itemView) {
            super(itemView);
            faculty = (TextView) itemView.findViewById(R.id.faculty);
            programme = (TextView) itemView.findViewById(R.id.programme);
            group = (TextView) itemView.findViewById(R.id.group);
            subject = (TextView) itemView.findViewById(R.id.subject);
            tutorlecturer = (TextView) itemView.findViewById(R.id.tutorlecturer);
            location = (TextView) itemView.findViewById(R.id.location);
            date = (TextView) itemView.findViewById(R.id.date);
            time = (TextView) itemView.findViewById(R.id.time);
        }

        @Override
        public void onClick(View view) {


        }
    }

}
