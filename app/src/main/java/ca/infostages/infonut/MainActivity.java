package ca.infostages.infonut;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    Button login, register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();

        //Check if user already logged in before and not logged out properly.
        if (firebaseAuth.getCurrentUser() != null) {
            //finish activity
            finish();

            //Open other activity
            Intent intent = new Intent(MainActivity.this, Home.class);
            startActivity(intent);
        }

        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null) {
            //finish activity
            finish();

            //Open other activity
            Intent intent = new Intent(MainActivity.this, Home.class);
            startActivity(intent);
        }

        //Associate Button object with XML Buttons
        login = (Button)findViewById(R.id.login);
        register = (Button) findViewById(R.id.register);

        //Set click listener for login
        login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent2 = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent2);
            }
        });
    }

    public void something(View view){
        Intent intent5 = new Intent(MainActivity.this, Home.class);
        startActivity(intent5);
    }
}
