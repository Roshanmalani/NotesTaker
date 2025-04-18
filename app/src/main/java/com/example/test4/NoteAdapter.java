package com.example.test4;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    private List<Note> noteList;
    private Context context;
    private OnNoteActionListener listener;

    public interface OnNoteActionListener {
        void onEdit(Note note);
        void onDelete(Note note);
    }

    public NoteAdapter(Context context, List<Note> noteList, OnNoteActionListener listener) {
        this.context = context;
        this.noteList = noteList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_note, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Note note = noteList.get(position);
        holder.textViewNote.setText(note.getContent());

        holder.buttonEdit.setOnClickListener(v -> listener.onEdit(note));
        holder.buttonDelete.setOnClickListener(v -> listener.onDelete(note));
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public void updateList(List<Note> newList) {
        noteList = newList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNote;
        Button buttonEdit, buttonDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewNote = itemView.findViewById(R.id.textViewNote);
            buttonEdit = itemView.findViewById(R.id.buttonEdit);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }
}
