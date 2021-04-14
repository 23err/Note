package com.example.note.fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
    private boolean isCardViewItem = false;
    private Fragment fragment;
    private int position = 0;


    public void setOnRemoveItemListener(OnRemoveItemListener onRemoveItemListener) {
        this.onRemoveItemListener = onRemoveItemListener;
    }

    public NotesListRVAdapter(Context context, List<Note> notes, Fragment fragment) {
        this.notes = notes;
        inflater = LayoutInflater.from(context);
        this.fragment = fragment;

    }

    public void setCardView(boolean isCardViewItem) {
        this.isCardViewItem = isCardViewItem;
    }

    public boolean getCardView() {
        return isCardViewItem;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int resItem;
        if (isCardViewItem) {
            resItem = R.layout.note_rv_item_card;
        } else {
            resItem = R.layout.note_rv_item_line;
        }
        View view = inflater.inflate(resItem, parent, false);
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
//        holder.itemView.setOnCreateContextMenuListener((contextMenu, view, contextMenuInfo) -> {
//
//            MenuItem menuItemShare = contextMenu.add(Menu.NONE, ID_REMOVE, Menu.NONE, R.string.share);
//            menuItemShare.setOnMenuItemClickListener(menuItem -> {
//                Toast.makeText(view.getContext(), "Поделились заметкой", Toast.LENGTH_SHORT).show();
//                return true;
//            });
//            MenuItem menuItemRemove = contextMenu.add(Menu.NONE, ID_REMOVE, Menu.NONE, R.string.remove);
//            menuItemRemove.setOnMenuItemClickListener(menuItem1 -> {
//                if (onRemoveItemListener != null) {
//                    onRemoveItemListener.onRemove(note);
//                }
//                return true;
//            });
//
//        });

    }

    public int getPosition() {
        return position;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public static final int MENU_ITEM_REMOVE = 999;
        final MaterialTextView tvName, tvDate, tvBody;

        public ViewHolder(@NonNull View view) {
            super(view);
            tvName = view.findViewById(R.id.tvName);
            tvDate = view.findViewById(R.id.tvDate);
            tvBody = view.findViewById(R.id.tvBody);
            registerContextMenu(view);
            setOnLongClick(view);

        }

        private void setOnLongClick(@NonNull View view) {
            view.setOnLongClickListener(view1 -> {
                position = getLayoutPosition();
                return false;
            });
        }

        private void registerContextMenu(@NonNull View view) {
            if (fragment != null) {
                fragment.registerForContextMenu(view);
            }
        }

        public void bind(Note note) {
            tvDate.setText(note.getFormatDate());
            if (tvBody != null) {
                tvBody.setText(note.getBody());
                tvName.setText(note.getName());
            } else {
                tvName.setText(note.toString());
            }
        }
    }

    public interface OnItemClickListener {
        void onClick(View view, Note note);
    }

    public interface OnRemoveItemListener {
        void onRemove(Note note);
    }


}
