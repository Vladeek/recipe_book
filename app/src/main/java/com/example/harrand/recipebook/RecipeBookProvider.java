package com.example.harrand.recipebook;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.ContentUris;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

public class RecipeBookProvider extends ContentProvider
{
    private DBHelper helper;

    private static final UriMatcher matcher;

    static
    {
        matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(RecipeBookContract.AUTHORITY, "recipes", 1);
        matcher.addURI(RecipeBookContract.AUTHORITY, "recipes/#", 2);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs)
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        String tableName;
        switch(matcher.match(uri))
        {
            case 1:
            default:
                tableName = "recipes";
                break;
        }
        int ret = db.delete(tableName, selection, selectionArgs);
        db.close();
        getContext().getContentResolver().notifyChange(uri, null);
        return ret;
    }

    @Override
    public String getType(Uri uri)
    {
        if (uri.getLastPathSegment()==null)
        {
            return "vnd.android.cursor.dir/RecipeBookProvider.data.text";
        }
        else
        {
            return "vnd.android.cursor.item/RecipeBookProvider.data.text";
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values)
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        String tableName;
        switch(matcher.match(uri))
        {
            case 1:
            default:
                tableName = "recipes";
                break;
        }
        long id = db.insert(tableName, null, values);
        db.close();
        Uri nu = ContentUris.withAppendedId(uri, id);
        Log.d("g53mdp", nu.toString());
        getContext().getContentResolver().notifyChange(nu, null);
        return nu;
    }

    @Override
    public boolean onCreate()
    {
        this.helper = new DBHelper(this.getContext(), null, 1);
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
    {
        switch(matcher.match(uri))
        {
            case 1:
            default:
                return this.helper.getWritableDatabase().query("recipes", projection, selection, selectionArgs, null, null, sortOrder);
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs)
    {
        SQLiteDatabase db = helper.getWritableDatabase();
        String tableName;
        switch(matcher.match(uri))
        {
            case 1:
            default:
                tableName = "recipes";
                break;
        }
        int ret = db.update(tableName, values, selection, selectionArgs);
        db.close();
        getContext().getContentResolver().notifyChange(uri, null);
        return ret;
    }
}
