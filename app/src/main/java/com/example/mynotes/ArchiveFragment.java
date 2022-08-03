package com.example.mynotes;

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
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class ArchiveFragment extends Fragment implements RecycleInterface2{

    RecyclerView recyclerView;
    ArrayList<Note> notes,search_note;
    RecycleDeletedNote adapter;
    NoteDatabase database;
    TextInputLayout search;

    public ArchiveFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notes=new ArrayList<>();
        search_note=new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.fragment_archive, container, false);
        search=v.findViewById(R.id.search3);
        database=new NoteDatabase(getContext());
        notes.addAll(database.getArchivedNotes());
        adapter=new RecycleDeletedNote(notes,this);
        recyclerView = v.findViewById(R.id.rv_notes3);
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


        return v;
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
        Intent intent=new Intent(getActivity(),ShowActivity.class);
        intent.putExtra("title",n.getTitle());
        intent.putExtra("note",n.getNote());
        intent.putExtra("date",n.getDate());
        intent.putExtra("pass",n.getPassword());
        startActivityForResult(intent,5);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==5&&resultCode==RESULT_OK){
            for (Note note : notes) {
                if (note.getDate().equals(data.getStringExtra("date"))) {
                    notes.remove(note);
                    adapter.notifyDataSetChanged();
                    break;
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onLongClick(Note n) {

    }
}