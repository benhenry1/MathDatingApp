package edu.upenn.mathapp;

import android.Manifest;
import android.content.Intent;
import android.gesture.Gesture;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ViewFlipper;
import android.content.SharedPreferences;


import com.example.ChoiceImpl.Date;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ViewFlipper viewFlipper;
    private GestureDetector gestureDetector;
    private float lastX;
    private boolean visited = false;
    private int numberOfDates = 10;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] STORAGE_PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        viewFlipper = (ViewFlipper) findViewById(R.id.viewflipper);
        gestureDetector = new GestureDetector(this.getApplicationContext(), new GestureTap());
        readDataFromFile();

        Button statButton = (Button) findViewById(R.id.statPageButton);
        Button loveButton = (Button) findViewById(R.id.lovePageButton);

        statButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent statIntent = new Intent(getApplicationContext(), StatisticsHomeActivity.class);
                startActivity(statIntent);
            }
        });

        loveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //if have not been visited
                /*if (!visited) {
                    Intent loveIntent = new Intent(getApplicationContext(), NumberOfDatesActivity.class);
                    visited = true;
                    writeDataToFile();
                    startActivity(loveIntent);

                }*/
                //if have been visited
                Intent newloveIntent = new Intent(getApplicationContext(), DatingHomeActivity.class);
                newloveIntent.putExtra("number",  numberOfDates);
                writeDataToFile();
                startActivity(newloveIntent);
            }
        });


    }

    // Using the following method, we will handle all screen swaps.
    public boolean onTouchEvent(MotionEvent touchevent) {
        gestureDetector.onTouchEvent(touchevent);
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

    /************************READING AND WRITING DATA TO FILE****/
    public void readDataFromFile() {

        File path = new File(getFilesDir() + "/data.txt");
        //if (RankDateActivity.getEffectiveSize(datesByPreferenceOrder) == 0)
        //    return;
        try {
            FileInputStream fileIn =
                    new FileInputStream(path);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            //I think this writes some nulls
            visited = in.readBoolean();
            numberOfDates = in.readInt();

            in.close();
            fileIn.close();
        }catch(IOException i) {
            i.printStackTrace();
        }
    }

    public void writeDataToFile() {
        try {
            File path = new File(getFilesDir() + "/data.txt");
            FileOutputStream fileOut =
                    new FileOutputStream(path);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeBoolean(visited);
            out.writeInt(numberOfDates);
            out.close();
            fileOut.close();
        }catch(IOException i) {
            i.printStackTrace();
        }
    }

    //Not referenced rn, but when called deletes all dates and pictures
    private void resetApp() {
        try {
            File path = new File(getFilesDir() + "/data.txt");
            FileOutputStream fileOut =
                    new FileOutputStream(path);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeBoolean(false);
            out.writeInt(0);
            out.close();
            fileOut.close();
        }catch(IOException i) {
            i.printStackTrace();
        }

    }







    /**********************Gesture Detection**********************/
    class GestureTap extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDoubleTap(MotionEvent e) {

            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if ( viewFlipper.getCurrentView().getId() == R.id.love ) {
                //handle
            } else if ( viewFlipper.getCurrentView().getId() == R.id.stats ) {
                //handle
            }
            return true;
        }
    }



}
