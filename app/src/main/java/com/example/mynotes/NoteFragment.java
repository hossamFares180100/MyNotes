package com.example.mynotes;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static android.app.Activity.RESULT_OK;

public class NoteFragment extends Fragment implements RecycleInterface{



    RecyclerView recyclerView;
    ArrayList<Note>notes,search_note,favorite;
    RecycleNoteAdapter adapter;
    NoteDatabase database;
    TextInputLayout title,note,pass,search;
    NoteFragmentInterface i;
    public NoteFragment() {
        // Required empty public constructor
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notes=new ArrayList<>();
        favorite=new ArrayList<>();
        search_note=new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_note, container, false);
        FloatingActionButton fab = v.findViewById(R.id.fb_add_notes);
        database=new NoteDatabase(getContext());
        notes.addAll(database.getAllNotes());
        favorite.addAll(database.getFavoriteNotes());
        adapter=new RecycleNoteAdapter(notes,favorite,this);
        recyclerView = v.findViewById(R.id.rv_notes);
        search = v.findViewById(R.id.search);
        search.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }

        });

        setRecycle();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent n=new Intent(getContext(),WriteNoteActivity.class);
                startActivityForResult(n,1);

                /*AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                View v2 = getLayoutInflater().inflate(R.layout.button_dialog, null);
                title = v2.findViewById(R.id.note_title);
                note = v2.findViewById(R.id.tf_note);
                pass =v2.findViewById(R.id.tf_pass);
                MaterialRippleLayout add = v2.findViewById(R.id.btnAdd);
                builder.setView(v2).setTitle("Add Note").setIcon(R.drawable.ic_baseline_add_24);
                final AlertDialog dialog = builder.create();
                dialog.show();
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (!noteValidation() | !titleValidation() | !passValidation()) {
                        } else {
                            String t = title.getEditText().getText().toString();
                            String n = note.getEditText().getText().toString();
                            String p=pass.getEditText().getText().toString();
                            Calendar c = Calendar.getInstance();
                            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("EEEE hh:mm a ");
                            String d = sdf.format(c.getTime());
                            Note note = new Note( t, n, d,p);
                            if(database.insertNote(note)){
                                notes.add(note);
                                adapter.notifyDataSetChanged();
                            Toast.makeText(getActivity(), "added complete", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(getActivity(), "failed added", Toast.LENGTH_SHORT).show();
                            }
                            dialog.dismiss();
                        }
                    }
                });*/

            }
        });


        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==1&&resultCode==RESULT_OK) {
            String t = data.getStringExtra("title");
            String not = data.getStringExtra("note");
            if (not.equals("") || t.equals("")) {
                Toast.makeText(getActivity(), "failed added because of the leak of data", Toast.LENGTH_SHORT).show();
            } else {
                Calendar c = Calendar.getInstance();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("EEEE hh:mm:ss a ");
                String d = sdf.format(c.getTime());
                assert data != null;
                Note n = new Note(t, not, d, data.getStringExtra("pass"));
                if(!data.getStringExtra("pass").equals("00000")){
                    database.insertLockedNote(n);
                }
                if (database.insertNote(n)) {
                    notes.add(n);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(getActivity(), "added complete", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "failed added", Toast.LENGTH_SHORT).show();
                }
            }
        }
        else if(requestCode==2&&resultCode==RESULT_OK){

            if(data.getStringExtra("archived").equals("archived")) {

                for (Note note : notes) {
                    if (note.getDate().equals(data.getStringExtra("date"))) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        notes.remove(note);
                        adapter.notifyDataSetChanged();
                        break;
                    }


                }
            }

            else if(data.getStringExtra("remove").equals("deleted")) {


                    for (Note note : notes) {
                        if (note.getDate().equals(data.getStringExtra("date"))) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            notes.remove(note);
                            adapter.notifyDataSetChanged();
                            break;
                        }
                    }
            }
            else {
                for (Note note : notes) {
                    if (note.getDate().equals(data.getStringExtra("date"))) {
                        note.setTitle(data.getStringExtra("title"));
                        note.setNote(data.getStringExtra("note"));
                        break;
                    }
                }
            }
            adapter.notifyDataSetChanged();

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void filter(String item) {
        ArrayList<Note>NSearch=new ArrayList<>();
        for (Note n : notes) {
            if (n.getTitle().toLowerCase().contains(item.toString().toLowerCase())) {
                NSearch.add(n);
            }

        }

        adapter.filterList(NSearch);
    }

    public void setRecycle(){
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter.notifyDataSetChanged();
    }



    public boolean titleValidation(){
        String em=title.getEditText().getText().toString().trim();
        if(em.isEmpty())
        {
            title.setError("this field shouldn't be empty");
            return false;
        }
        else
        {
            title.setError(null);
            return true;
        }
    }

    public boolean noteValidation(){
        String em=note.getEditText().getText().toString().trim();
        if(em.isEmpty())
        {
            note.setError("this field shouldn't be empty");
            return false;
        }
        else
        {
            note.setError(null);
            return true;
        }
    }


    public boolean passValidation(){
        String em=pass.getEditText().getText().toString().trim();
        if(em.isEmpty())
        {
            note.setError("this field shouldn't be empty");
            return false;
        }
        else
        {
            note.setError(null);
            return true;
        }
    }

    @Override
    public void onClick(final Note n) {

        if(!n.getPassword().equals("00000")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            View v2 = getLayoutInflater().inflate(R.layout.write_password, null);
            final TextInputLayout password = v2.findViewById(R.id.write_password);
            final MaterialRippleLayout confirm = v2.findViewById(R.id.confirm);
            builder.setView(v2).setTitle("Password").setIcon(R.drawable.ic_baseline_vpn_key_24);
            final AlertDialog dialog = builder.create();
            dialog.show();
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (password.getEditText().getText().toString().equals(n.getPassword())) {
                        dialog.dismiss();
                        open(n);
                    }
                    else{
                        Toast.makeText(getActivity(), "error password", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }

                }
            });

        }
        else {
            open(n);
        }
    }


    public void open(Note n){
        Intent intent = new Intent(getActivity(), ShowActivity.class);
        intent.putExtra("title", n.getTitle());
        intent.putExtra("note", n.getNote());
        intent.putExtra("date", n.getDate());
        intent.putExtra("pass", n.getPassword());
        startActivityForResult(intent, 2);
    }

    @Override
    public void onLongClick(Note n) {

    }

    @Override
    public void likeButton(Note n) {
        database.favoriteNote(n);
    }

    @Override
    public void unLike(Note n) {
        database.unFavoriteNote(n);

    }
}