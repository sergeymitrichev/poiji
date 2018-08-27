package com.poiji.deserialize.model.byname;

import com.poiji.annotation.ExcelCellName;

import java.util.ArrayList;
import java.util.List;

public class ListHolderByName {
    @ExcelCellName("Item")
    private
    List<String> items = new ArrayList<>();

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }


    @Override
    public String toString() {
        return "ListHolderByName{" +
                "items=" + items +
                '}';
    }
}
