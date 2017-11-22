package pl.ola.extrashoppinglist;

import android.content.Context;

import java.util.Comparator;
import java.util.List;

import io.objectbox.Box;
import io.objectbox.query.QueryBuilder;

/**
 * Created by Aleksandra Kusiak on 28.09.2017.
 */

public class DataManager {
    private static DataManager instance;
    Box itemBox;

    public static DataManager getInstance(Context context) {
        if (instance == null) {
            instance = new DataManager(context);
        }
        return instance;
    }

    private DataManager(Context context) {
        itemBox = ((MyApplication) context.getApplicationContext()).getBoxStore().boxFor(Item.class);
    }


    public List<Item> getItems() {
        return itemBox.query().equal(Item_.isItemDone, false).build().find();
    }

    public List<Item> getAscendingItems() {
        QueryBuilder<Item> builder = itemBox.query();
        builder.equal(Item_.isItemDone, false)
        .order(Item_.itemName);
        List<Item> ascendingList = builder.build().find();
        return ascendingList;
    }

    public List<Item> getDescendingItems() {
        QueryBuilder<Item> builder = itemBox.query();
        builder.equal(Item_.isItemDone, false)
        .orderDesc(Item_.itemName);
        List<Item> descendingList = builder.build().find();
        return descendingList;
    }

    public List<Item> getPrioritySortedItems() {
        QueryBuilder<Item> builder = itemBox.query();
        builder.equal(Item_.isItemDone, false)
                .orderDesc(Item_.isItemStarred);
        List<Item> prioritySortedList = builder.build().find();
        return prioritySortedList;
    }

    public List<Item> getDoneItems() {
        return itemBox.query().equal(Item_.isItemDone, true).build().find();
    }

    public void updateItem(Item item) {
        itemBox.put(item);
    }

    public long getItemId(Item item) {
        return itemBox.getId(item);
    }

    public Item getItemById(long itemId) {
        return (Item) itemBox.get(itemId);
    }

}
