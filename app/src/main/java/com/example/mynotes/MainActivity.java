package com.example.mynotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputLayout;
import com.hsalf.smilerating.SmileRating;
import com.hsalf.smileyrating.SmileyRating;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    static String type="note";
    Toolbar toolbar;
    TextInputLayout password,confirm;
    NoteDatabase database;
    String pass="000005";
    private Menu menuList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.myToolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Notes");
        navigationView = findViewById(R.id.nav_view);
        database=new NoteDatabase(this);
        drawer = findViewById(R.id.myDrawer);
        navigationView.setNavigationItemSelectedListener(this);
        toggle = new ActionBarDrawerToggle(MainActivity.this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NoteFragment noteFragment = new NoteFragment();
        openFragment(noteFragment);

    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            closeDrawer();
        } else {
            super.onBackPressed();
        }

    }


    public void closeDrawer() {
        drawer.closeDrawer(GravityCompat.START);
    }


    public void openFragment(Fragment f) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fm_view, f);
        ft.commit();
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

    public void open(){
        ArchiveFragment archiveFragment = new ArchiveFragment();
        openFragment(archiveFragment);
        getSupportActionBar().setTitle("Archive");
        type = "archive";
        hideMenu();
    }







    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.Note) {
            NoteFragment noteFragment = new NoteFragment();
            openFragment(noteFragment);
            getSupportActionBar().setTitle("Notes");
            type = "note";
            hideMenu();
        } else if (id == R.id.Trash) {
            type = "trash";
            TrashFragment trashFragment = new TrashFragment();
            openFragment(trashFragment);
            showMenu();
           /* toolbar.setVisibility(View.GONE);
            ContextualMenu contextualMenu=new ContextualMenu();
            startSupportActionMode(contextualMenu);*/

            //Toast.makeText(this, type, Toast.LENGTH_SHORT).show();
            //onCreateOptionsMenu(R.menu.trash_menu);
            getSupportActionBar().setTitle("Trash");
        } else if (id == R.id.Archive) {
            final SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
            final SharedPreferences.Editor editor = pref.edit();
            if (pref.getString("password", "").equals("")) {
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

                        if ((!passwordValidation() | !confirmValidation()) || (!check())) {
                        } else {
                            String t = password.getEditText().getText().toString();
                            editor.putString("password", t);
                            editor.commit();
                            dialog.dismiss();
                            closeDrawer();
                            open();
                        }
                    }
                });


            } else if (!pref.getString("password", "").equals("")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                View v2 = getLayoutInflater().inflate(R.layout.write_password, null);
                final TextInputLayout password = v2.findViewById(R.id.write_password);
                final MaterialRippleLayout confirm = v2.findViewById(R.id.confirm);
                builder.setView(v2).setTitle("Password").setIcon(R.drawable.ic_baseline_vpn_key_24);
                final AlertDialog dialog = builder.create();
                dialog.show();
                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (password.getEditText().getText().toString().equals(pref.getString("password", ""))) {
                            dialog.dismiss();
                            open();
                        } else {
                            Toast.makeText(MainActivity.this, "error password", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }

                    }
                });

            } else {
                open();
            }

        } else if (id == R.id.Favorite) {
            FavoriteFragment favoriteFragment = new FavoriteFragment();
            openFragment(favoriteFragment);
            getSupportActionBar().setTitle("Favorite");
            type = "favorite";
            hideMenu();
        }
        else if(id==R.id.locked){
            LockedFragment lockedFragment=new LockedFragment();
            openFragment(lockedFragment);
            getSupportActionBar().setTitle("Locked");
            type = "locked";
            hideMenu();
        }
        else if(id==R.id.Feedback){

            Intent n=new Intent(this,FeedbackActivity.class);
            startActivity(n);

        }
        else if(id==R.id.Contact_us){

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            View v2 = getLayoutInflater().inflate(R.layout.contact_layout, null);
            MaterialRippleLayout face = v2.findViewById(R.id.facebook);
            MaterialRippleLayout tel = v2.findViewById(R.id.tele);
            builder.setView(v2).setTitle("Contact").setIcon(R.drawable.ic_phone);
            final AlertDialog dialog = builder.create();
            dialog.show();
            face.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent browse=new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/profile.php?id=100008427044648"));
                    startActivity(browse);
                    dialog.dismiss();
                    closeDrawer();

                }
            });

            tel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent call=new Intent(Intent.ACTION_DIAL);
                    call.setData(Uri.parse("tel:01114652236"));
                    startActivity(call);
                    dialog.dismiss();
                    closeDrawer();
                }
            });




        }


        closeDrawer();
        return true;
    }


    /*public class ContextualMenu implements ActionMode.Callback{

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.trash_menu,menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            mode.setTitle("Trash");
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mode.finish();
        }
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menuList=menu;
        getMenuInflater().inflate(R.menu.trash_menu,menu);
        hideMenu();
        return true;
    }

    private void hideMenu(){
        MenuItem item=menuList.findItem(R.id.delete_All);
        MenuItem item2=menuList.findItem(R.id.restore_all);
        item.setVisible(false);
        item2.setVisible(false);
    }

    private void showMenu(){
        MenuItem item=menuList.findItem(R.id.delete_All);
        MenuItem item2=menuList.findItem(R.id.restore_all);
        item.setVisible(true);
        item2.setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.delete_All){
         database.deleteAllDeletedNote();
         TrashFragment.notes.clear();
         TrashFragment.adapter.notifyDataSetChanged();
        }
        else if(id==R.id.restore_all){
           database.restoreAllDeletedNote();
            TrashFragment.notes.clear();
            TrashFragment.adapter.notifyDataSetChanged();

        }
        return true;
    }
}