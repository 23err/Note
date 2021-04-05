package com.example.note.fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.note.R;
import com.example.note.beans.Note;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

public class NotesListRVAdapter extends RecyclerView.Adapter<NotesListRVAdapter.ViewHolder> {
    private LayoutInflater inflater;
    private List<Note> notes;
    private OnItemClickListener listener;

    public NotesListRVAdapter(Context context, List<Note> notes){
        this.notes = notes;
        inflater = LayoutInflater.from(context);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.note_rv_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Note note = notes.get(position);
        holder.tvName.setText(note.toString());
        holder.tvDate.setText(note.getFormatDate());
        holder.view.setOnClickListener(view1 ->{
            if (listener != null) {
                listener.onClick(view1, note);
            }
        });

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        final MaterialTextView tvName, tvDate;
        final View view;
        public ViewHolder(@NonNull View view) {
            super(view);
            tvName = view.findViewById(R.id.tvName);
            tvDate = view.findViewById(R.id.tvDate);
            this.view = view;
        }
    }

    public interface OnItemClickListener {
        void onClick(View view, Note note);
    }


}
