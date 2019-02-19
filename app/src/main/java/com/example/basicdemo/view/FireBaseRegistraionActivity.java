package com.example.basicdemo.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.basicdemo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FireBaseRegistraionActivity extends AppCompatActivity  implements View.OnClickListener {
    private FirebaseAuth mAuth;

    private static final String TAG = "FireBaseRegistraionActivity";
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ((Button)findViewById(R.id.btnLogin)).setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        ((TextView)findViewById(R.id.lnkLogin)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FireBaseRegistraionActivity.this, FireBaseLoginActivity.class));
            }
        });
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
                register();
                break;
                default:
                    break;
        }

    }

    private void register() {

        String name = ((EditText)findViewById(R.id.txtName)).getText().toString();
        String email = ((EditText)findViewById(R.id.txtEmail)).getText().toString();
        String pwd = ((EditText)findViewById(R.id.txtPwd)).getText().toString();
        showProgressDialog();

        mAuth.createUserWithEmailAndPassword(email, pwd)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            dismissProgressDialog();
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(FireBaseRegistraionActivity.this,"Register Successful",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(FireBaseRegistraionActivity.this, FireBaseLoginActivity.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            dismissProgressDialog();
                            Toast.makeText(FireBaseRegistraionActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void showProgressDialog()
    {
        dialog = new ProgressDialog(this);
        dialog.setMessage("Registering, Please wait ..");
        dialog.show();
    }
    private void dismissProgressDialog()
    {
        if (dialog!=null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
