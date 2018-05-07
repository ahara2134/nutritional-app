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

        yourButton = (Button) findViewById(R.id.button);

        yourButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(LoginActivity.this, Home.class));
            }
        });

        //Google Sign-in
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

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
            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
            startActivity(intent);
        }

        //Add click listener to Google sign-in
        findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(view.getId()) {
                    case R.id.sign_in_button:
                        signIn();
                        break;
                }
            }
        });

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

    //Sign in with Google
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    // This follows after the previous method
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    //handleSignIn Result method from previous method
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            firebaseAuthWithGoogle(account);

        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            System.out.println("signInResult:failed code=" + e.getStatusCode());
        }
    }

    // Exchange GoogleSignInAccount with a firebase credential, and authenticate with Firebase
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        final String authFailed = "Authentication Failed..";
        final String authSuccess = "Authentication was successful!";
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            Toast.makeText(LoginActivity.this, authSuccess, Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(LoginActivity.this, Home.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, authFailed, Toast.LENGTH_LONG).show();
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

                            //Close current activity
                            finish();

                            //Open userprofileActivity
                            Intent intent = new Intent(LoginActivity.this, Home.class);
                            startActivity(intent);
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