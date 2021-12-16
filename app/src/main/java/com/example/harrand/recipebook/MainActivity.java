package com.example.harrand.recipebook;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.populateList(false);
    }

    private void populateList(boolean sortByRating)
    {
        final String columns[] = new String[]
                {
                        RecipeBookContract.RECIPEID,
                        RecipeBookContract.TITLE,
                        RecipeBookContract.INSTRUCTIONS,
                        RecipeBookContract.RATING
                };

        String colsToDisplay [] = new String[]
                {
                        RecipeBookContract.TITLE,
                        RecipeBookContract.RATING
                };

        int[] colResIds = new int[]
                {
                        R.id.recipe_title,
                        R.id.recipe_rating
                };

        final ContentResolver cr = getContentResolver();

        Cursor c = cr.query(RecipeBookContract.RECIPES_URI,
                columns,
                null,
                null,
                sortByRating ? "rating DESC" : null);

        ListView lv = findViewById(R.id.recipesListView);
        lv.setOnItemClickListener(new ListView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapter_view, View v, int i, long l)
            {
                LinearLayout layout = (LinearLayout)((ViewGroup)v).getChildAt(0);
                String clicked_title = ((TextView)(layout.getChildAt(0))).getText().toString();
                Cursor tempCursor = cr.query(RecipeBookContract.RECIPES_URI, columns, RecipeBookContract.TITLE + " LIKE ?", new String[]{clicked_title}, null);
                int recipe_id = -1;
                Integer rating = null;
                String instructions = null;
                if(tempCursor.moveToFirst())
                {
                    recipe_id = tempCursor.getInt(tempCursor.getColumnIndex("_id"));
                    rating = tempCursor.getInt(tempCursor.getColumnIndex("rating"));
                    instructions = tempCursor.getString(tempCursor.getColumnIndex("instructions"));
                }
                tempCursor.close();
                MainActivity.this.editRecipe(recipe_id, clicked_title, rating, instructions);
            }
        });
        lv.setAdapter(new SimpleCursorAdapter(this,
                R.layout.recipe,
                c, colsToDisplay,
                colResIds, 0));
    }

    public void onButtonPress(View view)
    {
        switch(view.getId())
        {
            case R.id.add_new_recipe_button:
                Cursor cursor = getContentResolver().query(RecipeBookContract.RECIPES_URI,
                        null, null, null, null);
                startActivity(new Intent(this, CreateRecipeActivity.class));
                break;
            case R.id.sort_by_name:
                this.populateList(false);
                break;
            case R.id.sort_by_rating:
                this.populateList(true);
                break;
        }
    }

    private void editRecipe(int recipe_id, String title, Integer rating, String instructions)
    {
        Intent intent = new Intent(this, EditRecipeActivity.class);
        intent.putExtra(EditRecipeActivity.RECIPE_ID, recipe_id);
        intent.putExtra(EditRecipeActivity.RECIPE_TITLE, title);
        intent.putExtra(EditRecipeActivity.RECIPE_RATING, rating);
        intent.putExtra(EditRecipeActivity.RECIPE_INSTRUCTIONS, instructions);
        startActivity(intent);
    }
}
