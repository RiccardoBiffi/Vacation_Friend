package com.rbiffi.vacationfriend.AppSections.Home.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rbiffi.vacationfriend.AppSections.VacationList.Events.IVacationListClickEvents;
import com.rbiffi.vacationfriend.R;

public class SummaryListAdapter extends RecyclerView.Adapter<SummaryListAdapter.SummaryViewHolder> {

    private static final int VIEW_TYPE_OBJECT_VIEW = 1;

    private final LayoutInflater inflater;
    private Context context;
    private IVacationListClickEvents listener;

    public SummaryListAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public SummaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case VIEW_TYPE_OBJECT_VIEW:
                view = inflater.inflate(R.layout.fragment_home_summary_row, parent, false);
                break;
            default:
                view = inflater.inflate(R.layout.fragment_home_summary_row, parent, false);
                break;
        }
        return new SummaryListAdapter.SummaryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SummaryViewHolder holder, int position) {
        holder.testTextView.setText("Elemento n°" + position);
    }

    @Override
    public int getItemCount() {
        return 20;
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_OBJECT_VIEW;
    }

    class SummaryViewHolder extends RecyclerView.ViewHolder {

        // todo elementi di ogni view del sommario
        /*
        private final CardView vacationListRowEl;
        private final TextView vacationTitleView;
        private final ImageView vacationImageView;
        private final ImageButton vacationOverflow;
        */

        private final TextView testTextView;

        SummaryViewHolder(View itemView) {
            super(itemView);
            /*
            vacationListRowEl = itemView.findViewById(R.id.card_view);
            vacationTitleView = itemView.findViewById(R.id.vacation_title);
            vacationImageView = itemView.findViewById(R.id.vacation_image);
            vacationOverflow = itemView.findViewById(R.id.vacation_oveflow_menu);
            */
            testTextView = itemView.findViewById(R.id.home_summary_row);
        }
    }
}
