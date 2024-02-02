package it.unimib.buildyourholiday.data.source.travel;

import static it.unimib.buildyourholiday.util.Constants.FIREBASE_REALTIME_DATABASE;
import static it.unimib.buildyourholiday.util.Constants.FIREBASE_SAVED_TRAVELS_COLLECTION;
import static it.unimib.buildyourholiday.util.Constants.FIREBASE_USERS_COLLECTION;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import it.unimib.buildyourholiday.model.Travel;

public class SavedTravelDataSource extends BaseSavedTravelDataSource {
    private static final String TAG = SavedTravelDataSource.class.getSimpleName();
    private final DatabaseReference databaseReference;
    private final String idToken;

    public SavedTravelDataSource(String idToken) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(FIREBASE_REALTIME_DATABASE);
        databaseReference = firebaseDatabase.getReference().getRef();
        this.idToken = idToken;
    }

    @Override
    public void getSavedTravels() {
        databaseReference.child(FIREBASE_USERS_COLLECTION).child(idToken)
                .child(FIREBASE_SAVED_TRAVELS_COLLECTION).get().addOnCompleteListener(task -> {
                    if(!task.isSuccessful()) {
                        Log.d(TAG, "Error retrieving data" + task.getException());
                    } else {
                        Log.d(TAG, "Success: " + task.getResult().getValue());

                        List<Travel> travelList = new ArrayList<>();
                        for (DataSnapshot ds: task.getResult().getChildren()) {
                            Travel travel = ds.getValue(Travel.class);
                            travel.setSynchronized(true);
                            travelList.add(travel);
                        }
                        // TODO: callback
                    }
                });
    }

    @Override
    public void addSavedTravel(Travel travel) {
        databaseReference.child(FIREBASE_USERS_COLLECTION).child(idToken)
                .child(FIREBASE_SAVED_TRAVELS_COLLECTION).child(String.valueOf(travel.hashCode())).setValue(travel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        travel.setSynchronized(true);
                        // TODO: callback
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // TODO: callback
                    }
                });
    }

    @Override
    public void synchronizedFavoriteNews(List<Travel> notSynchronizedTravelsList) {
        // TODO
    }

    @Override
    public void deleteSavedTravel(Travel travel) {
        databaseReference.child(FIREBASE_USERS_COLLECTION).child(idToken)
                .child(FIREBASE_SAVED_TRAVELS_COLLECTION).child(String.valueOf(travel.hashCode()))
                .removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        travel.setSynchronized(false);
                        // TODO: callback
                    }
                }).addOnFailureListener(e -> {
                    // todo: callback
                });
    }

    @Override
    public void deleteAllSavedTravels() {
        databaseReference.child(FIREBASE_USERS_COLLECTION).child(idToken).
                child(FIREBASE_SAVED_TRAVELS_COLLECTION).removeValue().addOnSuccessListener(aVoid -> {
                    // todo callback
                }).addOnFailureListener(e -> {
                    // todo callback
                });
    }
}
