package com.software_term.gitpnu.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.software_term.gitpnu.R;
import com.software_term.gitpnu.adapter.RepoAdapter;
import com.software_term.gitpnu.api.GithubAPI;
import com.software_term.gitpnu.api.GithubClient;
import com.software_term.gitpnu.api.ImageDownloader;
import com.software_term.gitpnu.api.TextDownloader;
import com.software_term.gitpnu.model.GithubRepo;
import com.software_term.gitpnu.model.GithubRepoReadme;
import com.software_term.gitpnu.model.GithubUser;

import org.commonmark.node.Node;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import io.noties.markwon.Markwon;
import io.noties.markwon.ext.tables.TablePlugin;
import io.noties.markwon.image.ImageItem;
import io.noties.markwon.image.ImagesPlugin;
import io.noties.markwon.image.SchemeHandler;
import io.noties.markwon.image.data.DataUriSchemeHandler;
import io.noties.markwon.image.file.FileSchemeHandler;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private String m_token;
    ImageView avatarImg;
    TextView userNameTV;
    TextView followersTV;
    TextView followingTV;
    TextView logIn;
    TextView email;
    TextView readme;

    Bitmap myImage;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String token) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString("token", token);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        m_token = getArguments().getString("token");
        loadData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("ProfileFragment", "onCreateView");
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_profile, container, false);
        avatarImg = (ImageView)rootView.findViewById(R.id.avatar);
        userNameTV = (TextView)rootView.findViewById(R.id.username);
        followersTV = (TextView)rootView.findViewById(R.id.followers);
        followingTV = (TextView)rootView.findViewById(R.id.following);
        logIn = (TextView)rootView.findViewById(R.id.logIn);
        email = (TextView)rootView.findViewById(R.id.email);
        readme = (TextView)rootView.findViewById(R.id.readme);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_bottom_nav, menu);
    }

    private void loadData(){
        GithubClient apiService = GithubAPI.getClient().create(GithubClient.class);

        Call<GithubUser> call = apiService.getAuthroizedUser(String.format("token %s", m_token));
        call.enqueue(new Callback<GithubUser>() {
            @Override
            public void onResponse(Call<GithubUser> call, Response<GithubUser> response) {
                ImageDownloader task = new ImageDownloader();

                try {
                    myImage = task.execute(response.body().getAvatar()).get();

                } catch (Exception e) {

                    e.printStackTrace();

                }

                avatarImg.setImageBitmap(myImage);
                avatarImg.getLayoutParams().height=380;
                avatarImg.getLayoutParams().width=260;

                if(response.body().getName() == null){
                    userNameTV.setText("No name provided");
                } else {
                    userNameTV.setText("Username: " + response.body().getName());
                }

                followersTV.setText("Followers: " + response.body().getFollowers());
                followingTV.setText("Following: " + response.body().getFollowing());
                logIn.setText("LogIn: " + response.body().getLogin());

                if(response.body().getEmail() == null){
                    email.setText("No email provided");
                } else{
                    email.setText("Email: " + response.body().getEmail());
                }

                loadReadme(response.body().getLogin(), response.body().getLogin());
            }

            @Override
            public void onFailure(Call<GithubUser> call, Throwable t) {
                // Log error here since request failed
                Log.e("Repos", t.toString());
            }

        });
    }

    private void loadReadme(String username, String repo){
        GithubClient apiService = GithubAPI.getClient().create(GithubClient.class);
        Call<GithubRepoReadme> repoCall = apiService.getRepoReadme(username, repo, String.format("token %s", m_token));
        repoCall.enqueue(new Callback<GithubRepoReadme>() {
            @Override
            public void onResponse(Call<GithubRepoReadme> call, Response<GithubRepoReadme> response) {
                if (response.code() == 200)
                {
                    String readmeContents = null;
                    TextDownloader task = new TextDownloader();
                    try {
                        final Context context = getContext();
                        readmeContents = task.execute(response.body().getDownloadUrl()).get();

                        final Markwon markwon = Markwon.builder(context)
                                .usePlugin(ImagesPlugin.create(new ImagesPlugin.ImagesConfigure() {
                                    @Override
                                    public void configureImages(@NonNull ImagesPlugin plugin) {
                                        plugin.addSchemeHandler(DataUriSchemeHandler.create());
                                        plugin.addSchemeHandler(FileSchemeHandler.createWithAssets(context));
                                        // for example to return a drawable resource
                                        plugin.addSchemeHandler(new SchemeHandler() {
                                            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                                            @NonNull
                                            @Override
                                            public ImageItem handle(@NonNull String raw, @NonNull Uri uri) {

                                                // will handle URLs like `drawable://ic_account_24dp_white`
                                                final int resourceId = context.getResources().getIdentifier(
                                                        raw.substring("drawable://".length()),
                                                        "drawable",
                                                        context.getPackageName());

                                                // it's fine if it throws, async-image-loader will catch exception
                                                final Drawable drawable = context.getDrawable(resourceId);

                                                return ImageItem.withResult(drawable);
                                            }

                                            @NonNull
                                            @Override
                                            public Collection<String> supportedSchemes() {
                                                return Collections.singleton("drawable");
                                            }
                                        });
                                    }
                                }))
                                .usePlugin(TablePlugin.create(context))
                                .build();
                        // parse markdown to commonmark-java Node
                        final Node node = markwon.parse(readmeContents);
                        final Spanned markdown = markwon.render(node);
                        markwon.setParsedMarkdown(readme, markdown);
                        Log.d("loadReadme", "api load done");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<GithubRepoReadme> call, Throwable t) {
                // Log error here since request failed
                Log.e("Repos", t.toString());
            }
        });
    }
}