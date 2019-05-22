package lifesgame.tapstudios.ca.lifesgame.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import lifesgame.tapstudios.ca.lifesgame.helper.DatabaseHelper;
import lifesgame.tapstudios.ca.lifesgame.holder.TaskSubItemHolder;
import lifesgame.tapstudios.ca.lifesgame.modelV2.TaskSublistItem;

/**
 * Created by viditsoni on 2017-12-28.
 */

public class TaskSubItemAdapter extends RecyclerView.Adapter<TaskSubItemHolder> {
    public List<TaskSublistItem> sublistItemList;
    public LayoutInflater inflater;
    public DatabaseHelper databaseHelper;
    private int resource;
    private Context context;

    public TaskSubItemAdapter(Context context,
                              int resource,
                              DatabaseHelper databaseHelper,
                              List<TaskSublistItem> sublistItemList) {
        this.context = context;
        this.resource = resource;
        this.databaseHelper = databaseHelper;
        this.sublistItemList = sublistItemList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public TaskSubItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        return new TaskSubItemHolder(view, this);
    }


    @Override
    public void onBindViewHolder(TaskSubItemHolder holder, int position) {
        TaskSublistItem sublistItem = this.sublistItemList.get(position);
        holder.bindSubItem(sublistItem, position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return sublistItemList.size();
    }

    public void removeItem(int position) {
        sublistItemList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, sublistItemList.size());
    }

}
