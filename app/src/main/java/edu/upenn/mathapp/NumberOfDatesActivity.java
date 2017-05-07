package edu.upenn.mathapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.ChoiceImpl.Date;

public class NumberOfDatesActivity extends AppCompatActivity {

    private EditText number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number_of_dates);

        EditText number = (EditText) findViewById(R.id.numDatesDialogue);
        this.number = number;

        Button loveButton = (Button) findViewById(R.id.lovePageButton);

        loveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateForm()) {
                    submitForm();
                }
            }
        });

    }

    private boolean validateForm() {
        return number.getText().toString().matches("\\d+");
    } //Only reqt is to have a name

    //Create the date object, move to relative ranking page
    private void submitForm() {

        final int num = Integer.parseInt(number.getText().toString());

        Intent loveIntent = new Intent(getApplicationContext(), DatingHomeActivity.class);
        loveIntent.putExtra("number",  num);
        startActivity(loveIntent);
    }

}
