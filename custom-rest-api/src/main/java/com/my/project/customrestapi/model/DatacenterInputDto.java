package com.my.project.customrestapi.model;

public class DatacenterInputDto {

    private String name;

    private Integer noOfRacks;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNoOfRacks() {
        return noOfRacks;
    }

    public void setNoOfRacks(Integer noOfRacks) {
        this.noOfRacks = noOfRacks;
    }

}
