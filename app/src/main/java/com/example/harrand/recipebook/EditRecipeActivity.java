package com.example.harrand.recipebook;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class EditRecipeActivity extends AppCompatActivity
{
    public static final String RECIPE_ID = "recipe_id";
    public static final String RECIPE_TITLE = "recipe_title";
    public static final String RECIPE_RATING = "recipe_rating";
    public static final String RECIPE_INSTRUCTIONS = "recipe_instructions";

    private EditText recipe_title;
    private EditText recipe_rating;
    private EditText recipe_instructions;

    private int current_recipe_id;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe);

        this.recipe_title = findViewById(R.id.edit_recipe_title);
        this.recipe_rating = findViewById(R.id.edit_recipe_rating);
        this.recipe_instructions = findViewById(R.id.edit_recipe_instructions);

        Intent intent = this.getIntent();
        Bundle inputs = intent.getExtras();
        String title = null, instructions = null;
        Integer rating = null;
        if(intent.hasExtra(EditRecipeActivity.RECIPE_ID))
            this.current_recipe_id = inputs.getInt(EditRecipeActivity.RECIPE_ID);
        if(intent.hasExtra(EditRecipeActivity.RECIPE_TITLE))
            title = inputs.getString(EditRecipeActivity.RECIPE_TITLE);
        if(intent.hasExtra(EditRecipeActivity.RECIPE_RATING))
            rating = inputs.getInt(EditRecipeActivity.RECIPE_RATING);
        if(intent.hasExtra(EditRecipeActivity.RECIPE_INSTRUCTIONS))
            instructions = inputs.getString(EditRecipeActivity.RECIPE_INSTRUCTIONS);
        this.updateGUIElements(title, rating, instructions);
    }

    private void updateGUIElements(String title, Integer rating, String instructions)
    {
        this.recipe_title.setText(title);
        this.recipe_rating.setText(rating != null ? rating.toString() : "");
        this.recipe_instructions.setText(instructions);
    }

    public void onButtonClick(View view)
    {
        switch(view.getId())
        {
            case R.id.edit_recipe_confirm:
                this.confirmEdit();
                backToMain();
                break;
            case R.id.edit_recipe_delete:
                deleteRecipe();
                backToMain();
                break;
        }
    }

    private void deleteRecipe()
    {
        final ContentResolver cr = getContentResolver();
        cr.delete(RecipeBookContract.RECIPES_URI, RecipeBookContract.RECIPEID + " = ?", new String[]{Integer.toString(this.current_recipe_id)});
    }

    private void confirmEdit()
    {
        String title = this.recipe_title.getText().toString();
        int rating = 0;
        try
        {
            rating = Integer.parseInt(this.recipe_rating.getText().toString());
        }catch(NumberFormatException nfe){}
        rating = this.clampRating(rating);
        String instructions = this.recipe_instructions.getText().toString();

        final String columns[] = new String[]
                {
                        RecipeBookContract.RECIPEID,
                        RecipeBookContract.TITLE,
                        RecipeBookContract.INSTRUCTIONS,
                        RecipeBookContract.RATING
                };
        final ContentResolver cr = getContentResolver();

        ContentValues newValues = new ContentValues();
        newValues.put(RecipeBookContract.TITLE, title);
        newValues.put(RecipeBookContract.RATING, rating);
        newValues.put(RecipeBookContract.INSTRUCTIONS, instructions);
        cr.update(RecipeBookContract.RECIPES_URI, newValues, RecipeBookContract.RECIPEID + " = ?", new String[]{Integer.toString(this.current_recipe_id)});
    }

    private int clampRating(int rating)
    {
        if(rating < 0)
            return 0;
        else if(rating > 5)
            return 5;
        else
            return rating;
    }

    private void backToMain()
    {
        startActivity(new Intent(this, MainActivity.class));
    }
}
