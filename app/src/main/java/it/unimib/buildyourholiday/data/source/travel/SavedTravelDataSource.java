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
                            if(travel.isSaved())
                                travelList.add(travel);
                        }
                        travelCallback.onSuccessFromCloudReading(travelList);
                    }
                });
    }

    @Override
    public void getSavedTravels(String country) {
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
                            if(travel.getCountry().equals(country)) {

                                travelList.add(travel);
                            }
                        }
                        travelCallback.onSuccessFromCloudReading(travelList);
                    }
                });
    }

    @Override
    public void addTravel(Travel travel) {
        databaseReference.child(FIREBASE_USERS_COLLECTION).child(idToken)
                .child(FIREBASE_SAVED_TRAVELS_COLLECTION).child(String.valueOf(travel.getId())).setValue(travel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        travel.setSynchronized(true);
                        travelCallback.onSuccessFromCloudWriting(travel);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        travelCallback.onFailureFromCloud(e);
                    }
                });
    }

    @Override
    public void synchronizeSavedTravels(List<Travel> notSynchronizedTravelsList) {
        databaseReference.child(FIREBASE_USERS_COLLECTION).child(idToken)
                .child(FIREBASE_SAVED_TRAVELS_COLLECTION).get().addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        List<Travel> travels = new ArrayList<>();
                        for (DataSnapshot ds:task.getResult().getChildren()) {
                            Travel travel = ds.getValue(Travel.class);
                            travel.setSynchronized(true);
                            travels.add(travel);
                        }
                        travels.addAll(notSynchronizedTravelsList);

                        for (Travel t:travels) {
                            databaseReference.child(FIREBASE_USERS_COLLECTION).child(idToken)
                                    .child(FIREBASE_SAVED_TRAVELS_COLLECTION)
                                    .child(String.valueOf(t.hashCode())).setValue(t).addOnSuccessListener(
                                            new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    t.setSynchronized(true);
                                                }
                                            }
                                    );
                        }
                    }
                });
    }

    @Override
    public void getBookedTravels() {
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
                            if(travel.isBooked())
                                travelList.add(travel);
                        }
                        travelCallback.onSuccessFromBookedCloudReading(travelList);
                    }
                });
    }

    @Override
    public void deleteSavedTravel(Travel travel) {
        databaseReference.child(FIREBASE_USERS_COLLECTION).child(idToken)
                .child(FIREBASE_SAVED_TRAVELS_COLLECTION).child(String.valueOf(travel.getId()))
                .removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        travel.setSynchronized(false);
                        travelCallback.onSuccessFromCloudWriting(travel);
                    }
                }).addOnFailureListener(e -> {
                    travelCallback.onFailureFromCloud(e);
                });
    }

    @Override
    public void deleteAllSavedTravels() {
        databaseReference.child(FIREBASE_USERS_COLLECTION).child(idToken).
                child(FIREBASE_SAVED_TRAVELS_COLLECTION).removeValue().addOnSuccessListener(aVoid -> {
                    travelCallback.onSuccessFromCloudWriting(null);
                }).addOnFailureListener(e -> {
                    travelCallback.onFailureFromCloud(e);
                });
    }
}
