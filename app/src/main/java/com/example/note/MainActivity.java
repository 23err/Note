package com.example.note;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.note.beans.Note;
import com.example.note.fragments.ListNotesFragment;
import com.example.note.fragments.NoteBottomFragment;
import com.example.note.fragments.NoteFragment;
import com.example.note.observe.Publisher;
import com.example.note.repo.NoteRepository;
import com.example.note.repo.NoteRepositoryFactory;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;


public class MainActivity extends AppCompatActivity {

    public static final int RC_AUTH_GOOGLE = 7777;
    public static final String TAG = "MainActivity";
    private boolean isLandscapeOrientation;
    private Publisher publisher;
    private AppCompatImageView ivAva;
    private TextView tvUserName;
    private TextView tvMail;
    private SignInButton signInButton;
    private ImageView signOutButton;
    private GoogleSignInClient signInClient;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private LinearLayout profileLayout;
    private GoogleSignInAccount account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initPublisher();
        checkLandscape();
        showFragmentDependOrientation();
        initGoogleSignInClient();
        initView();
        initializeAccount();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_AUTH_GOOGLE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            account = task.getResult();
            updateNav(account);
        }
    }

    private void initializeAccount() {
        account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        updateNav(account);
    }

    private void updateNav(GoogleSignInAccount account) {
        if (account != null) {
            tvUserName.setText(account.getDisplayName());
            tvMail.setText(account.getEmail());
            enableSignIn(false);
            new DownLoadImageTask(ivAva).execute(account.getPhotoUrl().toString());
        } else {
            tvUserName.setText("");
            tvMail.setText("");
            ivAva.setImageBitmap(null);
            enableSignIn(true);
        }
    }

    private void enableSignIn(boolean enable) {
        if (enable) {
            profileLayout.setVisibility(View.INVISIBLE);
            signOutButton.setVisibility(View.INVISIBLE);
            signInButton.setVisibility(View.VISIBLE);
        } else {
            profileLayout.setVisibility(View.VISIBLE);
            signOutButton.setVisibility(View.VISIBLE);
            signInButton.setVisibility(View.INVISIBLE);
        }

    }


    private void initGoogleSignInClient() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getResources().getString(R.string.client_id_google))
                .build();
        signInClient = GoogleSignIn.getClient(getApplicationContext(), gso);
    }


    private void initPublisher() {
        publisher = new Publisher();
    }

    public Publisher getPublisher() {
        return publisher;
    }


    private void initView() {
        Toolbar toolbar = initToolbar();
        initDrawer(toolbar);
        View view = navigationView.getHeaderView(0);
        ivAva = view.findViewById(R.id.ivAvatar);
        tvUserName = view.findViewById(R.id.tvUserName);
        tvMail = view.findViewById(R.id.tvMail);
        signInButton = view.findViewById(R.id.sign_in_button);
        signOutButton = view.findViewById(R.id.sign_out_button);
        profileLayout = view.findViewById(R.id.profile);
        signButtonsInitialize();
    }

    private void signButtonsInitialize() {
        signInButton.setOnClickListener(view1 -> {
            Intent signInIntent = signInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_AUTH_GOOGLE);
        });

        signOutButton.setOnClickListener(view1 -> {
            signInClient.signOut();
            account = null;
            updateNav(account);
        });
    }

    private void initDrawer(Toolbar toolbar) {
        drawer = findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toogle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toogle);
        toogle.syncState();
        navigationView = findViewById(R.id.nav_view);
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
                showFragment(new SettingsFragment(), false);
                return true;
            case R.id.action_about:
                showFragment(new AboutFragment(),false);
                return true;
            case R.id.action_main:
                showFragment(new ListNotesFragment(), false);
                return true;
        }
        return false;
    }

    private void showFragment(Fragment fragment, boolean setCustomAnimation) {
        new Navigation(this).setCustomAnimation(setCustomAnimation).showFragment(fragment, !isLandscapeOrientation);
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
                fragment = NoteFragment.getInstance(note);
            } else {
                fragment = NoteFragment.getInstance();
            }

        } else {
            fragment = new ListNotesFragment();
//            fragment = new StartFragment();
        }

        showFragment(fragment, true);
    }

    private void checkLandscape() {
        isLandscapeOrientation = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }




}