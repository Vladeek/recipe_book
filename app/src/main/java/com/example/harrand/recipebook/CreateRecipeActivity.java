package com.example.harrand.recipebook;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.util.List;

public class CreateRecipeActivity extends AppCompatActivity
{

    List<RecipeBookContract> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_recipe);
    }

    public void onButtonClick(View view)
    {
        switch(view.getId())
        {
            case R.id.add_recipe_button:
                String title = ((EditText)findViewById(R.id.recipe_title)).getText().toString();
                String instructions = ((EditText)findViewById(R.id.recipe_instructions)).getText().toString();
                this.addRecipe(title, instructions);
                break;
        }
    }

    public void addRecipe(String title, String instructions)
    {
        ContentValues newValues = new ContentValues();
        newValues.put(RecipeBookContract.TITLE, title);
        newValues.put(RecipeBookContract.INSTRUCTIONS, instructions);
        getContentResolver().insert(RecipeBookContract.RECIPES_URI, newValues);
        startActivity(new Intent(this, MainActivity.class));
    }
}
