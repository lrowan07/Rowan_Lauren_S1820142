package org.me.gcu.rowan_lauren_s1820142;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.me.gcu.rowan_lauren_s1820142.Models.FeedItem;

import java.util.ArrayList;

//Lauren Rowan S1820142

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    ArrayList<FeedItem> feedItems;

    public ListAdapter(ArrayList<FeedItem> feedItems){
        this.feedItems = feedItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context c = parent.getContext();
        LayoutInflater li = LayoutInflater.from(c);

        View feedView = li.inflate(R.layout.feed_item, parent, false);

        ViewHolder vh = new ViewHolder(feedView);

        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FeedItem clickedItem = feedItems.get(vh.getAdapterPosition());

                Bundle bundle = new Bundle();
                bundle.putString("title", clickedItem.getTitle());
                bundle.putString("description", clickedItem.getDescription());
                String startDate = clickedItem.getStartDate() != null ? clickedItem.getStartDate().toString() : "N/A";
                bundle.putString("startDate", startDate);
                String endDate = clickedItem.getEndDate() != null ? clickedItem.getEndDate().toString() : "N/A";
                bundle.putString("endDate", endDate);
                bundle.putString("latLng", clickedItem.getLatitude() + " / " + clickedItem.getLongitude());
                bundle.putString("pubDate", clickedItem.getPublishDate().toString());

                Intent intent = new Intent(c, InspectFeedItem.class).putExtras(bundle);
                c.startActivity(intent);
            }
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(ListAdapter.ViewHolder holder, int position) {
        FeedItem fi = feedItems.get(position);

        TextView title = holder.title;
        title.setText(fi.getTitle());

        TextView description = holder.desc;
        description.setText(fi.getDescription());

        TextView startDateLabel = holder.startDateLabel;
        TextView endDateLabel = holder.endDateLabel;
        TextView status = holder.status;


        if(!fi.getType().equals("INCIDENT")){
            startDateLabel.setText("Start Date:");

            TextView startDate = holder.startDate;
            startDate.setText(fi.getStartDate().toString());

            endDateLabel.setText("End Date:");

            TextView endDate = holder.endDate;
            endDate.setText(fi.getEndDate().toString());

            //Get the duration in days between the dates
            int days = 0;
            long diff = fi.getEndDate().getTime() - fi.getStartDate().getTime();
            long diffDays = diff / (24 * 60 * 60 * 1000);
            days = (int) diffDays;

            if(days < 31){
                //Green, less than 31 days
                status.setTextColor(Color.parseColor("#0ec22c"));
            } else if(days >= 31 && days <= 61) {
                //Amber, between 31 and 61 days
                status.setTextColor(Color.parseColor("#ffa600"));

            } else if(days > 61) {
                //Red, greater than 61 days
                status.setTextColor(Color.parseColor("#bf0000"));

            }

            status.setText("These roadworks will last a total of " + days + " days");
        } else {
            startDateLabel.setText("");
            endDateLabel.setText("");
            status.setText("");
        }


    }

    @Override
    public int getItemCount() {
        return feedItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView description;
        public TextView startDate;
        public TextView endDate;
        public TextView desc;
        public TextView status;
        public TextView startDateLabel;
        public TextView endDateLabel;

        public ViewHolder(View view){
            super(view);

            title = (TextView) view.findViewById(R.id.title);
            description = (TextView) view.findViewById(R.id.description);
            startDate = (TextView) view.findViewById(R.id.startDate);
            endDate = (TextView) view.findViewById(R.id.endDate);
            desc = (TextView) view.findViewById(R.id.description);
            status = (TextView) view.findViewById(R.id.status);
            startDateLabel = (TextView) view.findViewById(R.id.startDateLabel);
            endDateLabel = (TextView) view.findViewById(R.id.endDateLabel);

        }

    }
}
