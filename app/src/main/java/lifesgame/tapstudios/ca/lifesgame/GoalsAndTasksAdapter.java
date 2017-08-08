package lifesgame.tapstudios.ca.lifesgame;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import lifesgame.tapstudios.ca.lifesgame.helper.DatabaseHelper;
import lifesgame.tapstudios.ca.lifesgame.helper.GameMechanicsHelper;
import lifesgame.tapstudios.ca.lifesgame.helper.GoalsAndTasksHelper;

/**
 * Created by Vidit Soni on 5/24/2017.
 */
public class GoalsAndTasksAdapter extends BaseSwipeAdapter {
    public List<GoalsAndTasks> goalsAndTasks;
    public LayoutInflater inflater;
    public DatabaseHelper databaseHelper;
    private int resource;
    private Context context;
    private GameMechanicsHelper gameMechanicsHelper;
    private GoalsAndTasksHelper goalsAndTasksHelper;

    public GoalsAndTasksAdapter(Context context, int resource, GameMechanicsHelper gameMechanicsHelper, DatabaseHelper databaseHelper,  List<GoalsAndTasks> objects, GoalsAndTasksHelper goalsAndTasksHelper){
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
                Date dt = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentTime = sdf.format(dt);
                notifyDataSetChanged();
                databaseHelper.deleteData(goalsAndTasks.get(position).getId(), false, currentTime);
                goalsAndTasks.remove(position);
            }
        });

        completedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date dt = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentTime = sdf.format(dt);
                notifyDataSetChanged();
                gameMechanicsHelper.addSilver(goalsAndTasks.get(position).getSilver());
                databaseHelper.deleteData(goalsAndTasks.get(position).getId(), true, currentTime);
                goalsAndTasks.remove(position);
                gameMechanicsHelper.addXp();
            }
        });

        failedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date dt = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String currentTime = sdf.format(dt);
                notifyDataSetChanged();
                databaseHelper.deleteData(goalsAndTasks.get(position).getId(), false, currentTime);
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
        final TextView goalsAndTasksTitle;
        final TextView goalsAndTasksDescription;
        goalsAndTasksTitle = (TextView) convertView.findViewById(R.id.goal_task_title);
        goalsAndTasksTitle.setText(goalsAndTasks.get(position).getTitle());
        goalsAndTasksDescription = (TextView) convertView.findViewById(R.id.goal_task_description);
        goalsAndTasksDescription.setText(goalsAndTasks.get(position).getDescription());
    }
}
