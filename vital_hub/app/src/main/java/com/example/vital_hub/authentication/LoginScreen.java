package com.example.vital_hub.authentication;

import static com.example.vital_hub.client.spring.controller.Api.getJwt;
import static com.example.vital_hub.client.spring.controller.Api.initJwt;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vital_hub.MainActivity;
import com.example.vital_hub.R;
import com.example.vital_hub.authentication.FirstRegistInfo;
import com.example.vital_hub.client.spring.objects.AuthResponseObject;
import com.example.vital_hub.client.spring.objects.AuthResponseObject;
import com.example.vital_hub.home_page.HomePageActivity;
import com.example.vital_hub.test.TestMain;
import com.example.vital_hub.test.TestServer;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginScreen extends AppCompatActivity {
    public static SignInClient oneTapClient;
    private BeginSignInRequest signInRequest;
    private ActivityResultLauncher<IntentSenderRequest> activityResultLauncher;
    Button loginBtn;
    private static final String TAG = "Error";
    private static final int REQ_ONE_TAP = 2;  // Can be any integer unique to the Activity.
    private boolean showOneTapUI = true;
    TextView dev;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        loginBtn = findViewById(R.id.btnLogin);
        dev = findViewById(R.id.dev);
        TextView term = findViewById(R.id.about);

        term.setMovementMethod(LinkMovementMethod.getInstance());

        term.setLinkTextColor(Color.GREEN);

        SharedPreferences prefs = getSharedPreferences("UserData", MODE_PRIVATE);
        String email = prefs.getString("email", null);
        String name = prefs.getString("name", null);

        if (name != null) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("email", email);
            intent.putExtra("name", name);
            startActivity(intent);
            finish();
        }

        initGoogleSignInRequest();

        receiveGoogleCredentials();


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trySigningIn();
            }
        });

        dev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dev();
            }
        });
    }

    protected void initGoogleSignInRequest() {
        oneTapClient = Identity.getSignInClient(this);

        signInRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        // Your server's client ID, not your Android client ID.
                        .setServerClientId(getString(R.string.default_web_client_id))
                        // Only show accounts previously used to sign in.
                        .setFilterByAuthorizedAccounts(false).build())
                // Automatically sign in when exactly one credential is retrieved.
                .setAutoSelectEnabled(false).build();
    }

    protected void receiveGoogleCredentials() {
        activityResultLauncher =
                registerForActivityResult(new ActivityResultContracts.StartIntentSenderForResult(), new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        try {
                            SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(result.getData());
                            String idToken = credential.getGoogleIdToken();

                            Log.i("token", idToken);
                            if (idToken !=  null) {
                                sendTokenToServer(credential);
                            }
                        } catch (ApiException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    protected void trySigningIn() {
        oneTapClient.beginSignIn(signInRequest)
                .addOnSuccessListener(LoginScreen.this, new OnSuccessListener<BeginSignInResult>() {
                    @Override
                    public void onSuccess(BeginSignInResult result) {
                        IntentSenderRequest intentSenderRequest =
                                new IntentSenderRequest.Builder(result.getPendingIntent().getIntentSender()).build();
                        activityResultLauncher.launch(intentSenderRequest);
                    }
                })
                .addOnFailureListener(LoginScreen.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // No saved credentials found. Launch the One Tap sign-up flow, or
                        // do nothing and continue presenting the signed-out UI.
                        Log.d(TAG, e.getLocalizedMessage());
                    }
                });
    }

    protected void sendTokenToServer(SignInCredential credential) {
        String accessToken = credential.getGoogleIdToken();
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + accessToken);

        initJwt(headers);
        getJwt.enqueue(new Callback<AuthResponseObject>() {
            @Override
            public void onResponse(Call<AuthResponseObject> call, Response<AuthResponseObject> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(LoginScreen.this, "Failed to login. Code: " + response.code(), Toast.LENGTH_LONG).show();
                    return;
                }
                AuthResponseObject object = response.body();
                String jsonWebToken = object.getToken();
                String email = credential.getId();
                String name = credential.getDisplayName();
                String ava = String.valueOf(credential.getProfilePictureUri());

                Intent intent;
                if (object.getFirstSign()) {
                    intent = new Intent(LoginScreen.this, FirstRegistInfo.class);
                    intent.putExtra("email", email);
                    intent.putExtra("name", name);
                    intent.putExtra("jwt", jsonWebToken);
                    intent.putExtra("ava", ava);
                    startActivity(intent);
                }
                else {
                    intent = new Intent(LoginScreen.this, MainActivity.class);
                    SharedPreferences.Editor editor = getSharedPreferences("UserData", MODE_PRIVATE).edit();
                    editor.putString("jwt", jsonWebToken);
                    editor.putString("email", email);
                    editor.putString("name",name);
                    editor.apply();
                    intent.putExtra("email", credential.getId());
                    intent.putExtra("name", credential.getDisplayName());
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<AuthResponseObject> call, Throwable t) {
                Log.e("LoginError", t.getMessage());
            }
        });
    }

    protected void dev() {
        Intent intent = new Intent(LoginScreen.this, TestMain.class);
        startActivity(intent);
    }
}
