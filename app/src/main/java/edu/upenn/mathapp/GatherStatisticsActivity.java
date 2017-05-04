package edu.upenn.mathapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.example.ChoiceImpl.AverageStatistics;

/**
 * Created by Ben on 5/4/2017.
 */

public class GatherStatisticsActivity extends AppCompatActivity {
    private ViewFlipper viewFlipper;
    private float lastX;

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
// To dismiss the dialog
        progress.dismiss();
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

}
