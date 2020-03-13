package com.czb.ccparkinglot.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.czb.ccparkinglot.R;
import com.czb.ccparkinglot.RecordItem;

import java.util.List;

public class RecordsAdapter extends RecyclerView.Adapter<RecordsAdapter.ViewHolder> {
    private List<RecordItem> recordItemList;

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView record_item_whichcar;
        TextView record_item_whichparkinglot;
        TextView record_item_state;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            record_item_whichcar = itemView.findViewById(R.id.record_item_whichcar);
            record_item_whichparkinglot = itemView.findViewById(R.id.record_item_whichparkinglot);
            record_item_state = itemView.findViewById(R.id.park_state);
        }
    }

    public RecordsAdapter(List<RecordItem> recordItemList) {
        this.recordItemList = recordItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.record_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecordsAdapter.ViewHolder holder, int position) {
        RecordItem recordItem = recordItemList.get(position);
        holder.record_item_whichcar.setText(recordItem.getWhichcar());
        holder.record_item_whichparkinglot.setText(recordItem.getWhichparkinglot());
        holder.record_item_state.setText(recordItem.getWhatstatus());
    }

    @Override
    public int getItemCount() {
        return recordItemList.size();
    }
}
