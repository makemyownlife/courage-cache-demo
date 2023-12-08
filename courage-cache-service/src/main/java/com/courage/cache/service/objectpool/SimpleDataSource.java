package com.courage.cache.service.objectpool;

import javax.print.DocFlavor;
import java.util.Arrays;

public class SimpleDataSource {

    public static void main(String[] args) {
        int poolingCount = 5;
        int removeCount = 3;
        String[] connections = new String[]{
                "0",
                "1",
                "2",
                "3",
                "4",
                null,
                null,
                null
        };
        System.arraycopy(connections, removeCount, connections, 0, poolingCount - removeCount);
        Arrays.fill(connections, poolingCount - removeCount, poolingCount, null);
        System.out.println(connections);
    }

}
