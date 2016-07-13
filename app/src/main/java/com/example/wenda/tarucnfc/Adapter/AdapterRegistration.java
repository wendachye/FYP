package com.example.wenda.tarucnfc.Adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wenda.tarucnfc.Domains.Registration;
import com.example.wenda.tarucnfc.R;

import java.util.List;

public class AdapterRegistration extends RecyclerView.Adapter<AdapterRegistration.ViewHolder>{

    Context context;
    List<Registration> items;
    int itemLayout;

    public AdapterRegistration(Context context, List<Registration> items, int itemLayout) {
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

        holder.registrationID.setText(items.get(position).getRegistrationID());
        holder.name.setText(items.get(position).getName());
        holder.nric.setText(items.get(position).getNric());
        holder.contact.setText(items.get(position).getContact());
        holder.email.setText(items.get(position).getEmail());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView registrationID, name, nric, contact, email;

        public ViewHolder(final View itemView) {
            super(itemView);
            registrationID = (TextView) itemView.findViewById(R.id.registrationID);
            name = (TextView) itemView.findViewById(R.id.name);
            nric = (TextView) itemView.findViewById(R.id.nric);
            contact = (TextView) itemView.findViewById(R.id.contact);
            email = (TextView) itemView.findViewById(R.id.email);
        }

        @Override
        public void onClick(View view) {


        }
    }

}

