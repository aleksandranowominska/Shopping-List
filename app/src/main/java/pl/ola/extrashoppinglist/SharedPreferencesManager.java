package pl.ola.extrashoppinglist;

import android.content.Context;
import android.content.SharedPreferences;

import io.objectbox.Box;

/**
 * Created by Aleksandra Kusiak on 22.11.2017.
 */

public class SharedPreferencesManager {

    private static SharedPreferencesManager instance;
    private Context context;
    public static final String MY_SHARED_PREFS = "MyPreferences";
    public static final String SORTED_ASCENDING = "asc";
    public static final String SORTED_DESCENDING = "dsc";
    public static final String SORTED_WITH_PRIORITY = "pri";
    public static final String NOT_SORTED = "none";

    public static SharedPreferencesManager getInstance(Context context){
        if (instance == null) {
            instance = new SharedPreferencesManager(context);
        }
        return instance;
    }

    private SharedPreferencesManager(Context context) {
        this.context = context;
    }


    public void setSortingStyle(String sortingStyle){
        SharedPreferences.Editor editor = context.getSharedPreferences(MY_SHARED_PREFS, context.MODE_PRIVATE).edit();
        editor.putString("sortingStyle", sortingStyle);
        editor.apply();
    }

    public String getSortingStyle(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(MY_SHARED_PREFS, context.MODE_PRIVATE);
        String restoredSortingStyle = sharedPreferences.getString("sortingStyle", NOT_SORTED);
        return restoredSortingStyle;
    }
}
