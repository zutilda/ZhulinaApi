package com.example.zhulinaapi;

import java.util.Comparator;

public class SortBywotkout implements Comparator<Mask> {
    @Override
    public int compare(Mask message, Mask t1) {
        return message.getwotkout().compareTo(t1.getwotkout());
    }
}
