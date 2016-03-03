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
        holder.subject.setText(items.get(position).getSubject());
        holder.classType.setText(items.get(position).getClassType());
        holder.tutorlecturer.setText(items.get(position).getTutorlecturer());
        holder.location.setText(items.get(position).getLocation());
        holder.startTime.setText(items.get(position).getStartTime());
        holder.endTime.setText(items.get(position).getEndTime());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView subject, classType, tutorlecturer, location, startTime, endTime;

        public ViewHolder(final View itemView) {
            super(itemView);
            subject = (TextView) itemView.findViewById(R.id.text_view_subject);
            classType = (TextView) itemView.findViewById(R.id.text_view_classType);
            tutorlecturer = (TextView) itemView.findViewById(R.id.text_view_tutorlecturer);
            location = (TextView) itemView.findViewById(R.id.text_view_location);
            startTime = (TextView) itemView.findViewById(R.id.text_view_startTime);
            endTime = (TextView) itemView.findViewById(R.id.text_view_endTime);
        }

        @Override
        public void onClick(View view) {


        }
    }

}
