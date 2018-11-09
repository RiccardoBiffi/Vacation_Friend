package com.rbiffi.vacationfriend.AppSections.Home;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rbiffi.vacationfriend.AppSections.Home.Adapters.ChatListAdapter;
import com.rbiffi.vacationfriend.AppSections.Home.ViewModels.VacationViewModel;
import com.rbiffi.vacationfriend.R;
import com.rbiffi.vacationfriend.Repository.Entities_POJOs.Discussion;
import com.rbiffi.vacationfriend.Utils.NoFooterDividerItemDecoration;

import java.util.List;

public class FragmentHomeChatList extends Fragment {

    private VacationViewModel viewModel;

    private RecyclerView chatList;
    private ChatListAdapter chatAdapter;
    private RecyclerView.LayoutManager chatLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = ViewModelProviders.of(getActivity()).get(VacationViewModel.class);
        return inflater.inflate(R.layout.fragment_home_chat, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupListWithAdapter(view);
    }

    private void setupListWithAdapter(final View view) {
        chatList = view.findViewById(R.id.chatList);
        chatList.addItemDecoration(new NoFooterDividerItemDecoration(ContextCompat.getDrawable(getContext(), R.drawable.simple_list_divider)));
        chatLayout = new LinearLayoutManager(getContext());
        chatList.setLayoutManager(chatLayout);

        viewModel.getDiscussions().observe(this, new Observer<List<Discussion>>() {
            @Override
            public void onChanged(@Nullable List<Discussion> discussions) {
                if (discussions == null) {
                    view.findViewById(R.id.empty_home_chat).setVisibility(View.VISIBLE);
                } else {
                    // passa la lista all'adapter per la visualizzazione
                    chatAdapter = new ChatListAdapter(getContext(), discussions);
                    chatList.setAdapter(chatAdapter);
                }
            }
        });

    }
}
