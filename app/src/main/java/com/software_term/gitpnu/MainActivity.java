package com.software_term.gitpnu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.software_term.gitpnu.api.AccessToken;
import com.software_term.gitpnu.api.GithubClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private String m_clientID = "6c0eecd346b825ffb473";
    private String m_clientSecret = "636601650dfc3c5814a9964ca0d650d5e8cc3173 ";
    private String m_redirectURL = "gitpnu://callback";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        Button signin_google_btn = (Button)findViewById(R.id.signin_btn_google);
        Button signin_github_btn = (Button)findViewById(R.id.signin_btn_github);

        signin_google_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .build();

                GoogleSignInClient account = GoogleSignIn.getClient(MainActivity.this, gso);
            }
        });

        signin_github_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/login/oauth/authorize" + "?client_id=" + m_clientID + "&scope=repo&redirect_uri=" + m_redirectURL));
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        Uri uri = getIntent().getData();
        if (uri != null && uri.toString().startsWith(m_redirectURL)) {
            String code = uri.getQueryParameter("code");
            Retrofit.Builder builder = new Retrofit.Builder()
                    .baseUrl("https://github.com/")
                    .addConverterFactory(GsonConverterFactory.create());
            Retrofit retrofit = builder.build();
            GithubClient client = retrofit.create(GithubClient.class);
            Call<AccessToken> accessTokenCall = client.getAccessToken(m_clientID, m_clientSecret, code);
            accessTokenCall.enqueue(new Callback<AccessToken>() {
                @Override
                public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                }
                @Override
                public void onFailure(Call<AccessToken> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "no!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}