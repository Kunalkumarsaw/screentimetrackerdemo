package com.codinghub.screentimetracker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.sql.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static android.app.AppOpsManager.MODE_ALLOWED;
import static android.app.AppOpsManager.OPSTR_GET_USAGE_STATS;

public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView = findViewById(R.id.listView);

        AppOpsManager appOps = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
        int mode = appOps.checkOpNoThrow(OPSTR_GET_USAGE_STATS, android.os.Process.myUid(), getPackageName());
       if (mode == MODE_ALLOWED){
           UsageStatsManager usageStatsManager = (UsageStatsManager) getSystemService(Context.USAGE_STATS_SERVICE);
           Calendar calendar = Calendar.getInstance();
           calendar.add(Calendar.MONTH, -1);
           long start = calendar.getTimeInMillis();
           long end = System.currentTimeMillis();
           List<UsageStats> queryUsageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST,start,end);
           Log.d("actualdata","ANS 1 = "+queryUsageStats.get(0).getPackageName()+" is = " + queryUsageStats.get(0).getTotalTimeInForeground());
           ArrayList<String> arrayList = new ArrayList<>();
           int lenth = queryUsageStats.size();
           for (int i=0 ;i<lenth;i++){
               arrayList.add(queryUsageStats.get(i).getPackageName()+" is = " + getDate(queryUsageStats.get(i).getTotalTimeInForeground())+"and in millies =" +queryUsageStats.get(i).getTotalTimeInForeground());
           }
           ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,R.layout.list_view_adapter,R.id.adapter_textView, arrayList);
           listView.setAdapter(arrayAdapter);


      }else {
           startActivity(new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS));
       }
    }
    public static String getDate(long milliSeconds )
    {
//        // Create a DateFormatter object for displaying date in specified format.
//        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
//
//        // Create a calendar object that will convert the date and time value in milliseconds to date.
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(milliSeconds);
        long sec = milliSeconds/1000;
        long min = sec/60;
        sec= sec%60;
        long hr = min/60;
        min=min%60;
        return ""+hr+":"+min+":"+sec;
    }
}