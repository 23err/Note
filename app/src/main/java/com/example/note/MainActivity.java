package com.example.note;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.note.beans.Note;
import com.example.note.fragments.ListNotesFragment;
import com.example.note.fragments.NoteFragment;
import com.example.note.observe.Publisher;
import com.example.note.repo.NoteRepository;
import com.example.note.repo.NoteRepositoryFactory;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private boolean isLandscapeOrientation;

    private Publisher publisher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initPublisher();
        checkLandscape();
        showFragmentDependOrientation();
        initView();
    }



    private void initPublisher() {
        publisher = new Publisher();
    }

    public Publisher getPublisher(){
        return publisher;
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
            if (navigateFragment(id)) {
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
            return false;
        });

    }

    private boolean navigateFragment(int id) {
        switch (id) {
            case R.id.action_settings:
                showFragment(new SettingsFragment());
                return true;
            case R.id.action_about:
                showFragment(new AboutFragment());
                return true;
            case R.id.action_main:
                showFragment(new ListNotesFragment());
                return true;
        }
        return false;
    }

    private void showFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments.size() > 0) {
            Fragment currentFragment = fragments.get(fragments.size() - 1);
            return;
        }

        FragmentTransaction transaction = fragmentManager.beginTransaction()
                .setCustomAnimations(R.animator.slide_in_left, R.animator.slide_in_right, R.animator.slide_out_left, R.animator.slide_out_right)
                .replace(R.id.fragmentContainer, fragment);
        if (!isLandscapeOrientation) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
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
                note = repo.get(lastOpenedNote);
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