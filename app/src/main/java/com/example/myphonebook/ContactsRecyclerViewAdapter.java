package com.example.myphonebook;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ContactsRecyclerViewAdapter extends RecyclerView.Adapter<ContactsRecyclerViewAdapter.ViewHolder> implements ContactListReceiver{
    private List<PhoneBookEntry> mData;
    private final LayoutInflater mInflater;
    private final Context ctx;

    ContactsRecyclerViewAdapter(Context context) {
        ctx=context;
        this.mInflater = LayoutInflater.from(ctx);
        this.mData = null;
    }
    private Intent createDetailsActivityIntent(PhoneBookEntry entry) {
        Intent intent = new Intent(ctx, DetailsActivity.class);
        intent.putExtra("CONTACT_ENTRY", entry);
        return intent;
    }

    public void startDetailsActivity(int pos){
        Intent intent = createDetailsActivityIntent(mData.get(pos));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        ctx.startActivity(intent);
    }


    public void setData(List<PhoneBookEntry> data){
        mData=data;
        this.notifyDataSetChanged();
    }
    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.contact_entry_layout, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(mData!=null) {
            String name = mData.get(position).name();
            holder.Name.setText(name);
            holder.itemView.setOnClickListener(view -> startDetailsActivity(position));
        }
    }

    // total number of rows
    @Override
    public int getItemCount() {
        if(mData==null) return 0;
        return mData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView Name;

        ViewHolder(View itemView) {
            super(itemView);
            Name = itemView.findViewById(R.id.Name);
        }
    }

}
