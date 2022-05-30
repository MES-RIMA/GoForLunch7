package com.example.go4lunch_randa.api.firebase;

import static com.example.go4lunch_randa.utils.Constants.COLLECTION_BOOKING;
import static com.example.go4lunch_randa.utils.Constants.COLLECTION_LIKED_NAME;

import com.example.go4lunch_randa.models.Booking;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class RestaurantsHelper {
    // --- COLLECTION REFERENCE ---

    public static CollectionReference getBookingCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_BOOKING);
    }

    public static CollectionReference getLikedCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_LIKED_NAME);
    }

    // --- CREATE ---

    public static Task<DocumentReference> createBooking(String bookingDate, String userId, String restaurantPlaceId, String restaurantName) {
        Booking bookingToCreate = new Booking(bookingDate, userId, restaurantPlaceId, restaurantName);
        return RestaurantsHelper.getBookingCollection().add(bookingToCreate);
    }

    public static Task<Void> createLike(String restaurantId, String userId) {
        Map<String, Object> user = new HashMap<>();
        user.put(userId, true);
        return RestaurantsHelper.getLikedCollection().document(restaurantId).set(user, SetOptions.merge());
    }

    // --- GET ---

    public static Task<QuerySnapshot> getBooking(String userId, String bookingDate) {
        return RestaurantsHelper.getBookingCollection().whereEqualTo("userId", userId).whereEqualTo("bookingDate", bookingDate).get();
    }

    public static Task<QuerySnapshot> getTodayBooking(String restaurantPlaceId, String bookingDate) {
        return RestaurantsHelper.getBookingCollection().whereEqualTo("restaurantId", restaurantPlaceId).whereEqualTo("bookingDate", bookingDate).get();
    }

    public static Task<DocumentSnapshot> getLikeForThisRestaurant(String restaurantPlaceId) {
        return RestaurantsHelper.getLikedCollection().document(restaurantPlaceId).get();
    }

    public static Task<QuerySnapshot> getAllLikeByUserId(String userId) {
        return RestaurantsHelper.getLikedCollection().whereEqualTo(userId, true).get();
    }

    // --- DELETE ---

    public static Task<Void> deleteBooking(String bookingId) {
        return RestaurantsHelper.getBookingCollection().document(bookingId).delete();
    }

    public static Boolean deleteLike(String restaurantId, String userId) {
        RestaurantsHelper.getLikeForThisRestaurant(restaurantId).addOnCompleteListener(restaurantTask -> {
            if (restaurantTask.isSuccessful()) {
                Map<String, Object> update = new HashMap<>();
                update.put(userId, FieldValue.delete());
                RestaurantsHelper.getLikedCollection().document(restaurantId).update(update);
            }
        });
        return true;
    }

    public static Boolean deleteNotTodayBooking(String date) {
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        CollectionReference itemsRef = rootRef.collection("booking");
        Query query = itemsRef.whereNotEqualTo("bookingDate", date);
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (DocumentSnapshot document : task.getResult()) {
                    itemsRef.document(document.getId()).delete();
                }
            }
        });
        return true;
    }
}
