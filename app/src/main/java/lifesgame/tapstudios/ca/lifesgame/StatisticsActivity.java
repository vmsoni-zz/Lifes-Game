package lifesgame.tapstudios.ca.lifesgame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.graphics.Color;
import android.view.animation.BounceInterpolator;


import com.db.chart.animation.Animation;
import com.db.chart.model.LineSet;
import com.db.chart.renderer.AxisRenderer;
import com.db.chart.view.LineChartView;

import lifesgame.tapstudios.ca.lifesgame.helper.DatabaseHelper;


public class StatisticsActivity extends AppCompatActivity {
    public LineChartView mChart;
    public DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        databaseHelper = new DatabaseHelper(this);
        mChart = (LineChartView) findViewById(R.id.mainChart);

        // Data
        LineSet dataset = databaseHelper.getCompletedGoalTasks();
        dataset.setSmooth(true);
        dataset.setDotsColor(Color.BLACK);

        dataset.setColor(Color.parseColor("#758cbb"))
                .setDotsColor(Color.parseColor("#758cbb")).setSmooth(false);
        mChart.addData(dataset);

        mChart.setAxisBorderValues(0, 500)
                .setYLabels(AxisRenderer.LabelPosition.OUTSIDE)
                .show(new Animation().setInterpolator(new BounceInterpolator())
                        .fromAlpha(0));
    }
}
