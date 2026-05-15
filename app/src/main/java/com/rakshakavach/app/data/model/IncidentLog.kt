package com.rakshakavach.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "incident_logs")
data class IncidentLog(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val taskName: String,
    val incidentType: String,
    val description: String,
    val location: String,
    val severity: String, // LOW, MEDIUM, HIGH
    val timestamp: Long = System.currentTimeMillis(),
    val gearMissed: String = ""
)
