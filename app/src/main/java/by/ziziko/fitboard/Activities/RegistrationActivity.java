package by.ziziko.fitboard.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import by.ziziko.fitboard.Animation;
import by.ziziko.fitboard.Entities.User;
import by.ziziko.fitboard.R;

public class RegistrationActivity extends AppCompatActivity {

    private DatabaseReference mDb;
    private String UserKey="User";
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mDb = FirebaseDatabase.getInstance().getReference(UserKey);
        auth = FirebaseAuth.getInstance();
        auth.signOut();

        EditText login = findViewById(R.id.loginR);
        EditText password = findViewById(R.id.passwordR);
        EditText email = findViewById(R.id.emailR);
        TextView registration = findViewById(R.id.registrationR);

        login.setOnClickListener(view -> Animation.Animate(registration, registration.getSolidColor(), Color.BLACK));

        registration.setOnClickListener(view ->
                RegisterUser(email.getText().toString(), login.getText().toString(), password.getText().toString(), registration));


    }

    public void RegisterUser(String email, String login, String password, View view)
    {
        if (!login.equals("") && !password.equals("") && !email.equals(""))
        {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            auth.signInWithEmailAndPassword(email, password);
                            User newUser = new User(auth.getCurrentUser().getUid(), email, login, String.valueOf(password.hashCode()), false, false);
                            mDb.push().setValue(newUser);
                            Animation.Animate(view, view.getSolidColor(), Color.GRAY);
                            Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                            intent.putExtra("login", login);
                            intent.putExtra("isAdmin", newUser.isAdmin());
                            intent.putExtra("isInBlackList", newUser.isInBlacklist());
                            startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(), "Не удалось зарегистрироваться", Toast.LENGTH_LONG).show();
                            Animation.Animate(view, view.getSolidColor(), Color.RED);
                        }
                    }
                });
            }
        else
            Toast.makeText(this, "Заполните все поля!", Toast.LENGTH_LONG).show();
    }
}

