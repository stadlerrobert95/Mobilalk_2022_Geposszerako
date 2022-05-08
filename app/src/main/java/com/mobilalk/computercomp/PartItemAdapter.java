package com.mobilalk.computercomp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mobilalk.computercomp.model.Part;

import java.util.ArrayList;

public class PartItemAdapter extends RecyclerView.Adapter<PartItemAdapter.ViewHolder> implements Filterable {
    private ArrayList<Part> mPartItems;
    private ArrayList<Part> mPartItemsAll;
    private Context mContext;
    private int lastPosition = -1;

    public PartItemAdapter(Context context, ArrayList<Part> itemsData) {
        this.mPartItems = itemsData;
        this.mPartItemsAll = itemsData;
        this.mContext = context;

    }

    @Override
    public PartItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.list_items, parent, false));
    }

    @Override
    public void onBindViewHolder( PartItemAdapter.ViewHolder holder, int position) {
        Part currentItem = mPartItems.get(position);

        holder.bindTo(currentItem);

        if(holder.getAdapterPosition() > lastPosition){
            Animation animation = AnimationUtils.loadAnimation(mContext,R.anim.slide_in_row);
            holder.itemView.startAnimation(animation);
            lastPosition = holder.getAdapterPosition();
        }
    }

    @Override
    public int getItemCount() {
        return mPartItems.size();
    }

    @Override
    public Filter getFilter() {
        return partFilter;
    }

    private Filter partFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<Part> filteredList = new ArrayList<>();
            FilterResults results = new FilterResults();

            if(charSequence == null || charSequence.length() == 0){
                results.count = mPartItemsAll.size();
                results.values = mPartItemsAll;
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for(Part item : mPartItemsAll ){
                    if(item.getName().toLowerCase().contains(filterPattern) || item.getDescription().toLowerCase().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }
                results.count = filteredList.size();
                results.values = filteredList;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mPartItems = (ArrayList) filterResults.values;
            notifyDataSetChanged();
        }
    };

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView mTitleText, mDescription, mWebLink;

        ViewHolder( View itemView) {
            super(itemView);

            mTitleText = itemView.findViewById(R.id.itemTitle);
            mDescription = itemView.findViewById(R.id.itemDescription);
            mWebLink = itemView.findViewById(R.id.itemWebLink);

            itemView.findViewById(R.id.addToComp).setOnClickListener(view -> Log.d("Activity", "Add to comp"));
        }

        public void bindTo(Part currentItem) {
            mTitleText.setText(currentItem.getName());
            mDescription.setText(currentItem.getDescription());
            mWebLink.setText(currentItem.getWebpage());

        }
    }
}

