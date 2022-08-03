package com.example.mynotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.google.android.material.textfield.TextInputLayout;
import com.hsalf.smileyrating.SmileyRating;

public class FeedbackActivity extends AppCompatActivity {

    SmileyRating smiley;
    TextInputLayout feed;
    MaterialRippleLayout send;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        smiley=(SmileyRating) findViewById(R.id.smile_rating);
        feed=findViewById(R.id.feed);
        send=findViewById(R.id.send_feedback);


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!feed.getEditText().getText().toString().equals("")) {
                    SmileyRating.Type type = smiley.getSelectedSmiley();
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                    emailIntent.setData(Uri.parse("mailto:"));
                    emailIntent.setType("text/plain");
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"hossamalostora648@gmail.com"});
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "FeedBack");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "feedback rating = "+type+"\n\n\n"+"user feedback : \n"+feed.getEditText().getText().toString());
                    try {
                        startActivity(emailIntent);
                        finish();
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(FeedbackActivity.this, "not app found to perform this action", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(FeedbackActivity.this, "please enter your feedback first", Toast.LENGTH_SHORT).show();
                }


            }
        });



    }
}