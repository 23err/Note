package com.example.note.fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    public static final int ID_REMOVE = 999;
    private LayoutInflater inflater;
    private List<Note> notes;
    private OnItemClickListener onItemClickListener;
    private OnRemoveItemListener onRemoveItemListener;

    public void setOnRemoveItemListener(OnRemoveItemListener onRemoveItemListener) {
        this.onRemoveItemListener = onRemoveItemListener;
    }

    public NotesListRVAdapter(Context context, List<Note> notes) {
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
        holder.bind(note);
        holder.itemView.setOnClickListener(view1 -> {
            if (onItemClickListener != null) {
                onItemClickListener.onClick(view1, note);
            }
        });
        holder.itemView.setOnCreateContextMenuListener((contextMenu, view, contextMenuInfo) -> {

            MenuItem menuItemShare = contextMenu.add(Menu.NONE, ID_REMOVE, Menu.NONE, R.string.share);
            menuItemShare.setOnMenuItemClickListener(menuItem -> {
                Toast.makeText(view.getContext(), "Поделились заметкой", Toast.LENGTH_SHORT).show();
                return true;
            });
            MenuItem menuItemRemove = contextMenu.add(Menu.NONE, ID_REMOVE, Menu.NONE, R.string.remove);
            menuItemRemove.setOnMenuItemClickListener(menuItem1 -> {
                if (onRemoveItemListener != null) {
                    onRemoveItemListener.onRemove(note);
                }
                return true;
            });

        });

    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public static final int MENU_ITEM_REMOVE = 999;
        final MaterialTextView tvName, tvDate;

        public ViewHolder(@NonNull View view) {
            super(view);
            tvName = view.findViewById(R.id.tvName);
            tvDate = view.findViewById(R.id.tvDate);
        }

        public void bind(Note note) {
            tvName.setText(note.toString());
            tvDate.setText(note.getFormatDate());
        }
    }

    public interface OnItemClickListener {
        void onClick(View view, Note note);
    }

    public interface OnRemoveItemListener {
        void onRemove(Note note);
    }


}
