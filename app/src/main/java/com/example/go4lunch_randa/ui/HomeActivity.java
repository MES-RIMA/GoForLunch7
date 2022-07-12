package com.example.go4lunch_randa.ui;

import static com.example.go4lunch_randa.utils.Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;
import static com.example.go4lunch_randa.utils.GetTodayDate.getTodayDate;
import static com.example.go4lunch_randa.utils.ShowToastSnack.showToast;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.go4lunch_randa.R;
import com.example.go4lunch_randa.api.firebase.RestaurantsHelper;
import com.example.go4lunch_randa.api.retrofit.GooglePlaceDetailsCalls;
import com.example.go4lunch_randa.api.retrofit.GooglePlaceSearchCalls;
import com.example.go4lunch_randa.api.retrofit.google_autocomplet.AutoCompleteCalls;
import com.example.go4lunch_randa.models.google_autocomplete_gson.AutoCompleteResult;
import com.example.go4lunch_randa.models.googleplaces_gson.ResultDetails;
import com.example.go4lunch_randa.models.googleplaces_gson.ResultSearch;
import com.example.go4lunch_randa.ui.restaurant_details.Restaurant_Details;
import com.example.go4lunch_randa.utils.DistanceTo;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, AutoCompleteCalls.Callbacks, GooglePlaceSearchCalls.Callbacks, GooglePlaceDetailsCalls.Callbacks, LocationListener {

    private final List<ResultDetails> mResultDetailsList = new ArrayList<>();
    // VIEWMODEL
    public SharedViewModel mShareViewModel;
    // LIVEDATA
    public MutableLiveData<List<ResultDetails>> mLiveData = new MutableLiveData<>();
    // FOR DESIGN
    private Toolbar toolbar;
    private ImageView imageProfileView;
    private TextView emailUser;
    private TextView nameUser;
    private ImageView backgroundView;
    private DrawerLayout drawerLayout;

    private int resultSize;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        // VIEWMODEL
        mShareViewModel = new ViewModelProvider(this).get(SharedViewModel.class);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


       RestaurantsHelper.deleteNotTodayBooking(getTodayDate());


        BottomNavigationView navView = findViewById(R.id.nav_view);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);


        // Configure Toolbar
        configureToolBar();

        // Configure Navigation Drawer
        configureDrawerLayout();
        configureNavigationView();


        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

        } else {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, location -> {
                if (location != null) {
                    double currentLatitude = location.getLatitude();
                    double currentLongitude = location.getLongitude();
                    mShareViewModel.updateCurrentUserPosition(new LatLng(currentLatitude, currentLongitude));
                    GooglePlaceSearchCalls.fetchNearbyRestaurants(this, mShareViewModel.getCurrentUserPositionFormatted());
                }
            });
        }

        this.updateView();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, location -> {
                    if (location != null) {
                        double currentLatitude = location.getLatitude();
                        double currentLongitude = location.getLongitude();
                        mShareViewModel.updateCurrentUserPosition(new LatLng(currentLatitude, currentLongitude));
                        GooglePlaceSearchCalls.fetchNearbyRestaurants(this, mShareViewModel.getCurrentUserPositionFormatted());
                    }
                });

            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return super.onCreateView(name, context, attrs);

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateView();
    }

    private void configureNavigationView() {
        NavigationView navigationView = findViewById(R.id.activity_main_nav_view);
        final View headerLayout = navigationView.getHeaderView(0);
        imageProfileView = headerLayout.findViewById(R.id.imageProfileView);
        emailUser = headerLayout.findViewById(R.id.emailUser);
        nameUser = headerLayout.findViewById(R.id.nameUser);
        backgroundView = headerLayout.findViewById(R.id.background_header_drawer);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Nullable
    private FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    private void signOutFirebase() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnSuccessListener(this, startLogin_AfterSignOut());
    }

    private OnSuccessListener<Void> startLogin_AfterSignOut() {
        return aVoid -> {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        };
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        // Gestion de la navigation au click sur la Navigation Drawer
        int id = item.getItemId();

        if (id == R.id.nav_logout)
            signOutFirebase();

        else if (id == R.id.nav_lunch) {
            RestaurantsHelper.getBooking(Objects.requireNonNull(getCurrentUser()).getUid(), getTodayDate()).addOnCompleteListener(this::onComplete);

        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


    // On récupère la toolbar
    private void configureToolBar() {
        toolbar = findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(toolbar);
    }

    // On récupère le DrawerLayout, avec lui et la toolbar on créé le menu hamburger
    private void configureDrawerLayout() {
        drawerLayout = findViewById(R.id.main_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_appbar, menu);
        return true;
    }

    private void updateView() {

        //Chargement infos user dans Navigation Drawer
        if (getCurrentUser() != null) {
            if (getCurrentUser().getPhotoUrl() != null) {
                Glide.with(this)
                        .load(Objects.requireNonNull(getCurrentUser()).getPhotoUrl())
                        .apply(RequestOptions.circleCropTransform())
                        .into(imageProfileView);
            } else {
                Glide.with(this).load(R.drawable.ic_anon_user_48dp).apply(RequestOptions.circleCropTransform()).into(imageProfileView);
            }
            if (getCurrentUser().getDisplayName() != null) {
                nameUser.setText(getCurrentUser().getDisplayName());
                String email = TextUtils.isEmpty(getCurrentUser().getEmail()) ? getString(R.string.email_not_found) : getCurrentUser().getEmail();
                emailUser.setText(email);
            } else {
                nameUser.setText(getCurrentUser().getEmail());
                nameUser.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                        getResources().getDimension(R.dimen.text_size_email_not_found));
                emailUser.setVisibility(View.GONE);
            }

            // Chargement fond d'écran en haut navigation drawer
            Glide.with(this)
                    .load(R.drawable.background_nav_header)
                    .apply(RequestOptions.bitmapTransform(new BlurTransformation(30)))
                    .into(backgroundView);
        }
    }

    @Override
    public void onResponse(@Nullable List<ResultSearch> resultSearchList) {

        mResultDetailsList.clear();

        // POUR LIMITER LES REQUETES ET PAS EPUISER LE CREDIT GOOGLE
//        for (int i = 0; i < 1; i++) {

        for (int i = 0; i < Objects.requireNonNull(resultSearchList).size(); i++) {
            GooglePlaceDetailsCalls.fetchPlaceDetails(this, resultSearchList.get(i).getPlaceId());
        }
        resultSize = resultSearchList.size();
    }

    @Override
    public void onResponse(@Nullable ResultDetails resultDetails) {
        assert resultDetails != null;
        int distance = (int) Math.round(DistanceTo.distanceTo(resultDetails, this));
        resultDetails.setDistance(distance);
        if (resultDetails.getTypes().contains("restaurant")) {
            mResultDetailsList.add(resultDetails);
            mLiveData.setValue(mResultDetailsList);
        } else {
            resultSize--;
        }
        if (mResultDetailsList.size() == resultSize) {
            mLiveData.setValue(mResultDetailsList);
        }
    }

    @Override
    public void onFailure() {
        showToast(getApplicationContext(), getResources().getString(R.string.no_restaurant_found), 1);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        mShareViewModel.updateCurrentUserPosition(new LatLng(currentLatitude, currentLongitude));
    }


    private void onComplete(Task<QuerySnapshot> bookingTask) {

        if (!bookingTask.isSuccessful()) {
            return;
        }
        if (!bookingTask.getResult().isEmpty()) {
            Map<String, Object> extra = new HashMap<>();
            for (QueryDocumentSnapshot booking : bookingTask.getResult()) {
                extra.put("PlaceDetailResult", booking.getData().get("restaurantId"));
            }
            Intent intent = new Intent(this, Restaurant_Details.class);
            for (String key : extra.keySet()) {
                String value = (String) extra.get(key);
                intent.putExtra(key, value);
            }
            startActivity(intent);
        } else {
            showToast(this, getResources().getString(R.string.no_restaurant_booked), 0);
        }
    }

    @Override
    public void onBackPressed() {
        // Handle back click to close menu
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    private void AutoCompleteToDetails(AutoCompleteResult autoCompleteResult) {
        mResultDetailsList.clear();
        for (int i = 0; i < autoCompleteResult.getPredictions().size(); i++) {
            GooglePlaceDetailsCalls.fetchPlaceDetails(this, autoCompleteResult.getPredictions().get(i).getPlaceId());
        }
    }

    public void googleAutoCompleteSearch(String query) {
        AutoCompleteCalls.fetchAutoCompleteResult(this, query, mShareViewModel.getCurrentUserPositionFormatted());
    }

    @Override
    public void onResponse(@Nullable AutoCompleteResult autoCompleteResult) {
        assert autoCompleteResult != null;
        resultSize = autoCompleteResult.getPredictions().size();
        AutoCompleteToDetails(autoCompleteResult);

    }

    public void searchByCurrentPosition() {
        GooglePlaceSearchCalls.fetchNearbyRestaurants(this, mShareViewModel.getCurrentUserPositionFormatted());
    }
}