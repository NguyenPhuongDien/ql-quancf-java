package com.example.doan1.Admin;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.doan1.Connect.Connect;
import com.example.doan1.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ThongKeNgay extends Fragment implements OnChartValueSelectedListener {

    private BarChart mBarChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.thongkengay_fragment, container, false);

        mBarChart = view.findViewById(R.id.barchart);

        mBarChart.getDescription().setEnabled(false);
        mBarChart.setDrawGridBackground(false);

        XAxis xAxis = mBarChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(7);

        mBarChart.getAxisLeft().setDrawGridLines(false);
        mBarChart.getAxisRight().setEnabled(false);

        fetchDailyStatistics();
        return view;
    }

    private void fetchDailyStatistics() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("http://" + Connect.connectip + "/DoAn_Android/ThongKe.php");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();

                    JSONObject jsonResponse = new JSONObject(response.toString());

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateBarChart(jsonResponse);
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "Lỗi kết nối", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).start();
    }

    private void updateBarChart(JSONObject data) {
        List<BarEntry> barEntries = new ArrayList<>();
        List<String> xAxisLabels = new ArrayList<>();

        try {
            JSONObject dailyData = data.getJSONObject("daily");
            JSONArray keys = dailyData.names();

            if (keys != null) {
                for (int i = 0; i < keys.length(); i++) {
                    String dateKey = keys.getString(i);
                    float value = (float) dailyData.getDouble(dateKey);

                    // Extract the day and month from the date string
                    String formattedDate = dateKey.substring(8, 10) + "/" + dateKey.substring(5, 7);
                    xAxisLabels.add(formattedDate);

                    barEntries.add(new BarEntry(i, value));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        BarDataSet dataSet = new BarDataSet(barEntries, "Doanh thu hàng ngày");
        dataSet.setColor(Color.RED);
        dataSet.setValueTextSize(20f);
        dataSet.setValueTextColor(Color.BLACK);

        dataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                DecimalFormat df = new DecimalFormat("#,###");
                return df.format(value) + " VND";
            }
        });

        BarData barData = new BarData(dataSet);
        mBarChart.setData(barData);
        mBarChart.invalidate();

        XAxis xAxis = mBarChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisLabels));
        xAxis.setGranularity(1f);

        mBarChart.setOnChartValueSelectedListener(this);
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        if (e instanceof BarEntry) {
            DecimalFormat df = new DecimalFormat("#,###");
            String formattedValue = df.format(e.getY());
            Toast.makeText(getContext(), "Doanh thu: " + formattedValue + " VND", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNothingSelected() {
    }
}
