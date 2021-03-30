package com.example.mySuperMarket;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mySuperMarket.objects.User;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.mySuperMarket.Utils.GMAIL_SIGN_IN;
import static com.example.mySuperMarket.Utils.IS_LOGGED;
import static com.example.mySuperMarket.Utils.REGULAR_SIGN_IN;
import static com.example.mySuperMarket.Utils.USERS_COLLECTION;
import static com.example.mySuperMarket.Utils.USER_NAME;

public class Login extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private EditText userName,password;
    private Button signInButton,gmailSignIn;
    private int gmailCode=1;
    private List<AuthUI.IdpConfig> providers = Collections.singletonList(
            new AuthUI.IdpConfig.GoogleBuilder().build()
    );
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("התחברות");
        getSupportActionBar().setElevation(0);
        init();
        checkIfUserIsLoggedIn();
    }

    private void checkIfUserIsLoggedIn() {
        if(sharedPreferences.getBoolean(IS_LOGGED,false)){
            goToNextActivity();
        }

    }

    private void init() {
        sharedPreferences=getSharedPreferences(Utils.USER_DATA,MODE_PRIVATE);
        userName=findViewById(R.id.user_name);
        password=findViewById(R.id.password);
        signInButton=findViewById(R.id.signin_button);
        gmailSignIn=findViewById(R.id.gmail_sign_in);
        gmailSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginToFirebase(GMAIL_SIGN_IN);
            }
        });
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int userNameLength=userName.getText().toString().length();
                int passwordLength=password.getText().toString().length();

                if(userNameLength>=2&&passwordLength>=6){
                    loginToFirebase(REGULAR_SIGN_IN);
                } if(passwordLength<6){
                    password.setError("נא להכניס סיסמה תקינה- 6 תווים ומעלה.");
                } if(userNameLength<2){
                    userName.setError("יש להזין את השם המלא שלך.");
                }
            }
        });
    }

    public void loginToFirebase(int loginMethod){
        String userName=this.userName.getText().toString();
        String password=this.password.getText().toString();
        if(loginMethod==GMAIL_SIGN_IN){
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .build(),
                    gmailCode);
        }else if(loginMethod==REGULAR_SIGN_IN) {
            db.collection(USERS_COLLECTION).document(userName).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.getResult().exists()){
                        if(task.getResult().toObject(User.class).getPassword().equals(password)&&task.getResult().toObject(User.class).getUserName().equals(userName)){
                            goToNextActivity();
                        }else{
                            Toast.makeText(Login.this,"יש לבדוק את הסיסמה ושם המשתמש ולוודא שהפרטים שהוכנסו נכונים.",Toast.LENGTH_LONG).show();
                        }
                    }else
                        db.collection(USERS_COLLECTION).document(userName).set(new User(userName,password,new ArrayList<>())).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                goToNextActivity();
                            }
                        });
                    }

            });
        }
    }

    private void goToNextActivity() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(userName.getText().toString().length()>0){
            sharedPreferences.edit().putString(USER_NAME,userName.getText().toString()).apply();
        }else if(user!=null){
            sharedPreferences.edit().putString(USER_NAME,user.getUid()).apply();
        }

        sharedPreferences.edit().putBoolean(IS_LOGGED,true).apply();
        startActivity(new Intent(this,NavigationActivity.class));
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == gmailCode) {

            if (resultCode == RESULT_OK) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                db.collection(USERS_COLLECTION).document(user.getUid()).set(new User(user.getDisplayName(),user.getUid(),new ArrayList<>())).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        goToNextActivity();
                    }
                });
                goToNextActivity();
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
            }
        }
    }
}