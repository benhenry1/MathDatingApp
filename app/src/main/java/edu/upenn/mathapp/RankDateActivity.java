package edu.upenn.mathapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ChoiceImpl.Date;

import java.util.ArrayList;

/**
 * Created by Ben on 5/5/2017.
 * Rank this date by comparing it alongside all your other dates in descending preference order.
 *
 * Sets up first comparison, then either increments counter and refreshes or breaks depeding on what button is pressed
 */

public class RankDateActivity extends AppCompatActivity {
    private Date thisDate;
    private ArrayList<Date> dateList;

    ImageView theirPicture;
    TextView theirName, theirAge, theirHeight, theirWeight, theirOcc;

    int counter = 1; //so the OnClickListeners can see the counter


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank_date);

        thisDate = (Date) getIntent().getSerializableExtra("Date");
        dateList = (ArrayList<Date>) getIntent().getSerializableExtra("Dates");
        System.out.println(thisDate);


        //These all to be updated on each iter
        theirPicture = (ImageView) findViewById(R.id.theirPic);
        theirName    = (TextView)  findViewById(R.id.nameOther);
        theirAge     = (TextView)  findViewById(R.id.theirAge);
        theirHeight  = (TextView)  findViewById(R.id.theirHeight);
        theirWeight  = (TextView)  findViewById(R.id.theirWeight);
        theirOcc     = (TextView)  findViewById(R.id.theirOcc);

        //We can set the current dudes value once and leave it
        ImageView thisPicture = (ImageView) findViewById(R.id.thisPic);
        if (thisDate.getPicture() != null) {
            Bitmap b = BitmapFactory.decodeFile(thisDate.getPicture().getAbsolutePath());
            thisPicture.setImageBitmap(b.createScaledBitmap(b, 100, 100, false));
        }

        //Must have a name
        TextView  thisName    = (TextView)  findViewById(R.id.nameThis);
        thisName.setText(thisDate.getName());

        TextView  thisAge     = (TextView)  findViewById(R.id.thisAge);
        if (thisDate.getAge() != -1)
            thisAge.setText(thisDate.getAge() + "yr");

        TextView  thisHeight  = (TextView)  findViewById(R.id.thisHeight);
        if (!thisDate.getHeight().equals(""))
            thisHeight.setText(thisDate.getHeight());

        TextView  thisWeight  = (TextView)  findViewById(R.id.thisWeight);
        if (thisDate.getWeight() != -1)
            thisWeight.setText(thisDate.getWeight() + "lb");

        TextView  thisOcc     = (TextView)  findViewById(R.id.thisOcc);
        if (!thisDate.getOccupation().equals(""))
            thisOcc.setText(thisDate.getOccupation());

        //Break back to home if this is the first date. Auto rank 1
        int numdates = getEffectiveSize(dateList);
        if (getEffectiveSize(dateList) == 0) {
            submit(1, thisDate);
            return;
        }

        mainCompareLoop(1);

        Button theirButton = (Button) findViewById(R.id.theirButton);
        Button thisButton  = (Button) findViewById(R.id.thisButton);

        theirButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counter > dateList.size()) {
                    submit(counter, thisDate);
                }
                counter = counter + 1;
                mainCompareLoop(counter);
            }
        });

        thisButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit(counter, thisDate);
            }
        });


    }

    //Iterate through
    private void mainCompareLoop(int count) {
    //If they choose this date the first time, they should be rank 1 so start counter at 1


        counter = count;
        Date otherDate = dateList.get(counter - 1); //its 0 indexed so subtract 1.

        if (otherDate == null) {
            submit(counter, thisDate);
            return;
        }
        //Update the date info

        theirName.setText(otherDate.getName());

        if (otherDate.getPicture() != null) {
            theirPicture.setImageBitmap
                    (BitmapFactory.decodeFile(otherDate.getPicture().getAbsolutePath()));
        } else {
            theirPicture.setImageResource(R.drawable.nopic);
        }


        if (otherDate.getAge() != -1) {
            theirAge.setText(otherDate.getAge() + "yr");
        } else {
            theirAge.setText("");
        }

        theirHeight.setText(otherDate.getHeight()); //If theres no height itll push the empty string for us
        theirOcc.setText(otherDate.getOccupation());

        if (thisDate.getWeight() != -1) {
            theirWeight.setText(otherDate.getWeight() + "lb");
        } else { theirWeight.setText(""); }
    }


    //The arraylist size counts NULL elements-- count the nonnull elements
    public static int getEffectiveSize(ArrayList<Date> list) {
        int nonNullCounter = 0;

        for (Date o : list) {
            if (o != null)
                nonNullCounter++;
        }

        return nonNullCounter;
    }


    private void submit(int rank, Date d) {
        Intent homeIntent = null;
        if ( !DatingHomeActivity.dateListContains(d) ) {
            DatingHomeActivity.insertDateAsRank(rank, d);

            if (DatingHomeActivity.stoppingAlgorithm.addNewDate(d, rank)
                    || (getEffectiveSize(dateList) >= DatingHomeActivity.numAvailableDates)) {//This returns whether or not this is the stopping element
                homeIntent = new Intent(this, ViewDateActivity.class);
                homeIntent.putExtra("Date", d);
                Toast.makeText(getApplicationContext(),"Congratulations! It turns out that " + d.getName() + " is your soulmate!", Toast.LENGTH_LONG);
            } else {
                homeIntent = new Intent(this, DatingHomeActivity.class);
            }

        }

        startActivity(homeIntent);
    }
}
