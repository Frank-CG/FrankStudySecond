package com.example.frankstudysecond;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.example.frankstudysecond.Protocols.ApiService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    CalendarView frankCalendar;

    private TableLayout mTableLayout;
    ProgressDialog mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        frankCalendar = (CalendarView) findViewById(R.id.frankCalendarView);
        frankCalendar.setFocusedMonthDateColor(Color.RED); // set the red color for the dates of  focused month
        frankCalendar.setUnfocusedMonthDateColor(Color.BLUE); // set the yellow color for the dates of an unfocused month
        frankCalendar.setSelectedWeekBackgroundColor(Color.RED); // red color for the selected week's background
        frankCalendar.setWeekSeparatorLineColor(Color.GREEN); // green color for the week separator line
        // perform setOnDateChangeListener event on CalendarView
        frankCalendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                // display the selected date by using a toast
//                Toast.makeText(getApplicationContext(), dayOfMonth + "/" + (month+1) + "/" + year, Toast.LENGTH_LONG).show();
                String msg = "" + dayOfMonth + "/" + (month+1) + "/" + year;
                Log.d(this.getClass().getName(),msg);
            }
        });

        mProgressBar = new ProgressDialog(this);

        // setup the table
//        mTableLayout = (TableLayout) findViewById(R.id.meetingsList);
//
//        mTableLayout.setStretchAllColumns(true);

        //startLoadData();
    }

    public void startLoadData() {
        mProgressBar.setCancelable(false);
        mProgressBar.setMessage("Fetching Invoices..");
        mProgressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressBar.show();
        new LoadDataTask().execute();
    }

    private JSONObject fetchDataFromApi(String fromDate, String endDate) {
        JSONObject resp = ApiService.GetEventList(fromDate,endDate);
        Log.d(this.getLocalClassName(),resp.toString());
        return resp;
    }

    private void loadDataForMeetingList(List<JSONObject> meetingList) throws JSONException {
        int leftRowMargin=0;
        int topRowMargin=0;
        int rightRowMargin=0;
        int bottomRowMargin = 0;

        mTableLayout.removeAllViews();
        int rowsCnt = meetingList.size();
        JSONObject meeting;
        for(int i=-1; i<rowsCnt; i++){
            if (i > -1) {
                meeting = meetingList.get(i);
            }

//            final TableRow tr = new TableRow(this);
//            tr.setId(i + 1);
//            TableLayout.LayoutParams trParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT,
//                    TableLayout.LayoutParams.WRAP_CONTENT);
//            trParams.setMargins(leftRowMargin, topRowMargin, rightRowMargin, bottomRowMargin);
//            tr.setPadding(0,0,0,0);
//            tr.setLayoutParams(trParams);
//
//            final LinearLayout layMeeting = new LinearLayout(this);
//            layMeeting.setOrientation(LinearLayout.VERTICAL);
//            layMeeting.setPadding(0, 10, 0, 10);
//            layMeeting.setBackgroundColor(Color.parseColor("#f8f8f8"));
        }
    }

    class LoadDataTask extends AsyncTask<String, Integer, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... params) {
            JSONObject rst;
            if(params.length == 0){
                rst = fetchDataFromApi(new SimpleDateFormat("yyyyMMdd").format(new Date()),"");
            }else if(params.length == 1){
                rst = fetchDataFromApi(params[0],"");
            }else {
                rst = fetchDataFromApi(params[0],params[1]);
            }
            return rst;
        }
        @Override
        protected void onPostExecute(JSONObject result) {
            mProgressBar.hide();
            try {
                JSONArray meetings = result.getJSONArray("ContentEntityDatas");
                ArrayList<JSONObject> meetingList = new ArrayList<JSONObject>();
                if(meetings != null){
                    int len = meetings.length();
                    for(int i=0; i<len; i++){
                        meetingList.add(meetings.getJSONObject(i));
                    }
                }
                loadDataForMeetingList(meetingList);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        @Override
        protected void onPreExecute() {
        }
        @Override
        protected void onProgressUpdate(Integer... values) {

        }
    }
}
