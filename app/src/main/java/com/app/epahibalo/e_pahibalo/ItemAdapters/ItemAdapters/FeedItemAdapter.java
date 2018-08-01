package com.app.epahibalo.e_pahibalo.ItemAdapters.ItemAdapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.epahibalo.e_pahibalo.Helpers.GlideApp;
import com.app.epahibalo.e_pahibalo.Helpers.URL;
import com.app.epahibalo.e_pahibalo.ItemAdapters.ItemModels.FeedItem;
import com.app.epahibalo.e_pahibalo.R;
import com.bumptech.glide.Glide;

import java.util.List;
import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;



public class FeedItemAdapter extends RecyclerView.Adapter<FeedItemAdapter.ViewHolder> {

    private List<FeedItem> feedItemList;
    Context context;
    public FeedItemAdapter(List<FeedItem> feedItemList, Context context) {
        this.feedItemList = feedItemList;
        this.context = context;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView citizenPic,incidentImage;
        public TextView disaster_id,citizenName, headLine, datePosted,disasterType,location,validated;

        public ViewHolder(View view) {
            super(view);
            disaster_id = (TextView)view.findViewById(R.id.incident_report_id);
            citizenPic = (ImageView)view.findViewById(R.id.citizenProfilePic);
            citizenName = (TextView)view.findViewById(R.id.citizenName);
            headLine = (TextView)view.findViewById(R.id.disasterHeadline);
            datePosted = (TextView)view.findViewById(R.id.disasterDatePosted);
            disasterType = (TextView)view.findViewById(R.id.disasterType);
            location = (TextView)view.findViewById(R.id.disasterLocation);
            incidentImage = (ImageView)view.findViewById(R.id.disasterImage);
            validated = (TextView)view.findViewById(R.id.citizenValidated);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_feed_item,parent,false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FeedItem item = feedItemList.get(position);

        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius( 30f);
        circularProgressDrawable.start();

        if(item.getValidated().length() > 0){


            holder.validated.setTextColor(item.getValidated().equals("validated") ? ContextCompat.getColor(context,R.color.colorValidated) : ContextCompat.getColor(context,R.color.colorUnvalidated));

            holder.validated.setVisibility(View.VISIBLE);
            holder.validated.setText(item.getValidated());
        }else{

            holder.validated.setVisibility(View.GONE);
        }
        holder.disaster_id.setText(item.getDisaster_id());

        GlideApp.with(context)
                .load(item.getCitizenPic())
                .placeholder(R.drawable.placeholder_image)
                .transition(withCrossFade())
                .into(holder.citizenPic);

        holder.citizenName.setText(item.getName());
        holder.headLine.setText(item.getHeadline());
        holder.datePosted.setText(item.getDate_posted());
        holder.disasterType.setText(item.getDisaster_type());
        holder.location.setText(item.getLocation());
        //holder.incidentImage.setImageResource(R.drawable.placeholder_image);




            GlideApp.with(context)
                    .load(item.getIncidentImage())
                    .placeholder(circularProgressDrawable)
                    .error(R.drawable.ic_e_pahibalo_image_not_found)
                    .transition(withCrossFade())
                    .into(holder.incidentImage);
            Log.d("HANDLER: ","AMP "+item.getIncidentImage());



    }

    @Override
    public int getItemCount() {
       return feedItemList.size();
    }
}
