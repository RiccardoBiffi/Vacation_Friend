package com.rbiffi.vacationfriend.Utils;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class FieldListAdapter extends RecyclerView.Adapter<FieldListAdapter.FieldViewHolder> {

    // todo imposta come classe base di EditFieldListAdapter e ReadFieldListAdapter
    // per esempio metti qua le costanti dei campi ed alcuni metodi comuni
    // escluso il onBindViewHolder, per esempio

    public class FieldViewHolder extends RecyclerView.ViewHolder {
        public FieldViewHolder(View itemView) {
            super(itemView);
        }
    }
}
