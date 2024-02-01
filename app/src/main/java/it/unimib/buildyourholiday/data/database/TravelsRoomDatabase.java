package it.unimib.buildyourholiday.data.database;

import static it.unimib.buildyourholiday.util.Constants.TRAVELS_DATABASE_NAME;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import it.unimib.buildyourholiday.model.Flight;
import it.unimib.buildyourholiday.model.Hotel;
import it.unimib.buildyourholiday.model.Travel;

@Database(entities = {Travel.class, Flight.class, Hotel.class}, version = 1)
public abstract class TravelsRoomDatabase extends RoomDatabase {

    public abstract TravelDao travelDao();

    private static volatile TravelsRoomDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = Runtime.getRuntime().availableProcessors();
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static TravelsRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TravelsRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            TravelsRoomDatabase.class, TRAVELS_DATABASE_NAME).build();
                }
            }
        }
        return INSTANCE;
    }
}
