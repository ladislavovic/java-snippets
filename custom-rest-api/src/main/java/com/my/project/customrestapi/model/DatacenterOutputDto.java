package com.my.project.customrestapi.model;

import java.util.ArrayList;
import java.util.List;

public class DatacenterOutputDto {

    private String name;

    private String localityCrossId;

    private String placeCrossId;

    private String technicalAreaCrossId;

    private List<String> rackCrossIds = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocalityCrossId() {
        return localityCrossId;
    }

    public void setLocalityCrossId(String localityCrossId) {
        this.localityCrossId = localityCrossId;
    }

    public String getPlaceCrossId() {
        return placeCrossId;
    }

    public void setPlaceCrossId(String placeCrossId) {
        this.placeCrossId = placeCrossId;
    }

    public String getTechnicalAreaCrossId() {
        return technicalAreaCrossId;
    }

    public void setTechnicalAreaCrossId(String technicalAreaCrossId) {
        this.technicalAreaCrossId = technicalAreaCrossId;
    }

    public List<String> getRackCrossIds() {
        return rackCrossIds;
    }

    public void setRackCrossIds(List<String> rackCrossIds) {
        this.rackCrossIds = rackCrossIds;
    }
}
