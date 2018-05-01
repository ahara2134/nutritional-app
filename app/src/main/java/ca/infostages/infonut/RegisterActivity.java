package ca.infostages.infonut;

import android.app.ProgressDialog;
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

public class RegisterActivity extends AppCompatActivity {

    EditText email, password;
    Button signUp;
    String emailHolder, pwHolder;
    ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    Boolean editTextStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = (EditText)findViewById(R.id.reg_email);
        password = (EditText)findViewById(R.id.reg_pw);
        signUp = (Button)findViewById(R.id.reg_btn);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(RegisterActivity.this);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckEditTextIsEmptyOrNot();

                if(editTextStatus) {
                    UserRegistrationFunction();
                } else {
                    Toast.makeText(RegisterActivity.this, getString(R.string.fill_field), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void CheckEditTextIsEmptyOrNot() {
        emailHolder = email.getText().toString().trim();
        pwHolder = password.getText().toString().trim();

        if(TextUtils.isEmpty(emailHolder) || TextUtils.isEmpty(pwHolder)) {
            editTextStatus = false;
        } else {
            editTextStatus = true;
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
                            Toast.makeText(RegisterActivity.this, getString(R.string.now_reg), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(RegisterActivity.this, getString(R.string.close_app), Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                });
    }
}
