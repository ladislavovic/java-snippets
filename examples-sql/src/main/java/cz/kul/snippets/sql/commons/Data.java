/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.kul.snippets.sql.commons;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author kulhalad
 */
public class Data {
    
    private List<List<Object>> data = new ArrayList<List<Object>>();

    private List<String> columnLabels = new ArrayList<>();
    
    public Data() {
    }
    
    public Data(List<String> columnAliases) {
        this.columnLabels = columnAliases;
    }
    
    public int getXSize() {
        if (data.isEmpty()) return 0;
        return data.get(0).size();
    }
    
    public int getYSize() {
        return data.size();
    }

    public int getRowsCount() {
        return getYSize();
    }

    public int getCollumnsCount() {
        return getXSize();
    }
    
    public Object getData(int x, int y) {
        return data.get(y).get(x);
    }
    
    public Object getData(String label, int y) {
        int x = labelToX(label);
        return getData(x, y);
    }

    public List<List<Object>> getRowsData() {
        return Collections.unmodifiableList(data);
    }

    public List<Object> getColumnData(int x) {
        ArrayList<Object> result = new ArrayList<>(getRowsCount());
        for (int row = 0; row < getRowsCount(); row++) {
            result.add(getData(x, row));
        }
        return result;
    }
    
    public void add(int x, int y, Object inst) {
        while (data.size() <= y ) {
            data.add(new ArrayList<Object>());
        }
        List<Object> row = data.get(y);
        while (row.size() <= x) {
            row.add(null);
        }
        row.set(x, inst);
    }
    
    private int labelToX(String alias) {
        int x = columnLabels.indexOf(alias);
        if (x < 0) throw new IllegalArgumentException("The alias " + alias + " was not found. All aliases: " + columnLabels);
        return x;
    }
}
