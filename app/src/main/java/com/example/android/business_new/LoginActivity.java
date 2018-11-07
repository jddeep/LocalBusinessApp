package com.example.android.business_new;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

    final static int RC_SIGN_IN  = 9001;
    private static final String TAG = "LoginActivity";

    private SignInButton Loginbtn;

    private FirebaseAuth auth;
    private GoogleSignInClient gClient;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    GoogleSignInOptions gso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        auth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!=null){
//                    startActivityForResult(new Intent(Intent.ACTION_PICK),0);
                    Intent shopPage = new Intent(LoginActivity.this, MainActivity.class);
                    shopPage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    shopPage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(shopPage);

                }
            }
        };


        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        gClient = GoogleSignIn.getClient(this, gso);

        Loginbtn = (SignInButton) findViewById(R.id.loginbtn);

        Loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(mAuthStateListener);
        FirebaseUser currentUser = auth.getCurrentUser();
        updateUI(currentUser);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try{
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthwithGoogle(account);

            }catch (ApiException e){
                Log.w(TAG, "Google sign in failed");
                updateUI(null);
            }
        }
    }

    private void firebaseAuthwithGoogle(GoogleSignInAccount acct){
        Log.d(TAG, "firebaseAuthwithGoogle:"+acct.getId());

        AuthCredential credential= GoogleAuthProvider.getCredential(acct.getIdToken(),null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = auth.getCurrentUser();
//                            startActivity(new Intent(MainActivity.this, ShopNames.class));
//                            finish();
                            updateUI(user);

                        }else {
                            Log.w(TAG, "signIn with Cred failed ", task.getException());
                            Toast.makeText(LoginActivity.this,"Authentication failed", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
    }

    private void signIn(){
        Intent signInIntent = gClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }



    private void updateUI(FirebaseUser user){

        if(user != null){

            Toast.makeText(LoginActivity.this, "WELCOME!", Toast.LENGTH_SHORT).show();

        }else {
            Toast.makeText(LoginActivity.this, "WELCOME BC!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
