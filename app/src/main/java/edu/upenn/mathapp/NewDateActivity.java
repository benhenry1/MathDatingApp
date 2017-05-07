package edu.upenn.mathapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.example.ChoiceImpl.Date;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static edu.upenn.mathapp.PhotoActivity.REQUEST_IMAGE_CAPTURE;
import static edu.upenn.mathapp.R.attr.height;
//import java.util.Date;
//import java.util.Locale;

/**
 * Created by Ben on 5/5/2017.
 */

public class NewDateActivity extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private File picture = null;

    private int numDates;
    private ArrayList<Date> dates; //To be passed onto next activity

    //Fields needed for validation (Name is the only req field
    EditText name, age, occ, height;
    ImageView displayedPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_date);
        numDates = (int) getIntent().getSerializableExtra("NumDates");
        dates = (ArrayList<Date>) getIntent().getSerializableExtra("Dates");

        Button takePic = (Button) findViewById(R.id.newPicButton);
        takePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickPicHelper();
            }
        });

        displayedPic = (ImageView) findViewById(R.id.newProfile);

        if ( picture != null ) {
            Bitmap b = BitmapFactory.decodeFile(picture.getAbsolutePath());
            displayedPic.setImageBitmap(b.createScaledBitmap(b, 125, 125, false));
        } else {
            displayedPic.setImageResource(R.drawable.nopic);
        }
        name = (EditText) findViewById(R.id.newName);

        age = (EditText) findViewById(R.id.newAge);

        occ = (EditText) findViewById(R.id.newOcc);

        height = (EditText) findViewById(R.id.newHeight);

        Button submit = (Button) findViewById(R.id.submitNewDate);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateForm()) {
                    submitForm();
                }
            }
        });


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    private boolean validateForm() {
        return !name.getText().toString().equals("");
    } //Only reqt is to have a name

    //Create the date object, move to relative ranking page
    private void submitForm() {
        Date d = new Date(name.getText().toString(), numDates + 1);

        String  agetext    = age.getText().toString(),
                heighttext = height.getText().toString(),
                occtext    = occ.getText().toString();
        if ( !agetext.equals("") ) {
            d.setAge(Integer.parseInt(agetext));
        }
        if ( !heighttext.equals("") ) {
            d.setHeight(heighttext);
        }
        if ( !occtext.equals("") ) {
            d.setOccupation(occtext);
        }

        if ( picture != null ) {
            d.setPicture(picture);
            d.setHasPicture(true);
        }

        Intent rankIntent = new Intent(getApplicationContext(), RankDateActivity.class);
        rankIntent.putExtra("Date", d);
        rankIntent.putExtra("Dates", dates); //Sorry for bad naming. Date is the newly created, dates is the list of all dates
        startActivity(rankIntent);
    }



    private void onClickPicHelper() {
        picture = dispatchTakePictureIntent("date" + (numDates + 1)); //date1
    }
    //Returns the photo file or Null if there was a problem. Call with foldername /datenum
    private File dispatchTakePictureIntent(String folderName) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile(folderName);
                //System.out.println(photoFile);
            } catch (IOException ex) {
                // Error occurred while creating the File
                Toast.makeText(getApplicationContext(),
                        "There was an error in creating the image. Please try again.",
                        Toast.LENGTH_LONG)
                        .show();

                return null;
            }

            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, "edu.upenn.mathapp", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                takePictureIntent.putExtra("date", folderName);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

            }
            return photoFile;
        }
        Toast.makeText(getApplicationContext(),
                "There was an error in creating the image. Please try again.",
                Toast.LENGTH_LONG)
                .show();
        return null;
    }

    private File createImageFile(String folderName) throws IOException {
        // Create an image file name

        String imageFileName = folderName;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File dateDir = new File(storageDir.getAbsolutePath() + "/" + folderName);
        dateDir.mkdir();
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                dateDir      /* directory */
        );
        System.out.println(imageFileName + " " + dateDir);
        // Save a file: path for use with ACTION_VIEW intents
        //mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.print(data == null);
        // Check which request we're responding to
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (resultCode == RESULT_OK) {
                Toast t = Toast.makeText(getApplicationContext(),
                        "Picture saved.",
                        Toast.LENGTH_SHORT);
                t.show();
                System.out.println(picture.getAbsolutePath());
                displayedPic.setImageBitmap(BitmapFactory.decodeFile(picture.getAbsolutePath()));
                displayedPic.invalidate();
            }
        }
    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("NewDate Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
