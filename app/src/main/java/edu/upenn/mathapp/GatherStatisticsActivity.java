package edu.upenn.mathapp;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.example.ChoiceImpl.AverageStatistics;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;

/**
 * Created by Ben on 5/4/2017.
 */

public class GatherStatisticsActivity extends AppCompatActivity {
    private ViewFlipper viewFlipper;
    private float lastX;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gather_statistics);
        int numCandidates = (int) getIntent().getSerializableExtra("NumCandidates");
        int numIterations = (int) getIntent().getSerializableExtra("NumIterations");
        viewFlipper = (ViewFlipper) findViewById(R.id.statviewflip);
        AverageStatistics stats = new AverageStatistics(numCandidates);

        TextView avgDP = (TextView) findViewById(R.id.avgRankDP);
        TextView rank1DP = (TextView) findViewById(R.id.dpRank1);
        TextView rank2DP = (TextView) findViewById(R.id.dpRank2);
        TextView rank3DP = (TextView) findViewById(R.id.dpRank3);
        TextView rank4DP = (TextView) findViewById(R.id.dpRank4);
        TextView rank5DP = (TextView) findViewById(R.id.dpRank5);

        TextView avgMOC = (TextView) findViewById(R.id.avgRankMOC);
        TextView rank1MOC = (TextView) findViewById(R.id.mocRank1);
        TextView rank2MOC = (TextView) findViewById(R.id.mocRank2);
        TextView rank3MOC = (TextView) findViewById(R.id.mocRank3);
        TextView rank4MOC = (TextView) findViewById(R.id.mocRank4);
        TextView rank5MOC = (TextView) findViewById(R.id.mocRank5);

        BarChart barChartDP = (BarChart) findViewById(R.id.bargraphdp);
        BarChart barChartMOC= (BarChart) findViewById(R.id.bargraphmoc);

        ProgressDialog progress = new ProgressDialog(this);
        progress.setTitle("Loading");
        progress.setMessage("Gathering data...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();

        stats.start();
        avgDP.setText(String.format("%.2f", stats.getAvgRankDP()));
        rank1DP.setText(String.format("%.2f", stats.getDPRankCounter1() * 100) + "%");
        rank2DP.setText(String.format("%.2f", stats.getDPRankCounter2() * 100) + "%");
        rank3DP.setText(String.format("%.2f", stats.getDPRankCounter3() * 100) + "%");
        rank4DP.setText(String.format("%.2f", stats.getDPRankCounter4() * 100) + "%");
        rank5DP.setText(String.format("%.2f", stats.getDPRankCounter5() * 100) + "%");

        avgMOC.setText(String.format("%.2f", stats.getAvgRankMOC()));
        rank1MOC.setText(String.format("%.2f", stats.getMOCRankCounter1() * 100) + "%");
        rank2MOC.setText(String.format("%.2f", stats.getMOCRankCounter2() * 100) + "%");
        rank3MOC.setText(String.format("%.2f", stats.getMOCRankCounter3() * 100) + "%");
        rank4MOC.setText(String.format("%.2f", stats.getMOCRankCounter4() * 100) + "%");
        rank5MOC.setText(String.format("%.2f", stats.getMOCRankCounter5() * 100) + "%");

        //Insert graph data
        ArrayList<BarEntry> yDataDP = new ArrayList<>();
        ArrayList<BarEntry> yDataMOC= new ArrayList<>();
        int[] bardataDP = stats.getDPRankCounterArray();
        int[] bardataMOC= stats.getMOCRankCounterArray();
        ArrayList<String> xAxis = new ArrayList<>();

        for ( int i = 0; i < numCandidates; i++ ) {
            yDataDP.add(new BarEntry((float) i, (float) bardataDP[i]));
            yDataMOC.add(new BarEntry((float) i, (float) bardataMOC[i]));
            xAxis.add(i, (i + "")); //X axis is giving me trouble, trying to put string numbers as X
        }

        BarDataSet barDataSetDP = new BarDataSet(yDataDP, "DPBarData");
        BarDataSet barDataSetMOC= new BarDataSet(yDataMOC, "MOCBarData");

        BarData dpData = new BarData(barDataSetDP);
        BarData mocData = new BarData(barDataSetMOC);

        barChartDP.setData(dpData);
        barChartDP.setTouchEnabled(true);
        barChartDP.isScaleXEnabled();
        barChartDP.setVisibleXRange(0, 20);

        barChartMOC.setData(mocData);
        barChartMOC.setTouchEnabled(true);
        barChartMOC.isScaleXEnabled();
        barChartMOC.setVisibleXRange(0, 20);

        barChartDP.invalidate();
        barChartMOC.invalidate();


// To dismiss the dialog
        progress.dismiss();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    // Using the following method, we will handle all screen swaps.
    public boolean onTouchEvent(MotionEvent touchevent) {
        switch (touchevent.getAction()) {

            case MotionEvent.ACTION_DOWN:
                lastX = touchevent.getX();
                break;
            case MotionEvent.ACTION_UP:
                float currentX = touchevent.getX();

                // Handling left to right screen swap.
                if (lastX < currentX) {

                    // If there aren't any other children, just break.
                    if (viewFlipper.getDisplayedChild() == 0)
                        break;

                    // Next screen comes in from left.
                    viewFlipper.setInAnimation(this, R.anim.slide_in_from_left);
                    // Current screen goes out from right.
                    viewFlipper.setOutAnimation(this, R.anim.slide_out_to_right);

                    // Display next screen.
                    viewFlipper.showNext();
                }

                // Handling right to left screen swap.
                if (lastX > currentX) {

                    // If there is a child (to the left), kust break.
                    if (viewFlipper.getDisplayedChild() == 1)
                        break;

                    // Next screen comes in from right.
                    viewFlipper.setInAnimation(this, R.anim.slide_in_from_right);
                    // Current screen goes out from left.
                    viewFlipper.setOutAnimation(this, R.anim.slide_out_to_left);

                    // Display previous screen.
                    viewFlipper.showPrevious();
                }
                break;
        }
        return false;
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("GatherStatistics Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
