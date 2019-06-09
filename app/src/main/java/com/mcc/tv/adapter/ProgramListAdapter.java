package com.mcc.tv.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mcc.tv.listeners.ItemClickListener;
import com.mcc.tv.model.Program;
import com.mcc.tv.R;
import com.mcc.tv.utility.TimeUtilities;

import java.util.List;

public class ProgramListAdapter extends RecyclerView.Adapter<ProgramListAdapter.MyViewHolder> {

    private List<Program> programList;
    private Context contex;
    private ItemClickListener itemClickListener;

    public ProgramListAdapter(List<Program> programList, Context context) {
        this.contex = context;
        this.programList = programList;
    }

    public void setItemClickListener (ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_programs_list, parent, false);

        return new MyViewHolder(itemView);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView title, time, date;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            date = (TextView) view.findViewById(R.id.date);
            time = (TextView) view.findViewById(R.id.time);
        }

        @Override
        public void onClick(View view) {

        }
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Program program = programList.get(position);

        holder.title.setText(program.getName());

        TimeUtilities timeUtilities = new TimeUtilities(program.getTime());

        holder.date.setText(timeUtilities.getDateOnly());
        holder.time.setText(timeUtilities.getTimeOnly());

    }

    @Override
    public int getItemCount() {
        return programList.size();
    }
}