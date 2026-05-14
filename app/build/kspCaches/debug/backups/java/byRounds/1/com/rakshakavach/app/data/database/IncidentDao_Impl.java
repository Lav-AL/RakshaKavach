package com.rakshakavach.app.data.database;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.rakshakavach.app.data.model.IncidentLog;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class IncidentDao_Impl implements IncidentDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<IncidentLog> __insertionAdapterOfIncidentLog;

  private final SharedSQLiteStatement __preparedStmtOfDeleteIncident;

  public IncidentDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfIncidentLog = new EntityInsertionAdapter<IncidentLog>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR REPLACE INTO `incident_logs` (`id`,`taskName`,`incidentType`,`description`,`location`,`severity`,`timestamp`,`gearMissed`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final IncidentLog entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getTaskName());
        statement.bindString(3, entity.getIncidentType());
        statement.bindString(4, entity.getDescription());
        statement.bindString(5, entity.getLocation());
        statement.bindString(6, entity.getSeverity());
        statement.bindLong(7, entity.getTimestamp());
        statement.bindString(8, entity.getGearMissed());
      }
    };
    this.__preparedStmtOfDeleteIncident = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM incident_logs WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertIncident(final IncidentLog incident,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfIncidentLog.insert(incident);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteIncident(final int id, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteIncident.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, id);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteIncident.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public LiveData<List<IncidentLog>> getAllIncidents() {
    final String _sql = "SELECT * FROM incident_logs ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return __db.getInvalidationTracker().createLiveData(new String[] {"incident_logs"}, false, new Callable<List<IncidentLog>>() {
      @Override
      @Nullable
      public List<IncidentLog> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTaskName = CursorUtil.getColumnIndexOrThrow(_cursor, "taskName");
          final int _cursorIndexOfIncidentType = CursorUtil.getColumnIndexOrThrow(_cursor, "incidentType");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
          final int _cursorIndexOfSeverity = CursorUtil.getColumnIndexOrThrow(_cursor, "severity");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfGearMissed = CursorUtil.getColumnIndexOrThrow(_cursor, "gearMissed");
          final List<IncidentLog> _result = new ArrayList<IncidentLog>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final IncidentLog _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpTaskName;
            _tmpTaskName = _cursor.getString(_cursorIndexOfTaskName);
            final String _tmpIncidentType;
            _tmpIncidentType = _cursor.getString(_cursorIndexOfIncidentType);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final String _tmpLocation;
            _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
            final String _tmpSeverity;
            _tmpSeverity = _cursor.getString(_cursorIndexOfSeverity);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final String _tmpGearMissed;
            _tmpGearMissed = _cursor.getString(_cursorIndexOfGearMissed);
            _item = new IncidentLog(_tmpId,_tmpTaskName,_tmpIncidentType,_tmpDescription,_tmpLocation,_tmpSeverity,_tmpTimestamp,_tmpGearMissed);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public LiveData<List<IncidentLog>> getIncidentsByTask(final String taskName) {
    final String _sql = "SELECT * FROM incident_logs WHERE taskName = ? ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, taskName);
    return __db.getInvalidationTracker().createLiveData(new String[] {"incident_logs"}, false, new Callable<List<IncidentLog>>() {
      @Override
      @Nullable
      public List<IncidentLog> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTaskName = CursorUtil.getColumnIndexOrThrow(_cursor, "taskName");
          final int _cursorIndexOfIncidentType = CursorUtil.getColumnIndexOrThrow(_cursor, "incidentType");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
          final int _cursorIndexOfSeverity = CursorUtil.getColumnIndexOrThrow(_cursor, "severity");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfGearMissed = CursorUtil.getColumnIndexOrThrow(_cursor, "gearMissed");
          final List<IncidentLog> _result = new ArrayList<IncidentLog>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final IncidentLog _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpTaskName;
            _tmpTaskName = _cursor.getString(_cursorIndexOfTaskName);
            final String _tmpIncidentType;
            _tmpIncidentType = _cursor.getString(_cursorIndexOfIncidentType);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final String _tmpLocation;
            _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
            final String _tmpSeverity;
            _tmpSeverity = _cursor.getString(_cursorIndexOfSeverity);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final String _tmpGearMissed;
            _tmpGearMissed = _cursor.getString(_cursorIndexOfGearMissed);
            _item = new IncidentLog(_tmpId,_tmpTaskName,_tmpIncidentType,_tmpDescription,_tmpLocation,_tmpSeverity,_tmpTimestamp,_tmpGearMissed);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getIncidentCount(final Continuation<? super Integer> $completion) {
    final String _sql = "SELECT COUNT(*) FROM incident_logs";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<Integer>() {
      @Override
      @NonNull
      public Integer call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final Integer _result;
          if (_cursor.moveToFirst()) {
            final int _tmp;
            _tmp = _cursor.getInt(0);
            _result = _tmp;
          } else {
            _result = 0;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Object getRecentIncidents(final long startTime,
      final Continuation<? super List<IncidentLog>> $completion) {
    final String _sql = "SELECT * FROM incident_logs WHERE timestamp >= ? ORDER BY timestamp DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, startTime);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<IncidentLog>>() {
      @Override
      @NonNull
      public List<IncidentLog> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfTaskName = CursorUtil.getColumnIndexOrThrow(_cursor, "taskName");
          final int _cursorIndexOfIncidentType = CursorUtil.getColumnIndexOrThrow(_cursor, "incidentType");
          final int _cursorIndexOfDescription = CursorUtil.getColumnIndexOrThrow(_cursor, "description");
          final int _cursorIndexOfLocation = CursorUtil.getColumnIndexOrThrow(_cursor, "location");
          final int _cursorIndexOfSeverity = CursorUtil.getColumnIndexOrThrow(_cursor, "severity");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final int _cursorIndexOfGearMissed = CursorUtil.getColumnIndexOrThrow(_cursor, "gearMissed");
          final List<IncidentLog> _result = new ArrayList<IncidentLog>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final IncidentLog _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpTaskName;
            _tmpTaskName = _cursor.getString(_cursorIndexOfTaskName);
            final String _tmpIncidentType;
            _tmpIncidentType = _cursor.getString(_cursorIndexOfIncidentType);
            final String _tmpDescription;
            _tmpDescription = _cursor.getString(_cursorIndexOfDescription);
            final String _tmpLocation;
            _tmpLocation = _cursor.getString(_cursorIndexOfLocation);
            final String _tmpSeverity;
            _tmpSeverity = _cursor.getString(_cursorIndexOfSeverity);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            final String _tmpGearMissed;
            _tmpGearMissed = _cursor.getString(_cursorIndexOfGearMissed);
            _item = new IncidentLog(_tmpId,_tmpTaskName,_tmpIncidentType,_tmpDescription,_tmpLocation,_tmpSeverity,_tmpTimestamp,_tmpGearMissed);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
