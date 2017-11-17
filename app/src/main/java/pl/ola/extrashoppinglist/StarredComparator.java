package pl.ola.extrashoppinglist;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.Comparator;

/**
 * Created by Aleksandra Kusiak on 17.11.2017.
 */

public class StarredComparator<I> implements Comparator<Item> {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int compare(Item o1, Item o2) {
        return Boolean.compare(o2.isItemStarred,o1.isItemStarred);
    }
}
