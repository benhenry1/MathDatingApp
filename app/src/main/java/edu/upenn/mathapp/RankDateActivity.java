package edu.upenn.mathapp;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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


        //These all to be updated on each iter
        theirPicture = (ImageView) findViewById(R.id.theirPic);
        theirName    = (TextView)  findViewById(R.id.nameOther);
        theirAge     = (TextView)  findViewById(R.id.theirAge);
        theirHeight  = (TextView)  findViewById(R.id.theirHeight);
        theirWeight  = (TextView)  findViewById(R.id.theirWeight);
        theirOcc     = (TextView)  findViewById(R.id.theirOcc);

        //We can set the current dudes value once and leave it
        ImageView thisPicture = (ImageView) findViewById(R.id.thisPic);
        if (thisDate.getPicture() != null)
            thisPicture.setImageBitmap(
                    BitmapFactory.decodeFile(thisDate.getPicture().getAbsolutePath()));

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

        if (getEffectiveSize(dateList) == 0) {
            DatingHomeActivity.insertDateAsRank(1, thisDate);
        }
        mainCompareLoop(1);

        Button theirButton = (Button) findViewById(R.id.theirButton);
        Button thisButton  = (Button) findViewById(R.id.thisButton);

        theirButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (counter > dateList.size()) {
                    DatingHomeActivity.insertDateAsRank(counter,thisDate);
                    //TODO: AT some point, this will be when it calculates the r[] val and chooses a soulmate
                    //Unitl then, go back to dating home
                    Intent backHomeIntent = new Intent(getApplicationContext(), DatingHomeActivity.class);
                    startActivity(backHomeIntent);
                }
                counter = counter + 1;
                mainCompareLoop(counter);
            }
        });

        thisButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatingHomeActivity.insertDateAsRank(counter, thisDate);
                Intent backHomeIntent = new Intent(getApplicationContext(), DatingHomeActivity.class);
                startActivity(backHomeIntent);
            }
        });


    }

    //Iterate through
    private void mainCompareLoop(int count) {
    //If they choose this date the first time, they should be rank 1 so start counter at 1


        counter = count;
        Date otherDate = dateList.get(counter - 1); //its 0 indexed so subtract 1.

        if (otherDate == null) {
            DatingHomeActivity.insertDateAsRank(counter, thisDate);
            Intent homeIntent = new Intent(getApplicationContext(), DatingHomeActivity.class);
            startActivity(homeIntent);
            return;
        }
        //Update the date info

        theirName.setText(otherDate.getName());

        if (thisDate.getPicture() != null) {
            theirPicture.setImageBitmap
                    (BitmapFactory.decodeFile(thisDate.getPicture().getAbsolutePath()));
        } else {
            theirPicture.setImageResource(R.drawable.nopic);
        }


        if (thisDate.getAge() != -1) {
            theirAge.setText(thisDate.getAge());
        } else {
            theirAge.setText("");
        }

        theirHeight.setText(thisDate.getHeight()); //If theres no height itll push the empty string for us
        theirOcc.setText(thisDate.getOccupation());

        if (thisDate.getWeight() != -1) {
            theirWeight.setText(thisDate.getWeight() + "lb");
        } else { theirWeight.setText(""); }
    }

    public static int getEffectiveSize(ArrayList<Date> list) {
        int counter = 0;

        for (Date o : list) {
            if (o != null)
                counter++;
        }

        return counter;
    }
}
