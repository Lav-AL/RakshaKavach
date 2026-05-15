package com.rakshakavach.app.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.rakshakavach.app.data.model.IncidentLog

@Dao
interface IncidentDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIncident(incident: IncidentLog)

    @Query("SELECT * FROM incident_logs ORDER BY timestamp DESC")
    fun getAllIncidents(): LiveData<List<IncidentLog>>

    @Query("SELECT * FROM incident_logs WHERE taskName = :taskName ORDER BY timestamp DESC")
    fun getIncidentsByTask(taskName: String): LiveData<List<IncidentLog>>

    @Query("SELECT COUNT(*) FROM incident_logs")
    suspend fun getIncidentCount(): Int

    @Query("DELETE FROM incident_logs WHERE id = :id")
    suspend fun deleteIncident(id: Int)

    @Query("SELECT * FROM incident_logs WHERE timestamp >= :startTime ORDER BY timestamp DESC")
    suspend fun getRecentIncidents(startTime: Long): List<IncidentLog>
}
