package edu.upenn.mathapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ChoiceImpl.Date;

import java.util.ArrayList;

/**
 * Created by Ben on 5/3/2017.
 */

public class DatingHomeActivity extends AppCompatActivity {
    private boolean firstRun = true;
    private final Context context = this;
    private Date dateHack;

    private int numAvailableDates = 10; //the NumCandidates used in the future

    private ArrayList<Date> datesByPreferenceOrder;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dating_home);

        if (true) {
            //firstRun = false; TODO: Figure out details here. This isnt well thought out
            promptForInput();
            datesByPreferenceOrder = new ArrayList<Date>(numAvailableDates);
        }



        /*********************Programmatically list all dates**********************/

        LinearLayout dates = (LinearLayout) findViewById(R.id.dateList);
        TextView datecount = (TextView) findViewById(R.id.dateCount);
        TextView datestotal= (TextView) findViewById(R.id.totalDates);
        datecount.setText("Date Count: " + datesByPreferenceOrder.size());
        datestotal.setText("Total Dates: " + numAvailableDates);
        for (Date d : datesByPreferenceOrder) {
            RelativeLayout layout = new RelativeLayout(this);
            dateHack = d; //This is so the onclick listener below can access the current date. IDK why it works but it does
            ImageButton profile = new ImageButton(this);
            //TODO: Insert their picture if they have it. Default for now
            profile.setImageResource(R.drawable.nopic);
            profile.setLayoutParams(new RelativeLayout.LayoutParams(100, 100));

            profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent viewDate = new Intent(getApplicationContext(), ViewDateActivity.class);
                    viewDate.putExtra("Date", dateHack);
                    startActivity(viewDate);
                }
            });

            TextView name = new TextView(this);
            name.setText(d.getName());

            TextView age = new TextView(this);
            age.setText(d.getAge());

            TextView occupation = new TextView(this);
            occupation.setText(d.getOccupation());

            TextView relRank = new TextView(this);
            relRank.setText(datesByPreferenceOrder.indexOf(d));

            layout.addView(profile);
            layout.addView(name);
            layout.addView(age);
            layout.addView(occupation);
            dates.addView(layout);
        }

        Button newDate = (Button) findViewById(R.id.newDate);
        newDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newDateIntent = new Intent(getApplicationContext(), NewDateActivity.class);
                newDateIntent.putExtra("NumDates", datesByPreferenceOrder.size());
                startActivity(newDateIntent);
            }
        });

    }

    public void insertDateAtIndex(int index, Date d) {
        //TODO: Implement
    }


    private void promptForInput() {
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.date_preference_prompt, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.numDatesDialogue);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result
                                // edit text
                                String in = userInput.getText().toString();
                                if (in.equals("")) {
                                    Toast.makeText(getApplicationContext(),
                                            "We defaulted the choice to 10.",
                                            Toast.LENGTH_LONG)
                                            .show();
                                    numAvailableDates = 10;
                                } else {
                                    numAvailableDates = Integer.parseInt(in);
                                }

                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();


    }

}
