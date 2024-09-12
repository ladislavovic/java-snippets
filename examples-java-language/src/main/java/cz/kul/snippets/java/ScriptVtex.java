package cz.kul.snippets.java;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections.map.HashedMap;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ScriptVtex
{

    public static void main(String[] args) throws IOException
    {
        List<String> files = List.of(
            "/home/lad/tmp/20-08/page-1.json",
            "/home/lad/tmp/20-08/page-2.json",
            "/home/lad/tmp/20-08/page-3.json"
        );
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Integer> statuses = new HashMap<>();
        int orderCount = 0;
        HashSet<String> orderIds = new HashSet<>();
        HashSet<String> clients = new HashSet<>();
        HashMap<String, List<String>> statusToOrderNumber = new HashMap<>();

        for (String file : files) {

            Map page = objectMapper.readValue(new File(file), Map.class);
            List orders = (List) page.get("list");
            for (Object order : orders) {
                String orderStatus = getOrderStatus((Map) order);

                if (statuses.containsKey(orderStatus)) {
                    statuses.put(orderStatus, statuses.get(orderStatus) + 1);
                } else {
                    statuses.put(orderStatus, 1);
                }

                orderCount++;

                orderIds.add( ((Map) order).get("orderId").toString() );
                clients.add( ((Map) order).get("clientName").toString() );

                List<String> orderNumbers = statusToOrderNumber.computeIfAbsent(orderStatus, key -> new ArrayList<>());
                orderNumbers.add(((Map) order).get("orderId").toString());

            }
        }

        System.out.println("count:" + orderCount);
        System.out.println("ids count:" + orderIds.size());
        System.out.println("client count:" + clients.size());
        System.out.println(statuses);

        for (String orderNumber : statusToOrderNumber.get("ready-for-handling")) {
            System.out.printf("ROW('%s'),%n", orderNumber);
        }


    }

    private static String getOrderStatus(Map order) {
        return order.get("status").toString();
    }


}
