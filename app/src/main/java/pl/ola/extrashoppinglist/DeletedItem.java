package pl.ola.extrashoppinglist;

import java.util.Date;

/**
 * Created by Aleksandra Kusiak on 08.11.2017.
 */

public class DeletedItem {
    public String itemName;
    public String itemDescription;
    public int itemId;

    public DeletedItem(Item item) {
        this.itemName = item.itemName;
        this.itemDescription = item.itemDescription;
        this.itemId = item.itemId;
    }

    @Override
    public String toString() {
        return "DeletedItem{" +
                "itemName='" + itemName + '\'' +
                ", itemDescription='" + itemDescription + '\'' +
                ", itemId=" + itemId +
                '}';
    }
}
