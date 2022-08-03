package com.example.mynotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.balysv.materialripple.MaterialRippleLayout;
import com.google.android.material.textfield.TextInputLayout;

public class WriteNoteActivity extends AppCompatActivity {

    TextInputLayout title,note,password,confirm;
    String pass="00000";
    private Menu mList;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_note);
        title = findViewById(R.id.note_title_write);
        note = findViewById(R.id.note_write);
        Toolbar toolbar=findViewById(R.id.myToolBar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("WriteNote");

        /*note.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                view.getParent().requestDisallowInterceptTouchEvent(true);
                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_UP:
                        view.getParent().requestDisallowInterceptTouchEvent(false);
                        break;

                }
                return false;
            }
        });*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mList=menu;
        getMenuInflater().inflate(R.menu.write_menu,menu);
        open();
        return true;
    }

    public void open(){
        MenuItem item=mList.findItem(R.id.lock_open);
        MenuItem item2=mList.findItem(R.id.lock_close);

        item.setVisible(true);
        item2.setVisible(false);
    }


    public void close(){
        MenuItem item=mList.findItem(R.id.lock_open);
        MenuItem item2=mList.findItem(R.id.lock_close);

        item.setVisible(false);
        item2.setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.lock_open){

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View v2 = getLayoutInflater().inflate(R.layout.button_dialog, null);
            password = v2.findViewById(R.id.note_password);
            confirm = v2.findViewById(R.id.confirm_password);
            MaterialRippleLayout add = v2.findViewById(R.id.btnAdd);
            builder.setView(v2).setTitle("Add Password").setIcon(R.drawable.ic_baseline_add_24);
            final AlertDialog dialog = builder.create();
            dialog.show();
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if ((!passwordValidation() | !confirmValidation()) || (!check()) ) {
                    } else {
                        String t = password.getEditText().getText().toString();
                        String n = confirm.getEditText().getText().toString();
                        pass=t;
                        dialog.dismiss();
                        close();
                    }
                }
            });


        }
        return true;
    }


    public boolean passwordValidation(){
        String em=password.getEditText().getText().toString().trim();
        if(em.isEmpty())
        {
            password.setError("this field shouldn't be empty");
            return false;
        }
        else
        {
            password.setError(null);
            return true;
        }
    }

    public boolean checkNum(){
        String em=password.getEditText().getText().toString().trim();
        if(em.length()>10)
        {
            password.setError("the password is very long");
            return false;
        }
        else
        {
            password.setError(null);
            return true;
        }
    }

    public boolean confirmValidation(){
        String em=confirm.getEditText().getText().toString().trim();
        if(em.isEmpty())
        {
            confirm.setError("this field shouldn't be empty");
            return false;
        }
        else
        {
            confirm.setError(null);
            return true;
        }
    }
    public boolean check(){
        String em=password.getEditText().getText().toString().trim();
        String em2=confirm.getEditText().getText().toString().trim();
        if(!em.equals(em2)){
            password.setError("the tow password don't match");
            confirm.setError("the tow password don't match");
            return false;
        }
        else{
            confirm.setError(null);
            password.setError(null);
            return true;

        }
    }

    @Override
    public void onBackPressed() {
        Intent n=getIntent();
        n.putExtra("title",title.getEditText().getText().toString());
        n.putExtra("note",note.getEditText().getText().toString());
        n.putExtra("pass",pass);
        setResult(RESULT_OK,n);
        super.onBackPressed();
    }
}