package com.example.wenda.tarucnfc.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wenda.tarucnfc.Domains.BusSchedule;
import com.example.wenda.tarucnfc.R;

import java.util.List;

public class AdapterBusRoute extends RecyclerView.Adapter<AdapterBusRoute.ViewHolder>{

    Context context;
    List<BusSchedule> items;
    int itemLayout;

    public AdapterBusRoute(Context context, List<BusSchedule> items, int itemLayout) {
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
        holder.departure.setText(items.get(position).getDeparture());
        holder.destination.setText(items.get(position).getDestination());
        holder.routeTime.setText(items.get(position).getRouteTime());
        holder.routeDay.setText(items.get(position).getRouteDay());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView departure, destination, routeTime, routeDay;

        public ViewHolder(final View itemView) {
            super(itemView);
            departure = (TextView) itemView.findViewById(R.id.text_departure);
            destination = (TextView) itemView.findViewById(R.id.text_destination);
            routeTime = (TextView) itemView.findViewById(R.id.text_route_time);
            routeDay = (TextView) itemView.findViewById(R.id.text_route_date);
        }

        @Override
        public void onClick(View view) {


        }
    }
}

