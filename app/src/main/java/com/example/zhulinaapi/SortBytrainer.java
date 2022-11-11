package com.example.zhulinaapi;

import java.util.Comparator;

public class SortBytrainer implements Comparator<Mask> {

    @Override
    public int compare(Mask message, Mask t1) {
        return message.gettrainer().compareTo(t1.gettrainer());
    }
}
