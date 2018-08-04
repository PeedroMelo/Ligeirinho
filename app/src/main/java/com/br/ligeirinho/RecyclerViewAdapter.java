package com.br.ligeirinho;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by PC on 15/05/2018.
 */

class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private ArrayList<String> values;
    RecyclerViewAdapter(ArrayList<String> val) {
        this.values = val;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.request_row,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        holder.detalhe.setText(values.get(position));

    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView detalhe;
        ViewHolder(View itemView){
            super(itemView);

            detalhe = (TextView) itemView.findViewById(R.id.fieldDetalhe);

        }
    }
}
