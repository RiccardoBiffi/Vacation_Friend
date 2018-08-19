package com.rbiffi.vacationfriend.Repository;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rbiffi.vacationfriend.VacationList.IClickEvents;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class FieldListAdapter extends RecyclerView.Adapter<FieldListAdapter.FieldViewHolder> {

    //TODO lista di interi per tutti i possibili fieldList
    private static final int VIEW_TYPE_TITLE = 0;
    private static final int VIEW_TYPE_PERIOD = 1;
    private static final int VIEW_TYPE_PLACE = 2;
    private static final int VIEW_TYPE_PARTECIPANTS = 3;
    private static final int VIEW_TYPE_PHOTO = 4;
    private static final int VIEW_TYPE_NOTES = 5;
    //...

    private final LayoutInflater inflater;
    private IClickEvents listener;
    private List<String> fieldList;

    public FieldListAdapter(Context applicationContext, IUserEditableObject editableObj) {
        inflater = LayoutInflater.from(applicationContext);
        fieldList = editableObj.getEditableFields();
    }

    @Override
    public FieldViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(FieldViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return fieldList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    class FieldViewHolder extends RecyclerView.ViewHolder {

        public FieldViewHolder(View itemView) {
            super(itemView);
        }
    }
}
