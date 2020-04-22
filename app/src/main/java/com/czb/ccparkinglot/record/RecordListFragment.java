package com.czb.ccparkinglot.record;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.czb.ccparkinglot.R;

import java.util.ArrayList;

import java.util.List;

public class RecordListFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater
                .inflate(R.layout.record_list_fragment, container, false);

        RecyclerView recordRecyclerView = view.findViewById(
                R.id.record_list_fragment_recyclerView_recordItem);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(
                getActivity());
        recordRecyclerView.setLayoutManager(linearLayoutManager);
        RecordAdapter recordAdapter = new RecordAdapter(init_recordItemList());
        recordRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recordRecyclerView.setAdapter(recordAdapter);
        return view;
    }

    private List<RecordItem> init_recordItemList() {
        List<RecordItem> recordList = new ArrayList<>();
        RecordItem recordItem;
        for (int i = 0; i < 50; i++) {
            recordItem = new RecordItem("兰博基尼"+i, "迪拜停车场", "停车中",
                                                   new Date(2019, 12, 5, 10, 18),
                                                   null);
            recordList.add(recordItem);
        }
        return recordList;
    }

    class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder> {

        private List<RecordItem> recordList;

        public RecordAdapter(List<RecordItem> recordList) {
            this.recordList = recordList;
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            TextView carName;
            TextView parkinglotName;
            TextView status;

            public ViewHolder(@NonNull View view) {
                super(view);
                carName = view.findViewById(R.id.record_item_textview_car_name);
                parkinglotName = view.findViewById(
                        R.id.record_item_textview_parkinglot_name);
                status = view.findViewById(R.id.record_item_textview_status);
            }
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                             int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.record_item, parent, false);
            final ViewHolder holder = new ViewHolder(view);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RecordItem recordItem = recordList.get(holder.getAdapterPosition());
                    RecordContentActivity.actionStart(getActivity(), recordItem);
                }
            });

            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            RecordItem recordItem = recordList.get(position);
            holder.carName.setText(recordItem.getCarName());
            holder.parkinglotName.setText(recordItem.getParkinglotName());
            holder.status.setText(recordItem.getStatus());
        }


        @Override
        public int getItemCount() {
            return recordList.size();
        }
    }
}
