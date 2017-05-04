package edu.upenn.mathapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Ben on 5/3/2017.
 */

public class StatisticsHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats_home);

        final EditText setSize = (EditText) findViewById(R.id.setSize);
        final EditText numIter = (EditText) findViewById(R.id.numIter);

        Button submit = (Button) findViewById(R.id.simulate);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Handle if they leave blank
                String cand = setSize.getText().toString();
                String iter = numIter.getText().toString();
                int numCandidates = 1000, numIterations = 100;
                if (!cand.equals(""))
                    numCandidates = Integer.parseInt(cand);
                if (!iter.equals(""))
                    numIterations = Integer.parseInt(iter);

                Intent goToSim = new Intent(getApplicationContext(), GatherStatisticsActivity.class);
                goToSim.putExtra("NumCandidates", numCandidates);
                goToSim.putExtra("NumIterations", numIterations);
                startActivity(goToSim);
            }
        });
    }

}
