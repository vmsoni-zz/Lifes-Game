package lifesgame.tapstudios.ca.lifesgame.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import lifesgame.tapstudios.ca.lifesgame.R;
import lifesgame.tapstudios.ca.lifesgame.adapter.TaskSubItemAdapter;
import lifesgame.tapstudios.ca.lifesgame.modelV2.TaskSublistItem;

/**
 * Created by viditsoni on 2019-05-05.
 */

public class TaskSubItemHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.remove_sublist_item_btn) ImageButton removeSubListItem;
    @BindView(R.id.subitem_text) TextView text;

    private TaskSubItemAdapter subItemAdapter;

    public TaskSubItemHolder(View itemView, TaskSubItemAdapter subItemAdapter) {
        super(itemView);
        this.subItemAdapter = subItemAdapter;
        ButterKnife.bind(this,  itemView);
    }

    public void bindSubItem(TaskSublistItem sublistItem, int position) {
        text.setText(sublistItem.getTitle());
        this.initializeOnClickListeners(position);
    }

    private void initializeOnClickListeners(final int position) {
        text.requestFocus();
        removeSubListItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subItemAdapter.removeItem(position);
            }
        });
    }

}
