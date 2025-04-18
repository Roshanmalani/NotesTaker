package com.example.test4;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NoteAdapter.OnNoteActionListener {

    private EditText editTextNote, editTextSearch;
    private Button buttonSave;
    private RecyclerView recyclerViewNotes;

    private NoteAdapter adapter;
    private DatabaseHelper dbHelper;
    private List<Note> allNotes = new ArrayList<>();
    private Note noteToEdit = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextNote = findViewById(R.id.editTextNote);
        editTextSearch = findViewById(R.id.editTextSearch);
        buttonSave = findViewById(R.id.buttonSave);
        recyclerViewNotes = findViewById(R.id.recyclerViewNotes);
        recyclerViewNotes.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DatabaseHelper(this);
        allNotes = dbHelper.getAllNotes();

        adapter = new NoteAdapter(this, allNotes, this);
        recyclerViewNotes.setAdapter(adapter);

        buttonSave.setOnClickListener(v -> {
            String content = editTextNote.getText().toString().trim();
            if (!content.isEmpty()) {
                if (noteToEdit == null) {
                    dbHelper.addNote(content);
                } else {
                    noteToEdit.setContent(content);
                    dbHelper.updateNote(noteToEdit);
                    noteToEdit = null;
                }
                editTextNote.setText("");
                refreshNotes();
            }
        });

        editTextSearch.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterNotes(s.toString());
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void afterTextChanged(Editable s) {}
        });
    }

    private void refreshNotes() {
        allNotes = dbHelper.getAllNotes();
        adapter.updateList(allNotes);
    }

    private void filterNotes(String query) {
        List<Note> filtered = new ArrayList<>();
        for (Note note : allNotes) {
            if (note.getContent().toLowerCase().contains(query.toLowerCase())) {
                filtered.add(note);
            }
        }
        adapter.updateList(filtered);
    }

    @Override
    public void onEdit(Note note) {
        noteToEdit = note;
        editTextNote.setText(note.getContent());
    }

    @Override
    public void onDelete(Note note) {
        dbHelper.deleteNote(note.getId());
        refreshNotes();
    }
}
