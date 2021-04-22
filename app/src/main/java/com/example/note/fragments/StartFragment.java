package com.example.note.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.note.DownLoadImageTask;
import com.example.note.Navigation;
import com.example.note.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class StartFragment extends Fragment {

    public static final int RC_SIGN_IN = 40404;
    public static final String TAG = "GoogleAuth";
    private SignInButton buttonSignIn;
    private MaterialButton buttonSignOut;
    private TextView emailView;
    private GoogleSignInClient googleSignInClient;
    private Button continue_;
    private AppCompatImageView ivAva;

    public static StartFragment newInstance() {
        StartFragment fragment = new StartFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_start, container, false);
        initGoogleSign();
        initView(view);
        enableSign();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getContext());
        if (account != null) {
            disableSign();
            updateUI(account.getEmail(), account.getPhotoUrl().toString());
            Toast.makeText(getContext(), account.getDisplayName(), Toast.LENGTH_SHORT).show();
            Log.d(TAG, account.getDisplayName());
            Log.d(TAG, String.valueOf(account.getPhotoUrl()));
            Log.d(TAG, String.valueOf(account.getEmail()));

//            if (avatarUrl != null) {
//                new DownLoadImageTask(ivAva).execute(avatarUrl);
//            }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void initView(View view) {
        buttonSignIn = view.findViewById(R.id.sign_in_button);
        buttonSignIn.setOnClickListener(view1 -> signIn());
        emailView = view.findViewById(R.id.email);
        continue_ = view.findViewById(R.id.continue_);
        ivAva = view.findViewById(R.id.ivAvatar);
        continue_.setOnClickListener(view1 -> {
            ListNotesFragment fragment = new ListNotesFragment();
            Navigation navigation = new Navigation(requireActivity());
            navigation.showFragment(fragment, true);
            Toast.makeText(getActivity(), "navigate to next fragment", Toast.LENGTH_SHORT).show();
        });
        buttonSignOut = view.findViewById(R.id.sign_out_button);
        buttonSignOut.setOnClickListener(view1 -> singOut());
    }


    private void initGoogleSign() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.client_id_google))
                .build();
        googleSignInClient = GoogleSignIn.getClient(getContext(), gso);
    }


    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
        buttonSignOut.setEnabled(false);
    }

    private void singOut() {
        googleSignInClient.signOut()
                .addOnCompleteListener(task -> {
                    updateUI("", null);
                    enableSign();
                });
    }

    private void enableSign() {
        buttonSignIn.setEnabled(true);
        continue_.setEnabled(false);
        buttonSignOut.setEnabled(false);
    }

    private void disableSign() {
        buttonSignIn.setEnabled(false);
        continue_.setEnabled(true);
        buttonSignOut.setEnabled(true);
    }

    private void updateUI(String email, String urlAvatar) {
        emailView.setText(email);
        if (urlAvatar != null) {
            new DownLoadImageTask(ivAva).execute(urlAvatar);
        } else {
            ivAva.setImageBitmap(null);
        }

    }

    private void handleSignInResult(Task<GoogleSignInAccount> completeTask) {
        try {
            GoogleSignInAccount account = completeTask.getResult(ApiException.class);
            disableSign();
            updateUI(account.getEmail(), account.getPhotoUrl().toString());
        } catch (ApiException e) {
            Log.w(TAG, "signInResult: failed code=" + e.getStatusCode());
        }
    }


}