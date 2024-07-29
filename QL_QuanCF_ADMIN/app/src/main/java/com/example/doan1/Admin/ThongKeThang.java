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
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ThongKeThang extends Fragment implements OnChartValueSelectedListener {

    private PieChart mPieChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.thongkethang_fragment, container, false);

        mPieChart = view.findViewById(R.id.piechart);

        // Pie Chart Setup
        mPieChart.getDescription().setEnabled(false);
        mPieChart.setUsePercentValues(true);
        mPieChart.setDrawHoleEnabled(true);
        mPieChart.setHoleColor(Color.WHITE);
        mPieChart.setTransparentCircleRadius(55f);
        mPieChart.setEntryLabelColor(Color.BLACK);
        mPieChart.setEntryLabelTextSize(10f);

        fetchMonthlyStatistics();
        return view;
    }

    private void fetchMonthlyStatistics() {
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
                            updatePieChart(jsonResponse);
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

    private void updatePieChart(JSONObject data) {
        List<PieEntry> entries = new ArrayList<>();
        List<Integer> colors = new ArrayList<>();

        String[] months = {"Tháng 1", "Tháng 2", "Tháng 3", "Tháng 4", "Tháng 5",
                "Tháng 6", "Tháng 7", "Tháng 8", "Tháng 9", "Tháng 10", "Tháng 11", "Tháng 12"};
        int[] colorArray = {
                Color.GRAY, Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW,
                Color.CYAN, Color.MAGENTA, Color.LTGRAY, Color.DKGRAY, Color.BLACK,
                Color.rgb(255, 165, 0)  // ORANGE
        };

        try {
            JSONObject monthlyData = data.getJSONObject("monthly");
            for (int i = 0; i < months.length; i++) {
                if (monthlyData.has(String.valueOf(i + 1))) {
                    float value = (float) monthlyData.getDouble(String.valueOf(i + 1));
                    entries.add(new PieEntry(value, months[i]+":"+String.format("%,.0f", value) + " VND"));
                    colors.add(colorArray[i]);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        PieDataSet dataSet = new PieDataSet(entries, "");
        dataSet.setColors(colors);
        dataSet.setValueTextSize(12f);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setSliceSpace(3f);

        PieData pieData = new PieData(dataSet);
        pieData.setValueFormatter(new PercentFormatter(mPieChart));
        mPieChart.setData(pieData);
        mPieChart.invalidate();

        Legend legend = mPieChart.getLegend();
        legend.setEnabled(true);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setTextSize(20f);
        legend.setTextColor(Color.BLACK);
        legend.setDrawInside(false);

        mPieChart.setOnChartValueSelectedListener(this);
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        if (e instanceof PieEntry) {
            Toast.makeText(getContext(), "Value: " + e.getY() + "%", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNothingSelected() {
    }
}
