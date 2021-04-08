package com.example.note;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Toast;

import com.example.note.beans.Note;
import com.example.note.fragments.ListNotesFragment;
import com.example.note.fragments.NoteFragment;
import com.example.note.repo.NoteRepository;
import com.example.note.repo.NoteRepositoryFactory;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    protected boolean isLandscapeOrientation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkLandscape();
        showFragmentDependOrientation();

        initView();
    }

    private void initView() {
        Toolbar toolbar = initToolbar();
        initDrawer(toolbar);
    }

    private void initDrawer(Toolbar toolbar) {
        DrawerLayout drawer = findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toogle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toogle);
        toogle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            Toast.makeText(this, String.valueOf(id), Toast.LENGTH_SHORT).show();
            drawer.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    private Toolbar initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        return toolbar;
    }

    private void showFragmentDependOrientation() {
        Fragment fragment;
        if (isLandscapeOrientation) {
            NoteRepository repo = NoteRepositoryFactory.getInstance();
            Note note;
            int lastOpenedNote = ListNotesFragment.getLastOpenedNote();
            if (repo.getSize() > 0 && lastOpenedNote != -1) {
                note = repo.getNote(lastOpenedNote);
            } else {
                note = new Note();
            }
            fragment = NoteFragment.getInstance(note);
        } else {
            fragment = new ListNotesFragment();
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }

    private void checkLandscape() {
        isLandscapeOrientation = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }


}