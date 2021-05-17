package com.example.machinetest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements DownloadData.OnDataDownloadComplete {

    BarChart lineGraph;
    Legend l;
    YAxis y;
    XAxis x;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lineGraph = findViewById(R.id.lineGraph);

        callAsynchronousTask();


    }



    public void downloadData()
    {
        if (isInternetAvail())
        {
            DownloadData downloadData = new DownloadData(this,this);
            downloadData.downloadDetails();
        }
        else
        {
            Toast.makeText(this, "Please Connect To Internet",
                    Toast.LENGTH_SHORT).show();
        }

    }


    public void callAsynchronousTask() {
        final Handler handler = new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try {
                            downloadData();
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 3000); //execute in every 50000 ms
    }


    public void setGraphData(ArrayList<Model> arrayList)
    {
         final ArrayList<String> labels = new ArrayList<String>();
         final ArrayList<BarEntry> entry1 = new ArrayList<BarEntry>();
         final ArrayList<BarEntry> entry2 = new ArrayList<BarEntry>();
         final ArrayList<BarEntry> entry3 = new ArrayList<BarEntry>();

        for (int i=0;i<arrayList.size();i++)
        {
            entry1.add(new BarEntry(arrayList.get(i).getUsd_rate_float(), i));
            entry2.add(new BarEntry(arrayList.get(i).getGbp_rate_float(), i));
            entry3.add(new BarEntry(arrayList.get(i).getEur_rate_float(), i));
            labels.add("Rate");
        }
        BarDataSet barDataSet1 = new BarDataSet(entry1, "USD");  // creating dataset for group1
        barDataSet1.setValueTextSize(10);
        barDataSet1.setColor(Color.rgb(102, 255, 51));

        BarDataSet barDataSet2 = new BarDataSet(entry2, "GBP");  // creating dataset for group1
        barDataSet2.setValueTextSize(10);
        barDataSet2.setColor(Color.rgb(255, 204, 51));

        BarDataSet barDataSet3 = new BarDataSet(entry3, "EUR");  // creating dataset for group1
        barDataSet3.setValueTextSize(10);
        barDataSet3.setColor(Color.rgb(51, 255, 255));

        ArrayList<BarDataSet> dataSets = new ArrayList<>();
        dataSets.add(barDataSet1);
        dataSets.add(barDataSet2);
        dataSets.add(barDataSet3);



        BarData data = new BarData(labels, dataSets);
        lineGraph.setData(data);

//        lineGraph.setDescription("");
        lineGraph.animateY(1000);

        l = lineGraph.getLegend();
        l.setTextSize(15f);

        y = lineGraph.getAxisLeft();
        y.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        y.setTextSize(10);
        y.setAxisMinValue(2000000);

        x = lineGraph.getXAxis();
        x.setPosition(XAxis.XAxisPosition.BOTTOM);
        x.setTextSize(10);
    }


    public boolean isInternetAvail()
    {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else
            connected = false;

        return connected;
    }

    @Override
    public void downloadDownloadComplete(ArrayList<Model> arrayList) {
//        Toast.makeText(this, "Success",
//                Toast.LENGTH_SHORT).show();

        setGraphData(arrayList);
    }

    @Override
    public void downloadDownloadFailed() {
        Toast.makeText(this, "Failed",
                Toast.LENGTH_SHORT).show();
    }
}