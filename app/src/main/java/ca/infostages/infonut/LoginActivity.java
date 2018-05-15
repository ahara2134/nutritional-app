package ca.infostages.infonut;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int RC_SIGN_IN = 9001;
    private EditText email, password;
    GoogleSignInClient mGoogleSignInClient;
    Button yourButton;
    String emailHolder, passwordHolder;
    Button Login;
    Boolean EditTextEmptyCheck;
    Boolean emailValid;
    ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private static final String APP_SHARED_PREFS = "ca.infostages.infonut";
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Assign ID to EditText
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);

        //Assign ID to button
        Login = (Button) findViewById(R.id.button_login);

        progressDialog = new ProgressDialog(LoginActivity.this);

        //Assign firebaseAuth instance to FirebaseAuth object
        firebaseAuth = FirebaseAuth.getInstance();

        //Check if user already logged in before and not logged out properly.
        if (firebaseAuth.getCurrentUser() != null) {
            //finish activity
            finish();

            //Open other activity
            Intent intent = new Intent(LoginActivity.this, Home.class);
            startActivity(intent);
        }

        //Add click listener on login button
        Login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //call method CheckEditTextIsEmptyorNot
                CheckEditTextIsEmptyOrNot();

                // If all fields were filled out properly
                if (EditTextEmptyCheck && emailValid) {
                    //then login function called
                    LoginFunction();
                } else if (!EditTextEmptyCheck) {
                    //if false, display toast
                    Toast.makeText(LoginActivity.this, getString(R.string.fill_field), Toast.LENGTH_LONG).show();
                } else{
                    Toast.makeText(LoginActivity.this, getString(R.string.text_error), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //Create signWithEmailandPassword method with firebaseAuth object
    public void LoginFunction() {

        final String wait = getString(R.string.wait);
        final String notFound = getString(R.string.not_found);
        //setup message in proress Dialog.
        progressDialog.setMessage(wait);

        //Show progress dialog
        progressDialog.show();

        //Call signinWithEmailandPassword function with firebase obj
        firebaseAuth.signInWithEmailAndPassword(emailHolder, passwordHolder)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //If task is successful
                        if(task.isSuccessful()) {

                            //Hide progress dialog
                            progressDialog.dismiss();

                            //Open userprofileActivity
                            Intent intent = new Intent(LoginActivity.this, Home.class);
                            startActivity(intent);

                            //Close current activity
                            finish();
                        } else {

                            //Hide Progress dialog
                            progressDialog.dismiss();

                            //Show toast message when email or pw not found in firebase
                            Toast.makeText(LoginActivity.this, notFound, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    //check if EditText is empty or not
    public void CheckEditTextIsEmptyOrNot() {
        emailHolder = email.getText().toString().trim();
        passwordHolder = password.getText().toString().trim();

        //Check if both editText is empty or not
        if(TextUtils.isEmpty(emailHolder) || TextUtils.isEmpty(passwordHolder))
        {
            //if empty, set value as false.
            EditTextEmptyCheck = false;
        } else {
            EditTextEmptyCheck = true;
        }

        //Check if the user's Email contains the '@' symbol.
        int n = emailHolder.indexOf('@');
        if(n == -1) {
            emailValid = false;
        } else {
            emailValid = true;
        }
    }
}