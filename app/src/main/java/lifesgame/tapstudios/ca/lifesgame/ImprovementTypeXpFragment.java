package lifesgame.tapstudios.ca.lifesgame;

import android.animation.PropertyValuesHolder;
import android.app.Fragment;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.db.chart.animation.Animation;
import com.db.chart.model.BarSet;
import com.db.chart.renderer.XRenderer;
import com.db.chart.renderer.YRenderer;
import com.db.chart.tooltip.Tooltip;
import com.db.chart.util.Tools;
import com.db.chart.view.BarChartView;

import java.text.DecimalFormat;

import lifesgame.tapstudios.ca.lifesgame.helper.DatabaseHelper;

/**
 * Created by viditsoni on 2017-11-09.
 */

public class ImprovementTypeXpFragment extends Fragment {
    public BarChartView improvementTypeXpChart;
    public DatabaseHelper databaseHelper;
    private Tooltip mTip;
    private View improvementView;
    private int totalDeleted;

    public ImprovementTypeXpFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        databaseHelper = new DatabaseHelper(getContext());
        improvementView = inflater.inflate(R.layout.activity_improvement, container, false);
        mTip = new Tooltip(improvementView.getContext(), R.layout.linechart_tooltip, R.id.value);
        improvementTypeXpChart = (BarChartView) improvementView.findViewById(R.id.improvementTypesChart);

        ((TextView) mTip.findViewById(R.id.value)).setTypeface(
                Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Semibold.ttf"));

        mTip.setVerticalAlignment(Tooltip.Alignment.BOTTOM_TOP);
        mTip.setDimensions((int) Tools.fromDpToPx(58), (int) Tools.fromDpToPx(25));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {

            mTip.setEnterAnimation(PropertyValuesHolder.ofFloat(View.ALPHA, 1),
                    PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f),
                    PropertyValuesHolder.ofFloat(View.SCALE_X, 1f)).setDuration(200);

            mTip.setExitAnimation(PropertyValuesHolder.ofFloat(View.ALPHA, 0),
                    PropertyValuesHolder.ofFloat(View.SCALE_Y, 0f),
                    PropertyValuesHolder.ofFloat(View.SCALE_X, 0f)).setDuration(200);

            mTip.setPivotX(Tools.fromDpToPx(65) / 2);
            mTip.setPivotY(Tools.fromDpToPx(25));
        }
        //Graph Data
        BarSet barSet = databaseHelper.getImprovementTypesXP();

        Paint gridPaint = new Paint();
        gridPaint.setColor(Color.parseColor("#ffffff"));
        gridPaint.setStyle(Paint.Style.STROKE);
        gridPaint.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
        gridPaint.setAntiAlias(true);
        gridPaint.setStrokeWidth(Tools.fromDpToPx(.75f));

        setupImprovementTypeXpChart(barSet, gridPaint);

        return improvementView;
    }

    @Override
    public void onResume() {
        super.onResume();
        //Graph Data

        int updatedTotalDeleted = databaseHelper.getTotalDeletedCount();
        if(databaseHelper.getTotalDeletedCount() != totalDeleted) {
            BarSet barSet = databaseHelper.getImprovementTypesXP();
            totalDeleted = updatedTotalDeleted;
            updateGraphs(barSet);
        }
    }

    public void setupImprovementTypeXpChart(BarSet barSet, Paint gridPaint) {
        barSet.setColor(Color.parseColor("#fc2a53"));
        improvementTypeXpChart.addData(barSet);
        improvementTypeXpChart.setXLabels(XRenderer.LabelPosition.OUTSIDE)
                .setYLabels(YRenderer.LabelPosition.OUTSIDE)
                .setLabelsFormat(new DecimalFormat("0"))
                .setAxisThickness(6)
                .setAxisLabelsSpacing(35)
                .setGrid(7, 0, gridPaint)
                .show(new Animation());
        improvementTypeXpChart.setBackgroundColor(Color.parseColor("#04baa6"));
    }

    private void updateGraphs(BarSet barSet) {
        improvementTypeXpChart.reset();

        Paint gridPaint = new Paint();
        gridPaint.setColor(Color.parseColor("#ffffff"));
        gridPaint.setStyle(Paint.Style.STROKE);
        gridPaint.setPathEffect(new DashPathEffect(new float[]{10, 20}, 0));
        gridPaint.setAntiAlias(true);
        gridPaint.setStrokeWidth(Tools.fromDpToPx(.75f));

        setupImprovementTypeXpChart(barSet, gridPaint);
    }
}
