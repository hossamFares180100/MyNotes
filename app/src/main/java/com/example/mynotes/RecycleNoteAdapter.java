package com.example.mynotes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.ArrayList;

public class RecycleNoteAdapter extends RecyclerView.Adapter<RecycleNoteAdapter.NoteViewHolder> {

    ArrayList<Note>notes,favorite;
    RecycleInterface listener;
    public RecycleNoteAdapter(ArrayList<Note>notes,ArrayList<Note>favorite,RecycleInterface listener){
        this.notes=notes;
        notifyDataSetChanged();
        this.listener=listener;
        this.favorite=favorite;

    }
    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_layout,null,false),listener);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        holder.title.setText(notes.get(position).getTitle());
        holder.date.setText(notes.get(position).getDate());

        for(Note n:favorite){

                if(n.getTitle().equals(holder.title.getText().toString())){
                    holder.like.setLiked(true);

                    break;
                }
            }



    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView title,date;
        RecycleInterface listener;
        LikeButton like;

        public NoteViewHolder(@NonNull View itemView, final RecycleInterface listener) {
            super(itemView);
            title=itemView.findViewById(R.id.tv_title);
            date=itemView.findViewById(R.id.tv_date);
            like=itemView.findViewById(R.id.fav_note);

            like.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {
                    listener.likeButton(notes.get(getAdapterPosition()));
                }

                @Override
                public void unLiked(LikeButton likeButton) {
                    listener.unLike(notes.get(getAdapterPosition()));
                }
            });

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
