package com.rbiffi.vacationfriend.AppSections.Spese.Adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rbiffi.vacationfriend.R;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.ResolutionItem;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class ResolutionAdapter
        extends ExpandableRecyclerViewAdapter<ResolutionViewHolder, ResolutionItemViewHolder> {

    private final LayoutInflater inflater;
    private final Context context;

    public ResolutionAdapter(Context context, List<? extends ExpandableGroup> groups) {
        super(groups);

        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public ResolutionViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fragment_expenses_resolution_group, parent, false);
        return new ResolutionViewHolder(view);
    }

    @Override
    public ResolutionItemViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fragment_expenses_resolution_item, parent, false);
        return new ResolutionItemViewHolder(view);
    }

    @Override
    public void onBindGroupViewHolder(ResolutionViewHolder holder, int flatPosition, ExpandableGroup group) {
        final Resolution resolution = (Resolution) group;
        holder.setAction(resolution);
        holder.setGroupValue(resolution);
        holder.setActionDirection(resolution);
        holder.setPersonIcon(resolution);
        holder.setPersonName(resolution);
        holder.setButtonListener(context);

        if (resolution.getAction().equals(context.getString(R.string.resolution_give))) {
            holder.setBackground(ContextCompat.getColor(context, R.color.colorSecondaryLight));
        }
    }

    @Override
    public void onBindChildViewHolder(ResolutionItemViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
        Resolution r = (Resolution) group;
        final ResolutionItem resolutionItem = r.getItems().get(childIndex);
        holder.onBind(resolutionItem, context);

        if (r.getAction().equals(context.getString(R.string.resolution_give))) {
            holder.showNeededAction(ContextCompat.getColor(context, R.color.colorSecondaryLight));
        }
    }
}
