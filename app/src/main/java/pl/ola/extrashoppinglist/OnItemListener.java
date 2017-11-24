package pl.ola.extrashoppinglist;

/**
 * Created by Aleksandra Kusiak on 22.11.2017.
 */

public interface OnItemListener {

    void onItemDeleted(long itemId);
    void onItemClick(long itemId);
}
