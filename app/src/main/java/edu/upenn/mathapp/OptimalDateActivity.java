package edu.upenn.mathapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import android.content.Intent;

import com.example.ChoiceImpl.Date;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * Created by Ben on 5/7/2017.
 */

public class OptimalDateActivity extends AppCompatActivity {
    private Date d;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_optimal_date);
        d = (Date) getIntent().getSerializableExtra("Date");

        //Get all the buttons
        ImageView pic = (ImageView) findViewById(R.id.dateImage);
        if (d.getPicture() == null) {
            pic.setImageResource(R.drawable.nopic);
        } else {
            if (d.getPicture().exists()) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                Bitmap bitmap = BitmapFactory.decodeFile(d.getPicture().getAbsolutePath(), options);
                //pic.setImageBitmap(bitmap);


                //Bitmap img = BitmapFactory.decodeFile(d.getPicture().getAbsolutePath());
                //System.out.println(storageDir + " " + d.getPicture().getAbsolutePath());
                pic.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 125, 125, false));
            }
        }

        //Guaranteed to have a name, we dont need to check
        TextView name   = (TextView) findViewById(R.id.dateName);
        name.setText(d.getName());


        TextView age    = (TextView) findViewById(R.id.dateAge);
        if ( d.getAge() == -1 ) {
            age.setText("Unspecified");
        }
        else {
            age.setText(d.getAge() + "yr");
        }
        //If height is unset it will be the empty string
        TextView height = (TextView) findViewById(R.id.dateHeight);
        if ( d.getHeight().equals("") )
            height.setText("");
        else {
            height.setText(d.getHeight());
        }

        TextView occ    = (TextView) findViewById(R.id.dateOccupation);
        if ( d.getOccupation().equals("") )
            occ.setText("");
        else {
            occ.setText(d.getOccupation());
        }

        Button finish = (Button) findViewById(R.id.finishDatingButton);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backHomeIntent = new Intent(getApplicationContext(), MainActivity.class);
                backHomeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                //Show thanks toast
                Toast.makeText(getApplicationContext(), "Thanks for using the Math Of Choice Dating App", Toast.LENGTH_LONG).show();
                //Clear all date data
                deleteLocalData();
                resetApp();

                startActivity(backHomeIntent);
            }
        });



    }


    //Not referenced rn, but when called deletes all dates and pictures
    private void deleteLocalData() {
        File path = new File(getFilesDir() + "/dates.txt");
        File pics = new File(Environment.DIRECTORY_PICTURES);

        for (int i = 0; i < 100; i++) {
            File fol = new File(pics.getAbsolutePath() + "/date" + i);
            if (fol != null)
                fol.delete();
        }
        path.delete();
        pics.delete();

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


}
