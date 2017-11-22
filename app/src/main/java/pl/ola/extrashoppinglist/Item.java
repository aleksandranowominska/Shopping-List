package pl.ola.extrashoppinglist;


import java.util.Date;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * Created by Aleksandra Kusiak on 27.09.2017.
 */

@Entity
public class Item {
    @Id
    public long id;

    public String itemName;
    public String itemDescription;
    public Date itemReminderDate;
    public boolean isItemStarred;
    public boolean isItemDone;

    public Item() {
    }

    public Item(String itemName) {
        this.itemName = itemName;
        this.itemDescription = "";
        this.itemReminderDate = null;
        this.isItemStarred = false;
        this.isItemDone = false;
    }


    @Override
    public String toString() {
        return "Item{" +
                "itemName='" + itemName + '\'' +
                ", itemDescription='" + itemDescription + '\'' +
                ", itemReminderDate=" + itemReminderDate +
                ", isItemStarred=" + isItemStarred +
                ", isItemDone=" + isItemDone +
                '}';
    }

}
