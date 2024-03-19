package it.unimib.buildyourholiday.data.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import it.unimib.buildyourholiday.model.Travel;

@Dao
public interface TravelDao {

    @Query("SELECT * from travel WHERE is_saved = 1")
    List<Travel> getAllSaved();

    @Query("SELECT * FROM travel WHERE id = :id")
    Travel getTravel(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertTravel(Travel travel);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insertTravelList(List<Travel> travelList);

    @Insert
    void insertAll(Travel... travel);

    @Update
    int updateSingleSavedTravel(Travel travel);

    @Delete
    void delete(Travel travel);

    @Query("DELETE FROM travel")
    int deleteAll();

    @Query("SELECT * FROM travel WHERE country = :countryCode")
    List<Travel> getTravels(String countryCode);

    @Query("SELECT * FROM travel WHERE is_booked = 1")
    List<Travel> getAllBooked();

    @Query("SELECT * FROM travel")
    List<Travel> getAll();
}
