package com.rakshakavach.app.viewmodel

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.rakshakavach.app.data.database.RakshaKavachDatabase
import com.rakshakavach.app.data.model.IncidentLog
import kotlinx.coroutines.launch
import java.util.Calendar

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val db = RakshaKavachDatabase.getDatabase(application)
    private val incidentDao = db.incidentDao()
    private val prefs: SharedPreferences = application.getSharedPreferences("raksha_prefs", 0)

    // All incidents from Room DB
    val allIncidents: LiveData<List<IncidentLog>> = incidentDao.getAllIncidents()

    // Safety Score
    private val _safetyScore = MutableLiveData<Int>()
    val safetyScore: LiveData<Int> = _safetyScore

    // Streak (consecutive safe days)
    private val _streak = MutableLiveData<Int>()
    val streak: LiveData<Int> = _streak

    // Selected task for today
    private val _selectedTask = MutableLiveData<String>()
    val selectedTask: LiveData<String> = _selectedTask

    // Checklist completion percentage
    private val _checklistPercent = MutableLiveData<Int>(0)
    val checklistPercent: LiveData<Int> = _checklistPercent

    init {
        loadSafetyScore()
        loadStreak()
        checkDailyReset()
    }

    private fun loadSafetyScore() {
        _safetyScore.value = prefs.getInt("safety_score", 0)
    }

    private fun loadStreak() {
        _streak.value = prefs.getInt("streak_days", 0)
    }

    private fun checkDailyReset() {
        val lastDate = prefs.getLong("last_check_date", 0L)
        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis

        if (lastDate < today) {
            // New day — reset checklist
            prefs.edit().putLong("last_check_date", today).apply()
            _checklistPercent.value = 0
        } else {
            _checklistPercent.value = prefs.getInt("checklist_percent", 0)
        }
    }

    fun setSelectedTask(taskName: String) {
        _selectedTask.value = taskName
        prefs.edit().putString("selected_task", taskName).apply()
    }

    fun updateSafetyScore(points: Int) {
        val current = _safetyScore.value ?: 0
        val newScore = (current + points).coerceAtLeast(0)
        _safetyScore.value = newScore
        prefs.edit().putInt("safety_score", newScore).apply()
    }

    fun completedSafeDay() {
        val currentStreak = _streak.value ?: 0
        val newStreak = currentStreak + 1
        _streak.value = newStreak
        prefs.edit().putInt("streak_days", newStreak).apply()
        // Bonus points for streak
        val bonus = when {
            newStreak % 7 == 0 -> 50
            newStreak % 3 == 0 -> 20
            else -> 10
        }
        updateSafetyScore(bonus)
    }

    fun updateChecklistPercent(percent: Int) {
        _checklistPercent.value = percent
        prefs.edit().putInt("checklist_percent", percent).apply()
    }

    fun addIncident(incident: IncidentLog) = viewModelScope.launch {
        incidentDao.insertIncident(incident)
        // Incident reported — deduct points
        updateSafetyScore(-20)
    }

    fun deleteIncident(id: Int) = viewModelScope.launch {
        incidentDao.deleteIncident(id)
    }

    fun getSelectedTask(): String {
        return prefs.getString("selected_task", "") ?: ""
    }

    fun addQuizPoints(correct: Int, total: Int) {
        val points = (correct.toFloat() / total * 30).toInt()
        updateSafetyScore(points)
    }
}
