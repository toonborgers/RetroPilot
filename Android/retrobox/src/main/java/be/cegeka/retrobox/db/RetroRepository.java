package be.cegeka.retrobox.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import be.cegeka.retrobox.domain.Retro;

public class RetroRepository {
    private final RetroBoxDBHelper retroPilotDBHelper;

    public RetroRepository(Context ctx) {
        this.retroPilotDBHelper = new RetroBoxDBHelper(ctx);
    }

    public boolean store(Retro retro) {
        long id = retroPilotDBHelper
                .getWritableDatabase()
                .insert(RetroBoxContract.Retros.TABLE_NAME, null, toContentValues(retro));

        return id > 0;
    }

    public List<Retro> getRetros() {
        Cursor cursor = retroPilotDBHelper.getReadableDatabase().query(RetroBoxContract.Retros.TABLE_NAME, null, null, null, null, null, null);
        List<Retro> result = new ArrayList<Retro>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            int id = cursor.getInt(cursor.getColumnIndex(RetroBoxContract.Retros._ID));
            String location = cursor.getString(cursor.getColumnIndex(RetroBoxContract.Retros.COL_LOCATION));
            String name = cursor.getString(cursor.getColumnIndex(RetroBoxContract.Retros.COL_NAME));
            long time = cursor.getLong(cursor.getColumnIndex(RetroBoxContract.Retros.COL_TIME));

            result.add(new Retro.Builder()
                    .withId(id)
                    .withLocation(location)
                    .withName(name)
                    .withTime(new DateTime(time))
                    .build());
        }
        cursor.close();
        return result;
    }

    public ContentValues toContentValues(Retro retro) {
        ContentValues values = new ContentValues();
        if (retro.getId() > 0) {
            values.put(RetroBoxContract.Retros._ID, retro.getId());
        }
        values.put(RetroBoxContract.Retros.COL_NAME, retro.getName());
        values.put(RetroBoxContract.Retros.COL_LOCATION, retro.getLocation());
        values.put(RetroBoxContract.Retros.COL_TIME, retro.getTime().toDateTime().getMillis());
        return values;
    }
}