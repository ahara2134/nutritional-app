package ca.infostages.infonut;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    EditText email, password;

    Button yourButton;

    String EmailHolder, PasswordHolder;

    Button Login;

    Boolean EditTextEmptyCheck;

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

                final String fill_field = getString(R.string.fill_field);

                // If EditTextEmptyCheck == true
                if(EditTextEmptyCheck)
                {
                    //then login function called
                    LoginFunction();
                } else {
                    //if false, display toast
                    Toast.makeText(LoginActivity.this, fill_field, Toast.LENGTH_LONG).show();
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
        firebaseAuth.signInWithEmailAndPassword(EmailHolder, PasswordHolder)
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

        //Get value from email's editText and fill into EmailHolder string
        EmailHolder = email.getText().toString().trim();

        //get value from password editText
        PasswordHolder = password.getText().toString().trim();

        //Check if both editText is emoty or not
        if(TextUtils.isEmpty(EmailHolder) || TextUtils.isEmpty(PasswordHolder))
        {
            //if empty, set value as false.
            EditTextEmptyCheck = false;
        } else {
            EditTextEmptyCheck = true;
        }
    }
}