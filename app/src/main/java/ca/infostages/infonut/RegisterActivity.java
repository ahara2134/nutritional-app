package ca.infostages.infonut;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final int RC_SIGN_IN = 9001;
    GoogleSignInClient mGoogleSignInClient;
    EditText email, password;
    Button signUp;
    String emailHolder, pwHolder;
    ProgressDialog progressDialog;

    private FirebaseUser mUser;
    private DatabaseReference mDatabase;
    private FirebaseAuth firebaseAuth;
    Boolean editTextStatus;
    Boolean emailValid;
    Boolean passwordValid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = (EditText)findViewById(R.id.reg_email);
        password = (EditText)findViewById(R.id.reg_pw);
        signUp = (Button)findViewById(R.id.reg_btn);
        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        progressDialog = new ProgressDialog(RegisterActivity.this);

        //Google Sign-in
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

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

        //Button listener for SignUp button
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckEditTextIsEmptyOrNot();

                if(editTextStatus && emailValid && passwordValid) {
                    UserRegistrationFunction();
                } else if(!editTextStatus){
                    Toast.makeText(RegisterActivity.this, getString(R.string.fill_field), Toast.LENGTH_LONG).show();
                } else if(!emailValid) {
                    Toast.makeText(RegisterActivity.this, getString(R.string.email_error), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(RegisterActivity.this, getString(R.string.pw_error), Toast.LENGTH_LONG).show();
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
                            if(user != null) {
                                mUser = user;
                                writeNewUser(mUser);
                                Toast.makeText(RegisterActivity.this, getString(R.string.now_reg), Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(RegisterActivity.this, Home.class);
                                startActivity(intent);
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, authFailed, Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    //Checks if the text entered in both fields is valid
    public void CheckEditTextIsEmptyOrNot() {
        emailHolder = email.getText().toString().trim();
        pwHolder = password.getText().toString().trim();

        //Check that user filled out both fields
        if(TextUtils.isEmpty(emailHolder) || TextUtils.isEmpty(pwHolder)) {
            editTextStatus = false;
        } else {
            editTextStatus = true;
        }

        //Check that the user's password is more than 6 characters long
        if(pwHolder.length() < 6) {
            passwordValid = false;
        } else {
            passwordValid = true;
        }

        //Check if the user's Email contains the '@' symbol.
        int n = emailHolder.indexOf('@');
        if(n == -1) {
            emailValid = false;
        } else {
            emailValid = true;
        }
    }

    public void UserRegistrationFunction() {
        progressDialog.setMessage(getString(R.string.wait));
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(emailHolder, pwHolder)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            if(user != null) {
                                mUser = user;
                                writeNewUser(mUser);
                                Toast.makeText(RegisterActivity.this, getString(R.string.now_reg), Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(RegisterActivity.this, Home.class);
                                startActivity(intent);
                            }

                        } else {
                            Toast.makeText(RegisterActivity.this, getString(R.string.close_app), Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                });
    }

    private void writeNewUser(FirebaseUser user) {
        String uID = user.getUid();
        mDatabase.child("users").child(uID).child("display_name").setValue("false");
        mDatabase.child("users").child(uID).child("age").setValue("false");
        mDatabase.child("users").child(uID).child("gender").setValue("false");
        mDatabase.child("users").child(uID).child("plan").setValue("false");
    }
}
