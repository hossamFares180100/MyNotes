package com.example.mynotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.like.LikeButton;

public class ShowActivity extends AppCompatActivity {


    TextView title,note;
    String t,no;
    Intent n;
    TextInputLayout title_update,note_update;
    NoteDatabase database;
    private Menu menuList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        title=findViewById(R.id.title_show);
        note=findViewById(R.id.note_show);
        title_update=findViewById(R.id.note_title_update);
        note_update=findViewById(R.id.note_update);
        Toolbar toolbar=findViewById(R.id.myToolBar3);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("ShowNote");
        n=getIntent();
        database=new NoteDatabase(this);
        t=n.getStringExtra("title");
        no=n.getStringExtra("note");
        title.setText(t);
        note.setText(no);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
            menuList=menu;
        getMenuInflater().inflate(R.menu.show_item,menu);
        if(MainActivity.type.equals("note")){
            NoteMenu();
        }
        else if(MainActivity.type.equals("trash")){
         trashMenu();
        }
        else if(MainActivity.type.equals("archive")){
            archiveMenu();
        }
        else if(MainActivity.type.equals("favorite")){
            favoriteMenu();
        }
        else if(MainActivity.type.equals("locked")){
            lockedMenu();
        }
        return true;
    }
    private void trashMenu(){

        MenuItem item2=menuList.findItem(R.id.update);
        MenuItem item3=menuList.findItem(R.id.delete);
        MenuItem item4=menuList.findItem(R.id.archive);
        MenuItem item5=menuList.findItem(R.id.unpin_archive);
        MenuItem item6=menuList.findItem(R.id.unfavorite);
        item2.setVisible(false);
        item3.setVisible(true);
        item4.setVisible(false);
        item5.setVisible(false);
        item6.setVisible(false);

    }


    private void lockedMenu(){

        MenuItem item2=menuList.findItem(R.id.update);
        MenuItem item3=menuList.findItem(R.id.delete);
        MenuItem item4=menuList.findItem(R.id.archive);
        MenuItem item5=menuList.findItem(R.id.unpin_archive);
        MenuItem item6=menuList.findItem(R.id.unfavorite);
        item2.setVisible(false);
        item3.setVisible(false);
        item4.setVisible(false);
        item5.setVisible(false);
        item6.setVisible(false);

    }

    private void archiveMenu(){
        MenuItem item2=menuList.findItem(R.id.update);
        MenuItem item3=menuList.findItem(R.id.delete);
        MenuItem item4=menuList.findItem(R.id.archive);
        MenuItem item5=menuList.findItem(R.id.unpin_archive);
        MenuItem item6=menuList.findItem(R.id.unfavorite);
        item2.setVisible(false);
        item3.setVisible(false);
        item4.setVisible(false);
        item5.setVisible(true);
        item6.setVisible(false);
    }


    private void favoriteMenu(){
        MenuItem item2=menuList.findItem(R.id.update);
        MenuItem item3=menuList.findItem(R.id.delete);
        MenuItem item4=menuList.findItem(R.id.archive);
        MenuItem item5=menuList.findItem(R.id.unpin_archive);
        MenuItem item6=menuList.findItem(R.id.unfavorite);
        item2.setVisible(false);
        item3.setVisible(false);
        item4.setVisible(false);
        item5.setVisible(false);
        item6.setVisible(true);
    }

    private void NoteMenu(){
        MenuItem item2=menuList.findItem(R.id.update);
        MenuItem item3=menuList.findItem(R.id.delete);
        MenuItem item4=menuList.findItem(R.id.archive);
        MenuItem item5=menuList.findItem(R.id.unpin_archive);
        MenuItem item6=menuList.findItem(R.id.unfavorite);
        item2.setVisible(true);
        item3.setVisible(true);
        item4.setVisible(true);
        item5.setVisible(false);
        item6.setVisible(false);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.update){
            title.setVisibility(View.GONE);
            note.setVisibility(View.GONE);
            title_update.setVisibility(View.VISIBLE);
            note_update.setVisibility(View.VISIBLE);
            title_update.getEditText().setText(t);
            note_update.getEditText().setText(no);
        }

        else if(id==R.id.delete){
            if(MainActivity.type.equals("note")){
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setTitle("Delete").setIcon(R.drawable.ic_baseline_delete_24)
                    .setMessage("Are you sure you want to delete this note..?")
                    .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Note nit=database.findNote(n.getStringExtra("date"));
                            /*if(!nit.getPassword().equals("00000")){
                                LockedFragment.removeElement(nit);
                            }*/
                            database.deleteNote(nit);

                            n.putExtra("remove","deleted");
                            n.putExtra("archived","not archived");
                            n.putExtra("delete_deleted","not deleted");
                            n.putExtra("date",n.getStringExtra("date"));
                            setResult(RESULT_OK,n);
                            finish();
                        }
                    }).setNegativeButton("no", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    n.putExtra("remove","not deleted");
                    setResult(RESULT_OK,n);
                }
            }).show();
            }else if(MainActivity.type.equals("trash")){

                AlertDialog.Builder builder=new AlertDialog.Builder(this);
                builder.setTitle("Delete Forever").setIcon(R.drawable.ic_baseline_delete_forever_24)
                        .setMessage("Are you sure you want to delete this note forever..?")
                        .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Note nit=database.findDeletedNote(n.getStringExtra("date"));
                                database.deleteOneNote(nit);
                                n.putExtra("archived","archived");
                                n.putExtra("remove","not deleted");
                                n.putExtra("delete_deleted","not deleted");
                                n.putExtra("date",n.getStringExtra("date"));
                                setResult(RESULT_OK,n);
                                finish();
                            }
                        }).setNegativeButton("no", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        n.putExtra("remove","not deleted");
                        setResult(RESULT_OK,n);
                    }
                }).show();

            }


        }
        else if(id==R.id.archive){

            Note nit=database.findNote(n.getStringExtra("date"));
            database.ArchivedNote(nit);
            n.putExtra("archived","archived");
            n.putExtra("remove","not deleted");
            n.putExtra("delete_deleted","not deleted");
            n.putExtra("date",n.getStringExtra("date"));
            setResult(RESULT_OK,n);
            finish();

        }
        else if(id==R.id.unpin_archive){

            Note nit=database.findArchivedNote(n.getStringExtra("date"));
            database.unArchivedNote(nit);
            n.putExtra("archived","un archived");
            n.putExtra("remove","not deleted");
            n.putExtra("delete_deleted","not deleted");
            n.putExtra("date",n.getStringExtra("date"));
            setResult(RESULT_OK,n);
            finish();
        }
        else if(id==R.id.unfavorite){
            Note nit=database.findFavoriteNote(n.getStringExtra("date"));
            database.unFavoriteNote(nit);
            n.putExtra("archived","archived");
            n.putExtra("remove","not deleted");
            n.putExtra("delete_deleted","not deleted");
            n.putExtra("date",n.getStringExtra("date"));
            setResult(RESULT_OK,n);
            finish();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if(title_update.getVisibility()==View.VISIBLE) {
            String tit, not;
            tit = title_update.getEditText().getText().toString();
            not = note_update.getEditText().getText().toString();


            if (tit.equals(t) && not.equals(no)) {
                Toast.makeText(this, "same data not change", Toast.LENGTH_SHORT).show();
            } else {

                if (!tit.equals("") && !not.equals("")) {
                    Note n2 = new Note(tit, not, n.getStringExtra("date"), n.getStringExtra("pass"));
                    if (database.updateNote(n2)) {
                        Toast.makeText(this, "note updated successfully", Toast.LENGTH_SHORT).show();
                        n.putExtra("title", tit);
                        n.putExtra("note", not);
                        n.putExtra("date", n.getStringExtra("date"));
                        n.putExtra("remove","not deleted");
                        n.putExtra("delete_deleted","not deleted");
                        n.putExtra("archived","not archived");
                        setResult(RESULT_OK, n);
                    } else {
                        Toast.makeText(this, "note updated failed", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "note updated failed because of leak of data", Toast.LENGTH_SHORT).show();
                }
            }
        }
        super.onBackPressed();
    }
}