package com.app.epahibalo.e_pahibalo.Sections;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.app.epahibalo.e_pahibalo.ItemAdapters.ItemAdapters.FeedItemAdapter;
import com.app.epahibalo.e_pahibalo.ItemAdapters.ItemModels.FeedItem;
import com.app.epahibalo.e_pahibalo.R;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class NewsFeedFragment extends Fragment {

    private FeedItemAdapter feedItemAdapter;
    private List<FeedItem> feedItemsList = new ArrayList<>();
    RecyclerView recyclerNewsFeed;
    public static Handler handler;
    ProgressDialog dialog;

    @SuppressLint("HandlerLeak")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_news_feed,container,false);
        recyclerNewsFeed = view.findViewById(R.id.recyclerNewsFeed);
        feedItemAdapter = new FeedItemAdapter(feedItemsList,getContext());
        dialog = new ProgressDialog(getContext());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerNewsFeed.setLayoutManager(layoutManager);
        recyclerNewsFeed.setItemAnimator(new DefaultItemAnimator());
        recyclerNewsFeed.setAdapter(feedItemAdapter);

        dialog.setMessage("Loading incidents");
        dialog.show();

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {

                if(msg.obj instanceof FeedItem){
                    FeedItem item = (FeedItem) msg.obj;

                    feedItemsList.add(item);
                    feedItemAdapter.notifyDataSetChanged();
                    dialog.dismiss();
                }
            }
        };
        return view;
    }

}
