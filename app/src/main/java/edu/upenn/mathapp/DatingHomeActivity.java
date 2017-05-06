package edu.upenn.mathapp;

import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ChoiceImpl.Date;

import java.util.ArrayList;

/**
 * Created by Ben on 5/3/2017.
 */

public class DatingHomeActivity extends AppCompatActivity {
    private static boolean firstRun = true; //static so it keeps its state
    private final Context context = this;

    private int numAvailableDates = 10; //the NumCandidates used in the future

    private static ArrayList<Date> datesByPreferenceOrder;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dating_home);

        if (firstRun) {
            firstRun = false; //TODO: Figure out details here. This isnt well thought out
            promptForInput();
            datesByPreferenceOrder = new ArrayList<Date>(numAvailableDates);

            //init arraylist with nulls apparently
            int i = 0;
            while ( i < numAvailableDates ) {
                datesByPreferenceOrder.add(datesByPreferenceOrder.size(), null);
                i++;
            }
        }



        /*********************Programmatically list all dates**********************/

        LinearLayout dates = (LinearLayout) findViewById(R.id.dateList);
        TextView datecount = (TextView) findViewById(R.id.dateCount);
        TextView datestotal= (TextView) findViewById(R.id.totalDates);
        datecount.setText("Date Count: " + RankDateActivity.getEffectiveSize(datesByPreferenceOrder));
        datestotal.setText("Total Dates: " + numAvailableDates);
        int idSetter = 0;
        for (Date d : datesByPreferenceOrder) {

            if (d == null) continue;

            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.HORIZONTAL);


            ImageButton profile = new ImageButton(this);
            profile.setId(idSetter);

            if (d.getPicture() == null || !d.getPicture().exists())
                profile.setImageResource(R.drawable.nopic);
            else {
                Bitmap b = BitmapFactory.decodeFile(d.getPicture().getAbsolutePath());
                profile.setImageBitmap(b.createScaledBitmap(b, 200, 200, false));
            }
            profile.setLayoutParams(new LinearLayout.LayoutParams(200, 200));

            profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent viewDate = new Intent(getApplicationContext(), ViewDateActivity.class);
                    if (datesByPreferenceOrder.get(v.getId()) != null) {
                        viewDate.putExtra("Date", datesByPreferenceOrder.get(v.getId()));
                        startActivity(viewDate);
                    }
                }
            });

            TextView name = new TextView(this);
            name.setText(d.getName());
            name.setLayoutParams(new LinearLayout.LayoutParams(200, 200));

            TextView age = new TextView(this);
            if (d.getAge() != -1)
                age.setText(d.getAge() + "yr");
            else
                age.setText("");
            age.setLayoutParams(new LinearLayout.LayoutParams(200, 200));

            TextView occupation = new TextView(this); //fine no check needed
            occupation.setText(d.getOccupation());
            occupation.setLayoutParams(new LinearLayout.LayoutParams(200, 200));

            TextView relRank = new TextView(this);
            relRank.setText(datesByPreferenceOrder.indexOf(d) + "");

            layout.addView(profile);
            layout.addView(name);
            layout.addView(age);
            layout.addView(occupation);
            dates.addView(layout);

            idSetter++;
        }

        Button newDate = (Button) findViewById(R.id.newDate);
        newDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newDateIntent = new Intent(getApplicationContext(), NewDateActivity.class);
                newDateIntent.putExtra("NumDates", datesByPreferenceOrder.size());
                newDateIntent.putExtra("Dates", datesByPreferenceOrder);
                startActivity(newDateIntent);
            }
        });

    }

    //index should be desired rank - 1 because of the 0 indexing
    public static void insertDateAsRank(int rank, Date d) {
        datesByPreferenceOrder.add(rank - 1, d);
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

    public static ArrayList<Date> getDateList() {
        return datesByPreferenceOrder;
    }
    public static boolean         dateListContains(Date d) { return datesByPreferenceOrder.contains(d); }

}
