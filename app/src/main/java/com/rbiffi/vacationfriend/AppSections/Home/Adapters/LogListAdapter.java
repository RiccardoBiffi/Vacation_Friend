package com.rbiffi.vacationfriend.AppSections.Home.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rbiffi.vacationfriend.R;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.ActivityLog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class LogListAdapter extends RecyclerView.Adapter<LogListAdapter.LogViewHolder> {

    private static final int VIEW_TYPE_OBJECT_VIEW = 0;
    private static final int VIEW_TYPE_HEADER = 1;

    private static final int HEADERS_NUM = 1;

    private final LayoutInflater inflater;
    private final Context context;
    private final List<ActivityLog> activityLogs;

    public LogListAdapter(Context context, List<ActivityLog> discussions) {
        super();
        inflater = LayoutInflater.from(context);

        this.context = context;
        this.activityLogs = discussions;
    }

    @NonNull
    @Override
    public LogViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case VIEW_TYPE_HEADER:
                view = inflater.inflate(R.layout.lists_section_divider, parent, false);
                break;
            case VIEW_TYPE_OBJECT_VIEW:
                view = inflater.inflate(R.layout.fragment_home_activitylog_row, parent, false);
                break;
            default:
                view = inflater.inflate(R.layout.vacationlist_list_row, parent, false);
                break;
        }
        return new LogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LogViewHolder holder, int position) {
        if (position == 0) {
            holder.sectionHeaderView.setText(R.string.activitylog_header_today);
        } else {
            ActivityLog current = activityLogs.get(position - HEADERS_NUM);

            holder.actorPhotoView.setImageURI(current.participantPhoto);
            holder.logTextView.setText(current.logText);
            holder.logDateTimeView.setText(showTimeOfDay(current.logDateTime));
        }
    }

    private CharSequence showTimeOfDay(Date logDateTime) {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return dateFormat.format(logDateTime);
    }

    @Override
    public int getItemCount() {
        return activityLogs.size() + HEADERS_NUM; // +1 per header
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return VIEW_TYPE_HEADER;
        return VIEW_TYPE_OBJECT_VIEW;
    }

    public class LogViewHolder extends RecyclerView.ViewHolder {

        private final CircleImageView actorPhotoView;
        private final TextView logTextView;
        private final TextView logDateTimeView;
        private final TextView sectionHeaderView;

        LogViewHolder(View itemView) {
            super(itemView);

            actorPhotoView = itemView.findViewById(R.id.actor_picture);
            logTextView = itemView.findViewById(R.id.activity_log_text);
            logDateTimeView = itemView.findViewById(R.id.activity_log_datetime);
            sectionHeaderView = itemView.findViewById(R.id.list_section_header);
        }
    }
}