package edu.upenn.mathapp;

import android.Manifest;
import android.content.Intent;
import android.gesture.Gesture;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ViewFlipper;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private ViewFlipper viewFlipper;
    private GestureDetector gestureDetector;
    private float lastX;
    private int visited = 0;
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

        FileInputStream fis = null;
        try {
            fis = openFileInput("hello.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (fis == null) {

        }
        else {
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            try {
                visited = Integer.parseInt(bufferedReader.readLine());
                numberOfDates = Integer.parseInt(bufferedReader.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        viewFlipper = (ViewFlipper) findViewById(R.id.viewflipper);
        gestureDetector = new GestureDetector(this.getApplicationContext(), new GestureTap());

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
                if (visited == 0) {
                    Intent loveIntent = new Intent(getApplicationContext(), DatingHomeActivity.class);
                    startActivity(loveIntent);

                    String filename = "visited";

                    FileOutputStream fos = null;
                    try {
                        fos = openFileOutput(filename, MODE_PRIVATE);
                        fos.write(1);
                        fos.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                //if have been visited
                Intent newloveIntent = new Intent(getApplicationContext(), NumberOfDatesActivity.class);
                newloveIntent.putExtra("number",  numberOfDates);
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









    /**********************Gesture Detection**********************/
    class GestureTap extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDoubleTap(MotionEvent e) {

            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            // TODO: handle tap here
            if ( viewFlipper.getCurrentView().getId() == R.id.love ) {
                //handle
            } else if ( viewFlipper.getCurrentView().getId() == R.id.stats ) {
                //handle
            }
            return true;
        }
    }

}
