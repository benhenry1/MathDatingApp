package edu.upenn.mathapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ChoiceImpl.Date;

import java.io.File;

/**
 * Created by Ben on 5/5/2017.
 */

public class ViewDateActivity extends AppCompatActivity {
    private Date d;

    //These bools will be used to give the user an option to provide more detail after the init screen
    private boolean hasAge = false, hasHeight = false, hasOcc = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_date);

        this.d = (Date) getIntent().getSerializableExtra("Date");

        //Find all the text views and update them

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
            hasAge = true;
        }
        //If height is unset it will be the empty string
        TextView height = (TextView) findViewById(R.id.dateHeight);
        if ( d.getHeight().equals("") )
            height.setText("");
        else {
            height.setText(d.getHeight());
            hasHeight = true;
        }

        TextView occ    = (TextView) findViewById(R.id.dateOccupation);
        if ( d.getOccupation().equals("") )
            occ.setText("");
        else {
            occ.setText(d.getOccupation());
            hasOcc = true;
        }

        //TODO: Give user buttons to add unadded parameters

    }

}
