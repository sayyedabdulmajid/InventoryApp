package com.example.inventoryapp.Item;

import java.util.Set;

public interface DaoItem {
    Item getItem(String itemCode) ;
    Set<Item> getItems();
}
