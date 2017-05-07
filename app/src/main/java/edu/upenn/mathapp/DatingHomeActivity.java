package edu.upenn.mathapp;

import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
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
import java.io.*;
import com.example.ChoiceImpl.Date;
import com.example.ChoiceImpl.DateStoppingAlgorithm;

import java.util.ArrayList;

/**
 * Created by Ben on 5/3/2017.
 */

public class DatingHomeActivity extends AppCompatActivity {
    private static File dateFile = new File("dates.txt");

    private static boolean firstRun = true; //static so it keeps its state
    private boolean pauseforinput = true;
    public static DateStoppingAlgorithm stoppingAlgorithm;

    private final Context context = this;
    private TextView dateCount, datesTotal;

    public static int numAvailableDates = 10; //the NumCandidates used in the future

    private static ArrayList<Date> datesByPreferenceOrder;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dating_home);
        dateCount = (TextView) findViewById(R.id.dateCount);
        datesTotal= (TextView) findViewById(R.id.totalDates);
        //deleteLocalData(); //Deletes all dates, pics
        if (firstRun) {
            firstRun = false; //TODO: This only works over 1 app run. make global somehow. write to file?
            datesByPreferenceOrder = new ArrayList<Date>(numAvailableDates);
            readDatesFromFile(); //FirstRun still needs to exist so we know when to read dates from file. Everything else should be under the control of syd's code

            if (RankDateActivity.getEffectiveSize(datesByPreferenceOrder) == 0) { //We dont need this prompt if theres already dates in there
                promptForInput();

                //init arraylist with nulls apparently
                datesByPreferenceOrder.add(0, null);
                int i = 0;
                while (i < numAvailableDates - 1) {
                    datesByPreferenceOrder.add(datesByPreferenceOrder.size(), null);
                    i++;
                }
            }
        } else {
            writeDatesToFile();
        }
        printDatePref();
        //while(pauseforinput);


        dateCount.setText("Date Count: " + RankDateActivity.getEffectiveSize(datesByPreferenceOrder));
        datesTotal.setText("Total Dates: " + numAvailableDates);

        System.out.println(numAvailableDates);
        stoppingAlgorithm = new DateStoppingAlgorithm(numAvailableDates);
        stoppingAlgorithm.setNumDates(RankDateActivity.getEffectiveSize(datesByPreferenceOrder));
        /*********************Programmatically list all dates**********************/


        int idSetter = 0;
        for (Date d : datesByPreferenceOrder) {

            if (d == null) continue;
            LinearLayout dates = (LinearLayout) findViewById(R.id.dateList);
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
                    printDatePref();
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
                newDateIntent.putExtra("NumDates", RankDateActivity.getEffectiveSize(datesByPreferenceOrder));
                newDateIntent.putExtra("Dates", datesByPreferenceOrder);
                startActivity(newDateIntent);
            }
        });
    }


    //index should be desired rank - 1 because of the 0 indexing
    public static void insertDateAsRank(int rank, Date d) {
        datesByPreferenceOrder.add(rank - 1, d);
    }

    public static void printDatePref() {
        for(Date d : datesByPreferenceOrder) {
            if (d != null)
                System.out.print(d.getName() + " ");
            else
                System.out.print("null ");
        }
        System.out.println();
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
                                    userCanceled();
                                } else {
                                    userSubmit(Integer.parseInt(in));
                                }

                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                userCanceled();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    private void userCanceled() {
        Toast.makeText(getApplicationContext(),
                "We defaulted the choice to 10.",
                Toast.LENGTH_LONG)
                .show();
        numAvailableDates = 10;
        datesTotal.setText("Total Dates: " + numAvailableDates);
        pauseforinput = false;
        //continueRun();
    }

    private void userSubmit(int in) {
        if (in > 0)
            numAvailableDates = in;
        else {
            userCanceled();
            return;
        }

        datesTotal.setText("Total Dates: " + numAvailableDates);
        pauseforinput = false;
        //continueRun();
    }

    public static ArrayList<Date> getDateList() {
        return datesByPreferenceOrder;
    }
    public static boolean         dateListContains(Date d) { return datesByPreferenceOrder.contains(d); }
    public static int             getRankOf(Date d) { return datesByPreferenceOrder.indexOf(d); }

    public void readDatesFromFile() {

        File path = new File(getFilesDir() + "/dates.txt");
        //if (RankDateActivity.getEffectiveSize(datesByPreferenceOrder) == 0)
        //    return;
        try {
            FileInputStream fileIn =
                    new FileInputStream(path);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            System.out.println("READ READ READ");
            //I think this writes some nulls



            //This is going to clobber the existing arraylist so make a new one off the bat
            ArrayList<Date> newArr = new ArrayList<Date>(numAvailableDates);
            for(int i = 0; i < numAvailableDates; i++) {
                newArr.add(i, null);
            }

            for(int i = 0; i < numAvailableDates; i++) {
                Date r = (Date) in.readObject();
                if (r != null)
                    r.setPicture(recoverPicture(r));
                newArr.add(i, r);
            }

            in.close();
            fileIn.close();
            if (newArr != null)
                datesByPreferenceOrder = newArr;
            dateCount.setText("Date Count: " + RankDateActivity.getEffectiveSize(datesByPreferenceOrder));
            datesTotal.setText("Total Dates: " + numAvailableDates);
            //TODO: Set the numAvailDates and refresh array
            invalView();

        }catch(IOException i) {
            i.printStackTrace();

        }catch(ClassNotFoundException e) {
            e.printStackTrace();
        } finally {

        }
    }

    private void invalView() {
        LinearLayout layout = (LinearLayout) findViewById(R.id.datingHomeLayout);
        layout.invalidate();
    }

    private File recoverPicture(Date d) {
        if (d == null || !d.hasPicture())
            return null;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File parentDir = new File(storageDir.getAbsolutePath() + "/date" + d.getDateId());

;
        File[] files = parentDir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.getName().endsWith(".jpg") & file.getName().contains("date" + d.getDateId())) {
                    return file;
                }
            }
        }

        return null;


    }

    public void writeDatesToFile() {
        try {
            if (RankDateActivity.getEffectiveSize(datesByPreferenceOrder) == 0)
                return;
            File path = new File(getFilesDir() + "/dates.txt");
            FileOutputStream fileOut =
                    new FileOutputStream(path);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);

            //I think this writes some nulls
            for (int i = 0; i < numAvailableDates; i++) {
                Date d = null;
                if (!(RankDateActivity.getEffectiveSize(datesByPreferenceOrder) <= i))
                    d = datesByPreferenceOrder.get(i);
                out.writeObject(d);
            }


            out.close();
            fileOut.close();
            System.out.printf("Serialized data is saved in /tmp/dates.ser");
        }catch(IOException i) {
            i.printStackTrace();
        }
    }

    //Not referenced rn, but when called deletes all dates and pictures
    private void deleteLocalData() {
        File path = new File(getFilesDir() + "/dates.txt");
        File pics = new File(Environment.DIRECTORY_PICTURES);

        for (int i = 0; i < numAvailableDates; i++) {
            File fol = new File(pics.getAbsolutePath() + "/date" + i);
            fol.delete();
        }
        path.delete();
        pics.delete();

    }


}
