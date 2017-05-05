package edu.upenn.mathapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ChoiceImpl.Date;

/**
 * Created by Ben on 5/5/2017.
 */

public class ViewDateActivity extends AppCompatActivity {
    private Date d;
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
            Bitmap img = BitmapFactory.decodeFile(d.getPicture().getAbsolutePath());
            pic.setImageBitmap(img);
        }

        TextView age    = (TextView) findViewById(R.id.dateAge);
        age.setText(d.getAge() + "yr");
        TextView height = (TextView) findViewById(R.id.dateHeight);
        height.setText(d.getHeight() + "in");
        TextView weight = (TextView) findViewById(R.id.dateWeight);
        weight.setText(d.getWeight() + "lb");
        TextView occ    = (TextView) findViewById(R.id.dateOccupation);
        occ.setText(d.getOccupation());


    }

}
