package com.example.assignment.DBHandler;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.assignment.Entity.LongTermNote;
import com.example.assignment.Entity.ShortTermNote;

import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String TAG = "SQLite";


    // Database version
    private static final int DATABASE_VERSION = 1;


    // Database name
    private static final String DATABASE_NAME = "ASAP1.db";


    // Short term note attributes
    private static final String TABLE_SHORTNOTE = "ShortTermNote";

    private static final String COLUMN_SHORTNOTE_ID = "Id";
    private static final String COLUMN_SHORTNOTE_TITLE = "Title";
    private static final String COLUMN_SHORTNOTE_CONTENT = "Content";
    private static final String COLUMN_SHORTNOTE_DEADLINE = "DeadLine";
    private static final String COLUMN_SHORTNOTE_ISDEAD = "IsDead";
    private static final String COLUMN_SHORTNOTE_LONGNOTEID = "LongNoteId";
    private static final String COLUMN_SHORTNOTE_ISCOMPLETE = "IsComplete";

    // Long term note attributes
    private static final String TABLE_LONGNOTE = "LongTermNote";

    private static final String COLUMN_LONGNOTE_ID = "Id";
    private static final String COLUMN_LONGNOTE_TITLE = "Title";

    //relationship constants
    private static final int NOT_BELONG = -1;
    private static final int NOT_COMPLETE = 0;
    private static final int NOT_DELETED = -1;

    private static final int COMPLETE = 1;
    private static final int DELETED = 1;


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    // Create tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // Table create scripts
        String script2 = "CREATE TABLE " + TABLE_SHORTNOTE + "("
                + COLUMN_SHORTNOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_SHORTNOTE_TITLE + " TEXT NOT NULL,"
                + COLUMN_SHORTNOTE_CONTENT + " TEXT NOT NULL,"
                + COLUMN_SHORTNOTE_DEADLINE + " TEXT NOT NULL,"
                + COLUMN_SHORTNOTE_ISDEAD + " INTEGER NOT NULL,"
                + COLUMN_SHORTNOTE_ISCOMPLETE + " INTEGER NOT NULL,"
                + COLUMN_SHORTNOTE_LONGNOTEID + " INTEGER)";

        String script = "CREATE TABLE " + TABLE_LONGNOTE + "("
                + COLUMN_LONGNOTE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_LONGNOTE_TITLE + " TEXT NOT NULL" + ")";
        // Run script to create tables
        db.execSQL(script);
        db.execSQL(script2);


    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.i(TAG, "MyDatabaseHelper.onUpgrade ... ");

        // Drop old tables if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHORTNOTE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LONGNOTE);

        // Create new one
        onCreate(db);
    }

    //add a short term note
    public long addShortTermNote(ShortTermNote note) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_SHORTNOTE_TITLE, note.getTitle());
        values.put(COLUMN_SHORTNOTE_CONTENT, note.getContent());
        values.put(COLUMN_SHORTNOTE_DEADLINE, note.getDeadline());
        values.put(COLUMN_SHORTNOTE_ISDEAD, NOT_DELETED);
        values.put(COLUMN_SHORTNOTE_ISCOMPLETE, NOT_COMPLETE);
        values.put(COLUMN_SHORTNOTE_LONGNOTEID, NOT_BELONG);

        SQLiteDatabase db = getWritableDatabase();
        long result = db.insert(TABLE_SHORTNOTE, null, values);

        return result;
    }

    //add a long term note
    public long addLongTermNote(LongTermNote note) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_LONGNOTE_TITLE, note.getTitle());

        SQLiteDatabase db = getWritableDatabase();
        long result = db.insert(TABLE_LONGNOTE, null, values);

        return result;
    }

    //find a short term note by name
    public ShortTermNote findShortTermNote(String noteName) {

        ShortTermNote note = new ShortTermNote();
        String query = "Select * From " + TABLE_SHORTNOTE
                + " Where " + COLUMN_SHORTNOTE_TITLE + " = '" + noteName + "'";
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            note.setId(cursor.getInt(0));
            note.setTitle(cursor.getString(1));
            note.setContent(cursor.getString(2));
            note.setDeadline(cursor.getString(3));
            note.setIsDeleted(cursor.getInt(4));
            note.setIsComplete(cursor.getInt(5));
            note.setLongNoteId(cursor.getInt(6));
            cursor.close();
            db.close();

            return note;
        }

        return null;
    }

    //find a short term note by id
    public ShortTermNote findShortById(int id) {
        ShortTermNote note = new ShortTermNote();
        String query = "Select * From " + TABLE_SHORTNOTE
                + " Where " + COLUMN_SHORTNOTE_ID + " = " + id + "";
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            note.setId(cursor.getInt(0));
            note.setTitle(cursor.getString(1));
            note.setContent(cursor.getString(2));
            note.setDeadline(cursor.getString(3));
            note.setIsDeleted(cursor.getInt(4));
            note.setIsComplete(cursor.getInt(5));
            note.setLongNoteId(cursor.getInt(6));

            cursor.close();
            db.close();
            return note;
        }

        return null;
    }

    //find a long term note
    public LongTermNote findLongTermNote(String noteName) {
        LongTermNote note = new LongTermNote();
        String query = "Select * From " + TABLE_LONGNOTE
                + " Where " + COLUMN_LONGNOTE_TITLE + " = '" + noteName + "'";

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            note.setId(cursor.getInt(0));
            note.setTitle(cursor.getString(1));

            cursor.close();
            db.close();
            return note;
        }
        return null;
    }

    //find short term plans of an long term plan
    public ArrayList<ShortTermNote> findShortByLong(int longTermId) {
        ShortTermNote note;
        ArrayList<ShortTermNote> notes = new ArrayList<>();
        String query = "Select * From " + TABLE_SHORTNOTE
                + " Where " + COLUMN_SHORTNOTE_LONGNOTEID + " = " + longTermId
                + " and "
                + COLUMN_SHORTNOTE_ISDEAD + " = " + NOT_DELETED;
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                note = new ShortTermNote();
                note.setId(cursor.getInt(0));
                note.setTitle(cursor.getString(1));
                note.setContent(cursor.getString(2));
                note.setDeadline(cursor.getString(3));
                note.setIsDeleted(cursor.getInt(4));
                note.setIsComplete(cursor.getInt(5));
                note.setLongNoteId(cursor.getInt(6));

                notes.add(note);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return notes;
    }

    //delete a short term note by name
    public int deleteShortTermNote(String noteName) {
        ShortTermNote note = findShortTermNote(noteName);
        if (note != null) {
            SQLiteDatabase db = getWritableDatabase();
            int result = db.delete(TABLE_SHORTNOTE, COLUMN_SHORTNOTE_ID + " = ?", new String[]{String.valueOf(note.getId())});
            return result;
        }
        return 0;
    }

    //delete short term note by id
    public int deleteShortById(int id) {
        ShortTermNote note = findShortById(id);
        if (note != null) {
            SQLiteDatabase db = getWritableDatabase();
            int result = db.delete(TABLE_SHORTNOTE, COLUMN_SHORTNOTE_ID + " = ?", new String[]{String.valueOf(note.getId())});
            return result;
        }
        return 0;
    }

    //delete a long term note by name
    public int deleteLongTermNote(String noteName) {
        LongTermNote note = findLongTermNote(noteName);
        if (note != null) {
            SQLiteDatabase db = getWritableDatabase();
            int longResult = db.delete(TABLE_LONGNOTE, COLUMN_LONGNOTE_ID + " = ?", new String[]{String.valueOf(note.getId())});


            ArrayList<ShortTermNote> shortNotes = findShortByLong(note.getId());
            String[] shortNoteIds = new String[shortNotes.size()];
            int i = 0;
            for (ShortTermNote shortTermNote : shortNotes) {
                shortNoteIds[i] = String.valueOf(shortTermNote.getId());
                i++;
            }

            int shortResult = db.delete(TABLE_SHORTNOTE, COLUMN_SHORTNOTE_ID + " = ?", shortNoteIds);

            return longResult;
        }
        return 0;
    }


    //find all short notes
    public ArrayList<ShortTermNote> findAllShortNotes() {
        ShortTermNote note;
        ArrayList<ShortTermNote> notes = new ArrayList<>();
        String query = "Select * From " + TABLE_SHORTNOTE + " Where "
                + COLUMN_SHORTNOTE_ISDEAD + " = " + NOT_DELETED
                + " and " + COLUMN_SHORTNOTE_LONGNOTEID + NOT_BELONG;

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                note = new ShortTermNote();
                note.setId(cursor.getInt(0));
                note.setTitle(cursor.getString(1));
                note.setContent(cursor.getString(2));
                note.setDeadline(cursor.getString(3));
                note.setIsDeleted(cursor.getInt(4));
                note.setIsComplete(cursor.getInt(5));
                note.setLongNoteId(cursor.getInt(6));

                notes.add(note);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return notes;
    }

    //find all short notes
    public ArrayList<ShortTermNote> findAllTrash() {
        ShortTermNote note;
        ArrayList<ShortTermNote> notes = new ArrayList<>();
        String query = "Select * From " + TABLE_SHORTNOTE + " WHERE " + COLUMN_SHORTNOTE_ISDEAD + " = " + DELETED;

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                note = new ShortTermNote();
                note.setId(cursor.getInt(0));
                note.setTitle(cursor.getString(1));
                note.setContent(cursor.getString(2));
                note.setDeadline(cursor.getString(3));
                note.setIsDeleted(cursor.getInt(4));
                note.setIsComplete(cursor.getInt(5));
                note.setLongNoteId(cursor.getInt(6));

                notes.add(note);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return notes;
    }

    //find urgent notes
    public ArrayList<ShortTermNote> findUrgentNotes() {
        ShortTermNote note;
        ArrayList<ShortTermNote> notes = new ArrayList<>();
        String query = "Select *  From  " + TABLE_SHORTNOTE + " Where " + COLUMN_SHORTNOTE_ISDEAD + " = " + NOT_DELETED +
                " and " + COLUMN_SHORTNOTE_ISCOMPLETE + " = " + NOT_COMPLETE +
                " Order by " + COLUMN_SHORTNOTE_DEADLINE;

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                note = new ShortTermNote();
                note.setId(cursor.getInt(0));
                note.setTitle(cursor.getString(1));
                note.setContent(cursor.getString(2));
                note.setDeadline(cursor.getString(3));
                note.setIsDeleted(cursor.getInt(4));
                note.setIsComplete(cursor.getInt(5));
                note.setLongNoteId(cursor.getInt(6));

                if (note.getIsDeleted() != DELETED) {
                    notes.add(note);
                } else {
                    continue;
                }

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        if (notes.size() <= 5) {
            return notes;
        } else {
            ArrayList<ShortTermNote> urgents = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                urgents.add(notes.get(i));
            }
            return urgents;
        }
    }

    //update a short term note
    public int updateShortNote(int id, ShortTermNote note) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_SHORTNOTE_TITLE, note.getTitle());
        values.put(COLUMN_SHORTNOTE_CONTENT, note.getContent());
        values.put(COLUMN_SHORTNOTE_DEADLINE, note.getDeadline());
        values.put(COLUMN_SHORTNOTE_ISCOMPLETE, note.getIsComplete());
        values.put(COLUMN_SHORTNOTE_ISDEAD, note.getIsDeleted());

        SQLiteDatabase database = getWritableDatabase();
        return database.update(TABLE_SHORTNOTE, values, COLUMN_SHORTNOTE_ID + " = ?", new String[]{String.valueOf(id)});
    }

    //move item to trash bin
    public int moveTrashShort(int id) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_SHORTNOTE_ISDEAD, DELETED);

        SQLiteDatabase database = getWritableDatabase();
        return database.update(TABLE_SHORTNOTE, values, COLUMN_SHORTNOTE_ID + " = ?", new String[]{String.valueOf(id)});
    }

    //restore item from trash
    public int restorePlan(int id) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_SHORTNOTE_ISDEAD, NOT_DELETED);

        SQLiteDatabase database = getWritableDatabase();
        return database.update(TABLE_SHORTNOTE, values, COLUMN_SHORTNOTE_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public int deleteAllBin() {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_SHORTNOTE, COLUMN_SHORTNOTE_ISDEAD + " = ?", new String[]{String.valueOf(DELETED)});
    }

    public int revertAllBin() {
        ContentValues values = new ContentValues();
        values.put(COLUMN_SHORTNOTE_ISDEAD, NOT_DELETED);

        SQLiteDatabase database = getWritableDatabase();
        return database.update(TABLE_SHORTNOTE, values, COLUMN_SHORTNOTE_ISDEAD + " = ?", new String[]{String.valueOf(DELETED)});
    }

    public ArrayList<LongTermNote> findAllLongNotes() {
        LongTermNote note;
        ArrayList<LongTermNote> notes = new ArrayList<>();
        String query = "Select * From " + TABLE_LONGNOTE + " order by " + COLUMN_LONGNOTE_ID + " desc";

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                note = new LongTermNote();
                note.setId(cursor.getInt(0));
                note.setTitle(cursor.getString(1));


                notes.add(note);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return notes;
    }

    public long addShortTermNoteOfLongTerm(ShortTermNote note) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_SHORTNOTE_TITLE, note.getTitle());
        values.put(COLUMN_SHORTNOTE_CONTENT, note.getContent());
        values.put(COLUMN_SHORTNOTE_DEADLINE, note.getDeadline());
        values.put(COLUMN_SHORTNOTE_ISDEAD, note.getIsDeleted());
        values.put(COLUMN_SHORTNOTE_ISCOMPLETE, NOT_COMPLETE);
        values.put(COLUMN_SHORTNOTE_LONGNOTEID, note.getLongNoteId());

        SQLiteDatabase db = getWritableDatabase();
        long result = db.insert(TABLE_SHORTNOTE, null, values);

        return result;
    }

    public int updateLongNote(int id, LongTermNote note) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_SHORTNOTE_TITLE, note.getTitle());

        SQLiteDatabase database = getWritableDatabase();
        return database.update(TABLE_LONGNOTE, values, COLUMN_LONGNOTE_ID + " = ?", new String[]{String.valueOf(id)});
    }
}
