package pl.ola.extrashoppinglist;

import java.util.Date;

/**
 * Created by Aleksandra Kusiak on 27.09.2017.
 */

public class Item {
    public String itemName;
    public String itemDescription;
    public Date itemReminderDate;
    public int itemId;
    public boolean isItemStarred;

    public Item (String itemName){
        this.itemName = itemName;
        this.itemDescription = "";
        this.itemReminderDate = null;
        this.itemId = 0;
        this.isItemStarred = false;
    }

    @Override
    public String toString() {
        return "Item{" +
                "itemName='" + itemName + '\'' +
                ", itemDescription='" + itemDescription + '\'' +
                ", itemReminderDate=" + itemReminderDate +
                ", itemId=" + itemId +
                ", isItemStarred=" + isItemStarred +
                '}';
    }

}
