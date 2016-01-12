package com.example.wenda.tarucnfc.Adapters;

/**
 * Created by Wenda on 1/10/2016.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wenda.tarucnfc.Domain.Login;

import java.util.List;

/**
 * Created by cw fei on 1/4/2016.
 */

public class LoginAdapter extends RecyclerView.Adapter<LoginAdapter.ViewHolder> {

    Context context;
    List<Login> items;
    int itemLayout;

    public LoginAdapter(Context context, List<Login> items, int itemLayout) {
        this.context = context;
        this.items = items;
        this.itemLayout = itemLayout;
        // init();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        return new ViewHolder(view);    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.loginId.setText(items.get(position).getLoginId()+"");
        holder.accountId.setText(items.get(position).getAccountId());
        holder.password.setText(items.get(position).getPassword());
        holder.previousPassword.setText(items.get(position).getPreviousPassword());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView loginId, accountId, password, previousPassword;

        public ViewHolder(final View itemView) {
            super(itemView);
            //loginId = (TextView) itemView.findViewById(R.id.);
            //accountId = (TextView) itemView.findViewById(R.id.text_accountId);
            //password = (TextView) itemView.findViewById(R.id.);
            //previousPassword = (TextView) itemView.findViewById(R.id.text_previous_password);

        }

        @Override
        public void onClick(View view) {


        }
    }
}