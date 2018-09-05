package com.poiji.util;

import org.apache.poi.ss.usermodel.Row;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Converting {

    private static final Converting instance = new Converting();

    public static Converting getInstance() {
        return instance;
    }

    private Converting() {
    }

    public <T> Object convertValue(Row currentRow, T instance, Field field) {
        Object o;
        switch (field.getType().getName()) {
            case "java.util.List": {
//                o = listValue(instance, field, );
                break;
            }
            case "java.util.Map": {
//                o = mapValue(instance, field);
                break;
            }
        }

        return null;
    }

    private <K, V, T> Map<K, V> mapValue(T instance, Field field, K key, V value) throws IllegalAccessException {
        field.setAccessible(true);
        Map<K, V> map = (Map<K, V>) field.get(instance);
        if(map == null) {
            map = new HashMap<>();
        }
        map.put(key, value);
        return map;
    }

    private <V, T> List<V> listValue(T instance, Field field, V value) throws IllegalAccessException {
        field.setAccessible(true);
        List<V> list = (List<V>) field.get(instance);
        if(list == null) {
            list = new ArrayList<>();
        }
        list.add(value);
        return list;
    }
}
