package by.ziziko.fitboard.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import by.ziziko.fitboard.Animation;
import by.ziziko.fitboard.R;
import by.ziziko.fitboard.Entities.User;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private DatabaseReference mDb;
    private String UserKey="User";
    private List<User> users;
    private User User = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mDb = FirebaseDatabase.getInstance().getReference(UserKey);
        auth = FirebaseAuth.getInstance();
        auth.signOut();

        users = new ArrayList<>();

        mDb.get().addOnCompleteListener(task ->
        {
            for (DataSnapshot ds: task.getResult().getChildren())
            {
                users.add(ds.getValue(by.ziziko.fitboard.Entities.User.class));
            }
        });


        EditText email = findViewById(R.id.login);
        EditText password = findViewById(R.id.password);
        TextView loginButton = findViewById(R.id.loginButton);
        TextView registration = findViewById(R.id.registration);

        email.setOnClickListener(view -> Animation.Animate(loginButton, loginButton.getCurrentTextColor(), Color.BLACK));
        loginButton.setOnClickListener(view ->
        {
            if (!email.getText().toString().equals("") && !password.getText().toString().equals(""))
            {

                for(User user: users)
                {
                    if (user.getEmail().equals(email.getText().toString()))
                    {
                        User = user;
                    }
                }
                auth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(task -> {
                    if (task.isSuccessful())
                    {
                        Toast.makeText(this, User.getLogin() + User.getEmail() + User.getPassword(), Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("login", User.getLogin());
                        intent.putExtra("isAdmin", User.isAdmin());
                        intent.putExtra("isInBlackList", User.isInBlacklist());
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Не удалось войти", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        registration.setOnClickListener(view ->
        {
            Intent intent = new Intent(this, RegistrationActivity.class);
            startActivity(intent);
        });

    }

}