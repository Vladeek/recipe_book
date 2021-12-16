package com.example.harrand.recipebook;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper
{
    public DBHelper(Context context, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, "recipebookDB", factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE recipes (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title VARCHAR(64) NOT NULL, " +
                "instructions VARCHAR(64) NOT NULL," +
                "rating INTEGER" +
                ");");
        //db.execSQL("INSERT INTO recipes (title, instructions, rating) VALUES ('pizza', 'fry it lol', 5);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS recipes");
        this.onCreate(db);
    }

    /*
    public void addRecipeToDatabase(String recipe_name, String recipe_instructions)
    {
        this.db.execSQL("INSERT INTO recipes (name, instructions) " +
                "VALUES " +
                "('" + recipe_name + "','" + recipe_instructions + "');");
    }

    public Cursor getRecipes()
    {
        return this.db.query("recipes", new String[]{"recipeID, name, instructions"}, null, null, null, null, null);
    }
    */
}