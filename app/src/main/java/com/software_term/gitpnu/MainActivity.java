package com.software_term.gitpnu;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class MainActivity extends AppCompatActivity {

    Button m_signin_github_btn;
    Button m_signin_google_btn;
    GoogleSignInClient m_signin_google_client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        m_signin_google_btn = (Button)findViewById(R.id.signin_btn_google);
        m_signin_github_btn = (Button)findViewById(R.id.signin_btn_github);

        m_signin_google_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Configure sign-in to request the user's ID, email address, and basic
                // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .build();

                m_signin_google_client = GoogleSignIn.getClient(MainActivity.this, gso);
            }
        });

        m_signin_github_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
            }
        });
    }
}