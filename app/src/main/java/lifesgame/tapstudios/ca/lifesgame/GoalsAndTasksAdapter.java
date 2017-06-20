package lifesgame.tapstudios.ca.lifesgame;

import android.content.ContentValues;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vidit Soni on 5/24/2017.
 */
public class GoalsAndTasksAdapter extends BaseSwipeAdapter {
    public List<String> goalsAndTasks;
    public LayoutInflater inflater;
    public DatabaseHelper databaseHelper;
    private int resource;
    private Context context;
    private GameMechanicsHelper gameMechanicsHelper;
    private GoalsAndTasksHelper goalsAndTasksHelper;

    public GoalsAndTasksAdapter(Context context, int resource, GameMechanicsHelper gameMechanicsHelper, DatabaseHelper databaseHelper,  List<String> objects, GoalsAndTasksHelper goalsAndTasksHelper){
        this.context = context;
        this.resource = resource;
        this.gameMechanicsHelper = gameMechanicsHelper;
        this.databaseHelper = databaseHelper;
        this.goalsAndTasksHelper = goalsAndTasksHelper;
        goalsAndTasks = objects;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    @Override
    public View generateView(final int position, ViewGroup parent){
        View convertView = LayoutInflater.from(context).inflate(R.layout.goal_and_task_row, null);
        final SwipeLayout swipeLayout = (SwipeLayout) convertView.findViewById(getSwipeLayoutResourceId(position));

        ImageButton editBtn = (ImageButton) convertView.findViewById(R.id.editButton);
        ImageButton deleteBtn = (ImageButton) convertView.findViewById(R.id.deleteButton);
        ImageButton completedBtn = (ImageButton) convertView.findViewById(R.id.completedButton);
        ImageButton failedBtn = (ImageButton) convertView.findViewById(R.id.failedButton);

        editBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                goalsAndTasksHelper.displayGoalsAndTasksEditMenu(position);
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                notifyDataSetChanged();
                databaseHelper.deleteData(goalsAndTasks.get(position));
                goalsAndTasks.remove(position);
            }
        });

        completedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyDataSetChanged();
                databaseHelper.deleteData(goalsAndTasks.get(position));
                goalsAndTasks.remove(position);
                gameMechanicsHelper.addXp();
            }
        });

        failedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyDataSetChanged();
                databaseHelper.deleteData(goalsAndTasks.get(position));
                goalsAndTasks.remove(position);
                gameMechanicsHelper.removeHealth();
            }
        });

        return convertView;
    }

    @Override
    public int getCount() {
        return goalsAndTasks.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void fillValues(int position, View convertView) {
        final TextView goalsAndTasksView;
        goalsAndTasksView = (TextView) convertView.findViewById(R.id.goal_task_textView);
        goalsAndTasksView.setText(goalsAndTasks.get(position));
    }
}
