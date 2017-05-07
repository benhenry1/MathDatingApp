package edu.upenn.mathapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NumberOfDatesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_of_dates);

        EditText number = (EditText) findViewById(R.id.numDatesDialogue);
        final int num = Integer.parseInt(number.getText().toString());

        Button loveButton = (Button) findViewById(R.id.lovePageButton);

        loveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loveIntent = new Intent(getApplicationContext(), DatingHomeActivity.class);
                loveIntent.putExtra("number",  num);
                startActivity(loveIntent);
            }
        });

    }
}
