package com.example.simpletodo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.TextView;

import java.util.List;

// adapter = display data into each row of recycler view
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    public interface OnClickListener {
        void OnItemClicked(int position);
    }

    public interface OnLongClickListener {
        void onItemLongClicked(int position);
    }
    List<String> items;
    OnLongClickListener longClickListener;
    OnClickListener clickListener;
    public ItemAdapter(List<String> items, OnLongClickListener longClickListener, OnClickListener clickListener) {
        this.items = items;
        this.longClickListener = longClickListener;
        this.clickListener = clickListener;
    }

    @NonNull

    // creates each view
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Use layout inflater + wrap view inside ViewHolder
        View todoView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);

        return new ViewHolder(todoView);    // return ViewHolder
    }

    // binds data to specific viewholder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // 1: find item's position
        String item = items.get(position);

        // 2: bind item to specific viewholder
        holder.bind(item);
    }

    // number of items in the data
    @Override
    public int getItemCount() {
        return items.size();
    }

    // viewholder = container to easily access views in each list
    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(android.R.id.text1);  // text1 is from simple list item 1 android id
        }

        // Update view inside viewholder of specific data
        public void bind(String item) {
            tvItem.setText(item);

            // account for single click of item in list
            tvItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Notify listener the position of pressed item
                    clickListener.OnItemClicked(getAdapterPosition());
                }
            });

            // account for long hold of item in list
            tvItem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    // Notify listener the position of long-pressed item
                    longClickListener.onItemLongClicked(getAdapterPosition());
                    return true;
                }
            });
        }
    }
}
