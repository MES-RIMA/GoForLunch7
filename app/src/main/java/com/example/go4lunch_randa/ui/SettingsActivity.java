package com.example.go4lunch_randa.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.go4lunch_randa.R;
import com.example.go4lunch_randa.api.firebase.UserHelper;
import com.example.go4lunch_randa.databinding.ActivitySettingsBinding;
import com.example.go4lunch_randa.utils.notifications.NotificationHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {
    protected SharedViewModel mSharedViewModel;


    private NotificationHelper mNotificationHelper;

    private ActivitySettingsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mSharedViewModel = new ViewModelProvider(this).get(SharedViewModel.class);
        configureToolbar();
        retrieveUserSettings();
        setListenerAndFilters();
        createNotificationHelper();
        setTitle(getString(R.string.settings_toolbar));
    }


    private void configureToolbar() {
        setSupportActionBar(binding.activityMainToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void retrieveUserSettings() {
        if (getCurrentUser() != null) {
            UserHelper.getWorkmatesCollection().document(getCurrentUser().getUid()).addSnapshotListener((documentSnapshot, e) -> {
                if (e != null) {
                    Log.e("TAG", "Listen failed.", e);
                    return;
                }
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    Log.e("TAG", "Current data: " + documentSnapshot.getData());
                    binding.settingsSwitch.setChecked(Objects.equals(Objects.requireNonNull(documentSnapshot.getData()).get("notification"), true));
                    if (Objects.equals(documentSnapshot.getData().get("notification"), true)) {
                        mNotificationHelper.scheduleRepeatingNotification();
                    }
                } else {
                    Log.e("TAG", "Current data: null");
                }
            });
        }
    }

    private void setListenerAndFilters() {
        binding.settingsSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isPressed() && buttonView.isChecked()) {
                UserHelper.updateUserSettings(Objects.requireNonNull(getCurrentUser()).getUid(), true);
                Toast.makeText(getApplication(), "NOTIFICATIONS ON", Toast.LENGTH_SHORT).show();
                mNotificationHelper.scheduleRepeatingNotification();


            } else if (!buttonView.isChecked()) {
                UserHelper.updateUserSettings(Objects.requireNonNull(getCurrentUser()).getUid(), false);
                Toast.makeText(getApplication(), "NOTIFICATIONS OFF", Toast.LENGTH_SHORT).show();
                mNotificationHelper.cancelAlarmRTC();
            }
        });
    }

    private void createNotificationHelper() {
        mNotificationHelper = new NotificationHelper(getBaseContext());

    }

    @Nullable
    private FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }
}