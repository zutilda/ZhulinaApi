package com.example.zhulinaapi;

import java.util.Comparator;

public class SortByday implements Comparator<Mask> {
    @Override
    public int compare(Mask message, Mask t1) {
        return message.getday().compareTo(t1.getday());
    }
}
