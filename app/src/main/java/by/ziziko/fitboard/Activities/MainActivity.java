package by.ziziko.fitboard.Activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

import by.ziziko.fitboard.R;
import by.ziziko.fitboard.Entities.User;
import by.ziziko.fitboard.databinding.ActivityMainBinding;
import by.ziziko.fitboard.databinding.AppBarMainBinding;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    private boolean isAdmin;
    private String UserKey="User";
    private List<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getIntent().getExtras()!=null)
        {
            isAdmin = getIntent().getExtras().getBoolean("isAdmin");
        }

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);

        AppBarMainBinding appBar = binding.appBarMain;
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;


        if (!isAdmin)
        {
            appBar.toolbar.setVisibility(View.INVISIBLE);
            getSupportActionBar().hide();
        }
        else
        {
            appBar.toolbar.setVisibility(View.VISIBLE);
            getSupportActionBar().show();
        }

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}