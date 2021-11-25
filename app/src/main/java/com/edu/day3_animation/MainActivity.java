package com.edu.day3_animation;

import static java.lang.Thread.sleep;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final List<BarEntry> entries = new ArrayList<>();
    private List<Expense> listExpenseSevenDays;
    private List<Expense> listExpenseLastWeek;
    private TabLayout tabLayout;
    private RoundedBarChart barChart;
    private TextView textViewValueTotal;
    private LinearLayout linerLayoutDetail;
    private ConstraintLayout constraintLayoutSuperMarket;
    private ConstraintLayout constraintLayoutElectric;
    private ConstraintLayout constraintLayoutInternet;
    private CardView cardViewSuperMarketBar;
    private CardView cardViewElectricBar;
    private CardView cardViewInternetBar;
    private TextView textViewSuperMarket;
    private TextView textViewElectric;
    private TextView textViewInternet;
    private int widthBegin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        setXAxis();
        setYAxis();
        setAnimation();
        registerTabLayoutSelectedListener();
        registerBarChartSelectedListener();
    }

    private void registerBarChartSelectedListener() {
        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                onClickColumn((int) e.getX());
            }

            @Override
            public void onNothingSelected() {

            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void onClickColumn(int position) {
        resetViewDetail();
        String currencyUnit = getString(R.string.currency_unit);
        String convertToThousand = getString(R.string.convert_to_thousand);
        Expense expense = null;
        if (tabLayout.getSelectedTabPosition() == 0) {
            expense = listExpenseSevenDays.get(position);
        } else if (tabLayout.getSelectedTabPosition() == 1) {
            expense = listExpenseLastWeek.get(position);
        }

        int superMarket = 0;
        int electric = 0;
        int internet = 0;
        if (expense != null) {
            superMarket = expense.getSuperMarket();
            electric = expense.getElectric();
            internet = expense.getInternet();
            textViewSuperMarket.setText(superMarket + "" + convertToThousand + currencyUnit);
            textViewElectric.setText(electric + "" + convertToThousand + currencyUnit);
            textViewInternet.setText(internet + "" + convertToThousand + currencyUnit);
        }

        linerLayoutDetail.setVisibility(View.VISIBLE);

        Animation alphaAnimation = AnimationUtils.loadAnimation(this, R.anim.alpha_animation);
        constraintLayoutSuperMarket.setAnimation(alphaAnimation);
        constraintLayoutElectric.setAnimation(alphaAnimation);
        constraintLayoutInternet.setAnimation(alphaAnimation);

        Handler handlerSuperMarket = new Handler();
        Runnable runnableSuperMarket = () -> {
            cardViewSuperMarketBar.getLayoutParams().width = cardViewSuperMarketBar.getWidth() + 2;
            cardViewSuperMarketBar.requestLayout();
        };
        int finalSuperMarket = superMarket;
        Thread threadSuperMarketBar = new Thread(() -> {
            try {
                sleep(2000);
                do {
                    handlerSuperMarket.post(runnableSuperMarket);
                    sleep(10);
                } while (cardViewSuperMarketBar.getWidth() < finalSuperMarket);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        threadSuperMarketBar.start();

        Handler handlerElectric = new Handler();
        Runnable runnableElectric = () -> {
            cardViewElectricBar.getLayoutParams().width = cardViewElectricBar.getWidth() + 2;
            cardViewElectricBar.requestLayout();
        };
        int finalElectric = electric;
        Thread threadElectric = new Thread(() -> {
            try {
                sleep(2000);
                do {
                    handlerElectric.post(runnableElectric);
                    sleep(10);
                } while (cardViewElectricBar.getWidth() < finalElectric);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        threadElectric.start();

        Handler handlerInternet = new Handler();
        Runnable runnableInternet = () -> {
            cardViewInternetBar.getLayoutParams().width = cardViewInternetBar.getWidth() + 2;
            cardViewInternetBar.requestLayout();
        };
        int finalInternet = internet;
        Thread threadInternet = new Thread(() -> {
            try {
                sleep(2000);
                do {
                    handlerInternet.post(runnableInternet);
                    sleep(10);
                } while (cardViewInternetBar.getWidth() < finalInternet);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        threadInternet.start();
    }

    private void resetViewDetail() {
        linerLayoutDetail.setVisibility(View.GONE);
        textViewSuperMarket.setText(null);
        textViewInternet.setText(null);
        textViewElectric.setText(null);
        cardViewSuperMarketBar.getLayoutParams().width = widthBegin;
        cardViewElectricBar.getLayoutParams().width = widthBegin;
        cardViewInternetBar.getLayoutParams().width = widthBegin;
    }

    private void registerTabLayoutSelectedListener() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 1) {
                    List<BarEntry> oldEntries = new ArrayList<>(entries);
                    setDataLastWeek();
                    AnimateDataSetChanged changer = new AnimateDataSetChanged(600, barChart, oldEntries, entries);
                    changer.setInterpolator(new AccelerateInterpolator());
                    changer.run();
                    linerLayoutDetail.setVisibility(View.GONE);
                } else if (tab.getPosition() == 0) {
                    List<BarEntry> oldEntries = new ArrayList<>(entries);
                    setDataSevenDays();
                    AnimateDataSetChanged changer = new AnimateDataSetChanged(600, barChart, oldEntries, entries);
                    changer.setInterpolator(new AccelerateInterpolator());
                    changer.run();
                    linerLayoutDetail.setVisibility(View.GONE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initView() {
        tabLayout = findViewById(R.id.tabLayout);
        textViewValueTotal = findViewById(R.id.textViewValueTotal);
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.label_tab_layout_seven_day)));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.label_tab_layout_last_week)));
        barChart = findViewById(R.id.barChart);
        Legend legend = barChart.getLegend();
        legend.setEnabled(false);

        linerLayoutDetail = findViewById(R.id.linerLayoutDetail);
        constraintLayoutSuperMarket = findViewById(R.id.constraintLayoutSuperMarket);
        constraintLayoutElectric = findViewById(R.id.constraintLayoutElectric);
        constraintLayoutInternet = findViewById(R.id.constraintLayoutInternet);

        cardViewSuperMarketBar = findViewById(R.id.cardViewSuperMarketBar);
        cardViewElectricBar = findViewById(R.id.cardViewElectricBar);
        cardViewInternetBar = findViewById(R.id.cardViewInternetBar);

        textViewSuperMarket = findViewById(R.id.textViewSuperMarket);
        textViewElectric = findViewById(R.id.textViewElectric);
        textViewInternet = findViewById(R.id.textViewInternet);
    }

    private void initData() {
        setDataSevenDays();
        BarDataSet barDataSet = new BarDataSet(entries, getResources().getString(R.string.label_bar_data_set));
        barDataSet.setDrawValues(false);
        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);
        Description description = new Description();
        description.setEnabled(false);
        barChart.setDescription(description);
        widthBegin = cardViewSuperMarketBar.getLayoutParams().width;
    }

    private void setAnimation() {
        barChart.animateY(2000);
        setAnimationTextView();
    }

    private void setXAxis() {
        ArrayList<String> labels = new ArrayList<>();
        for (Label label : Label.values()) {
            labels.add(label.getDay());
        }

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                return labels.get((int) value);
            }
        });
        xAxis.setTextColor(Color.WHITE);

        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);

    }

    private void setYAxis() {
        YAxis barChartYAxisLeft = barChart.getAxisLeft();
        barChartYAxisLeft.setEnabled(false);
        barChartYAxisLeft.setAxisMaximum(700);
        barChartYAxisLeft.setAxisMinimum(10f);

        YAxis barChartYAxisRight = barChart.getAxisRight();
        barChartYAxisRight.setEnabled(false);
        barChartYAxisRight.setAxisMaximum(700);
        barChartYAxisRight.setAxisMinimum(10f);


        barChartYAxisLeft.setDrawAxisLine(false);
        barChartYAxisRight.setDrawAxisLine(false);
    }

    private void setAnimationTextView() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.animation_text_view_total);
        animation.reset();
        textViewValueTotal.clearAnimation();
        textViewValueTotal.setAnimation(animation);
    }

    private void setDataSevenDays() {
        listExpenseSevenDays = new ArrayList<>();
        listExpenseSevenDays.add(new Expense(220, 75, 50));
        listExpenseSevenDays.add(new Expense(235, 100, 65));
        listExpenseSevenDays.add(new Expense(230, 80, 75));
        listExpenseSevenDays.add(new Expense(435, 125, 65));
        listExpenseSevenDays.add(new Expense(410, 75, 80));
        listExpenseSevenDays.add(new Expense(350, 80, 90));
        listExpenseSevenDays.add(new Expense(360, 110, 90));

        entries.clear();

        for (int i = 0; i < 7; i++) {
            entries.add(new BarEntry(i, listExpenseSevenDays.get(i).getTotalExpense()));
        }
    }

    private void setDataLastWeek() {
        listExpenseLastWeek = new ArrayList<>();
        listExpenseLastWeek.add(new Expense(280, 70, 70));
        listExpenseLastWeek.add(new Expense(325, 130, 55));
        listExpenseLastWeek.add(new Expense(220, 60, 45));
        listExpenseLastWeek.add(new Expense(285, 80, 85));
        listExpenseLastWeek.add(new Expense(430, 85, 90));
        listExpenseLastWeek.add(new Expense(390, 90, 95));
        listExpenseLastWeek.add(new Expense(290, 80, 75));
        entries.clear();
        for (int i = 0; i < 7; i++) {
            entries.add(new BarEntry(i, listExpenseLastWeek.get(i).getTotalExpense()));
        }
    }
}