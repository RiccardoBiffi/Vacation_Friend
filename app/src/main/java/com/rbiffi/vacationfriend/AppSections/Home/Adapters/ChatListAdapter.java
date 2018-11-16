package com.rbiffi.vacationfriend.AppSections.Home.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Discussion;

import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatViewHolder> {


    private final Context context;
    private final List<Discussion> chat;

    public ChatListAdapter(Context context, List<Discussion> discussions) {
        super();
        this.context = context;
        this.chat = discussions;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatViewHolder(parent);
    }

    @Override
    public void onBindViewHolder(ChatViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {
        public ChatViewHolder(View itemView) {
            super(itemView);
        }
    }
}
