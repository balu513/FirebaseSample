package com.example.basicdemo.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.basicdemo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FireBaseLoginActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;

    private static final String TAG = "FireBaseRegistraionActivity";
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        ((Button)findViewById(R.id.btnLogin)).setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.btnLogin:
                login();
                break;
            default:
                break;
        }

    }

    private void login() {

        String email = ((EditText)findViewById(R.id.txtEmail)).getText().toString();
        String pwd = ((EditText)findViewById(R.id.txtPwd)).getText().toString();
        showProgressDialog();

        mAuth.signInWithEmailAndPassword(email, pwd)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            dismissProgressDialog();
                            startActivity(new Intent(FireBaseLoginActivity.this, FirebaseDataBaseOperationsActiivty.class));

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            dismissProgressDialog();
                            Toast.makeText(FireBaseLoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });

    }

    private void showProgressDialog()
    {
        dialog = new ProgressDialog(this);
        dialog.setMessage("logging in, Please wait ..");
        dialog.show();
    }
    private void dismissProgressDialog()
    {
        if (dialog!=null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}

