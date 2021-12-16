package com.example.harrand.recipebook;

import android.net.Uri;

public class RecipeBookContract
{
    public static final String AUTHORITY = "com.example.harrand.recipebook.RecipeBookProvider";
    public static final Uri RECIPES_URI =
            Uri.parse("content://" + AUTHORITY + "/recipes");
    public static final String RECIPEID = "_id";
    public static final String TITLE = "title";
    public static final String INSTRUCTIONS = "instructions";
    public static final String RATING = "rating";
}
