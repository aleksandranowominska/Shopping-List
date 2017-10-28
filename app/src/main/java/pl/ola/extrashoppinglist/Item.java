package pl.ola.extrashoppinglist;

import java.util.Date;

/**
 * Created by Aleksandra Kusiak on 27.09.2017.
 */

public class Item {
    public String itemName;
    public String itemDescription;
    public Date itemReminderDate;

    public Item (String itemName){
        this.itemName = itemName;
        this.itemDescription = "";
        this.itemReminderDate = null;
    }

    @Override
    public String toString() {
        return "Item{" +
                "itemName='" + itemName + '\'' +
                ", itemDescription='" + itemDescription + '\'' +
                ", itemReminderDate=" + itemReminderDate +
                '}';
    }
}
