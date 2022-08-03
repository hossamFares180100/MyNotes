package com.example.mynotes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecycleDeletedNote extends RecyclerView.Adapter<RecycleDeletedNote.DeletedNoteViewHolder> {
    ArrayList<Note> notes;
    RecycleInterface2 listener;


    public RecycleDeletedNote(ArrayList<Note>notes,RecycleInterface2 listener){
        this.notes=notes;
        notifyDataSetChanged();
        this.listener=listener;

    }
    @NonNull
    @Override
    public DeletedNoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecycleDeletedNote.DeletedNoteViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.deleted_layout,null,false),listener);
    }

    @Override
    public void onBindViewHolder(@NonNull DeletedNoteViewHolder holder, int position) {
        holder.title.setText(notes.get(position).getTitle());
        holder.date.setText(notes.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class DeletedNoteViewHolder extends RecyclerView.ViewHolder {
        TextView title,date;
        RecycleInterface2 listener;
        public DeletedNoteViewHolder(@NonNull View itemView, final RecycleInterface2 listener) {
            super(itemView);
            title=itemView.findViewById(R.id.tv_title2);
            date=itemView.findViewById(R.id.tv_date2);
            this.listener=listener;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(notes.get(getAdapterPosition()));
                }
            });
        }
    }

    public void filterList(ArrayList<Note>n){
        notes=n;
        notifyDataSetChanged();
    }
}
