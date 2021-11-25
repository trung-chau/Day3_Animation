package com.edu.day3_animation;

import android.os.Handler;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AnimateDataSetChanged {
    private final int duration;
    private final BarChart chart;
    private final List<BarEntry> oldData;
    private final List<BarEntry> newData;
    private long startTime;
    private int fps = 30;
    private Handler timerHandler;
    private Interpolator interpolator;

    public AnimateDataSetChanged(int duration, BarChart chart, List<BarEntry> oldData, List<BarEntry> newData) {
        this.duration = duration;
        this.chart = chart;
        this.oldData = new ArrayList<>(oldData);
        this.newData = new ArrayList<>(newData);
        interpolator = new LinearInterpolator();
    }

    public void setInterpolator(Interpolator interpolator) {
        this.interpolator = interpolator;
    }

    public void run(int fps) {
        this.fps = fps;
        run();
    }

    public void run() {
        startTime = Calendar.getInstance().getTimeInMillis();
        timerHandler = new Handler();
        Runner runner = new Runner();
        runner.run();
    }

    private class Runner implements Runnable {
        @Override
        public void run() {
            float increment = (Calendar.getInstance().getTimeInMillis() - startTime) / (float) duration;
            increment = interpolator.getInterpolation(increment < 0f ? 0f : Math.min(increment, 1f));
            chart.getData().getDataSetByIndex(0).clear();
            for (int i = 0; i < newData.size(); i++) {
                float oldY = oldData.size() > i ? oldData.get(i).getY() : newData.get(i).getY();
                float oldX = oldData.size() > i ? oldData.get(i).getX() : newData.get(i).getX();
                float newX = newData.get(i).getX();
                float newY = newData.get(i).getY();
                BarEntry e = new BarEntry(oldX + (newX - oldX) * increment, oldY + (newY - oldY) * increment);
                chart.getData().getDataSetByIndex(0).addEntry(e);
            }
            chart.getXAxis().resetAxisMaximum();
            chart.getXAxis().resetAxisMinimum();
            chart.notifyDataSetChanged();
            chart.refreshDrawableState();
            chart.invalidate();
            chart.setAutoScaleMinMaxEnabled(true);
            if (increment < 1f) {
                timerHandler.postDelayed(this, 1000 / fps);
            }
        }
    }
}
