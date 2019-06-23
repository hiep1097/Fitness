package com.example.hoang.fitness.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.hoang.fitness.R;
import com.example.hoang.fitness.utils.AssetsUtil;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingActivity extends AppCompatActivity {
    @BindView(R.id.btn_logout)
    Button mLogout;
    @BindView(R.id.btn_share_link)
    Button mShareLink;
    @BindView(R.id.btn_share_image)
    Button mShareImage;
    private GoogleSignInClient mGoogleSignInClient;
    ShareDialog shareDialog;
    ShareLinkContent shareLinkContent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                logoutGoogle();
                Intent intent = new Intent(SettingActivity.this,SigninActivity.class);
                startActivity(intent);
                finish();
            }
        });

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

        if (isLoggedIn){
            mShareLink.setVisibility(View.VISIBLE);
            mShareImage.setVisibility(View.VISIBLE);
            mShareLink.setOnClickListener(l->shareLink());
            mShareImage.setOnClickListener(l->shareImage());
        } else {
            mShareLink.setVisibility(View.GONE);
            mShareImage.setVisibility(View.GONE);
        }
    }

    private void shareLink(){
        shareDialog = new ShareDialog(SettingActivity.this);
        if (shareDialog.canShow(ShareLinkContent.class)){
            shareLinkContent = new ShareLinkContent.Builder()
                    .setQuote("Ứng dụng luyện tập thể dục 7 MINUTES")
                    .setContentUrl(Uri.parse("https://play.google.com/store/apps/details?id=net.workoutinc.seven_7_minutes_workouts_challenge"))
                    .build();
            shareDialog.show(shareLinkContent);
        }
    }

    private void shareImage(){
        shareDialog = new ShareDialog(SettingActivity.this);
        Bitmap image = AssetsUtil.getBitmapFromAsset(this,"workout_pic/7m_abs.webp");
        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(image)
                .build();
        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();
        shareDialog.show(content);
    }

    private void logoutGoogle(){
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                    }
                });
    }
}
