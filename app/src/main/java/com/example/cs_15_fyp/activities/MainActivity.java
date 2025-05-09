import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.cs_15_fyp.R;
import com.example.cs_15_fyp.fragments.NotificationsFragment;
import com.example.cs_15_fyp.fragments.RestaurantSearchFragment;
import com.example.cs_15_fyp.fragments.UserProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);

        // Set default fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, new NotificationsFragment())
                .commit();
        }

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.menu_notifications:
                    selectedFragment = new NotificationsFragment();
                    break;
                case R.id.menu_search:
                    selectedFragment = new RestaurantSearchFragment();
                    break;
                case R.id.menu_profile:
                    selectedFragment = new UserProfileFragment();
                    break;
            }
            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, selectedFragment)
                    .commit();
            }
            return true;
        });
    }
}