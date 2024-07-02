package com.mycompany.myapp.service.dto;

import com.mycompany.myapp.domain.District;
import com.mycompany.myapp.domain.Union;
import com.mycompany.myapp.domain.Upazila;
import com.mycompany.myapp.domain.enumeration.AddressType;
import com.mycompany.myapp.domain.enumeration.AddressType;
import java.io.Serializable;
import java.io.Serializable;

public class AddressDTO implements Serializable {

    private Long id;
    private String addressLineOne;
    private String addressLineTwo;
    private String addressLineThree;
    private AddressType addressType;
    private String unionName;
    private String upazilaName;
    private String districtName;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddressLineOne() {
        return addressLineOne;
    }

    public void setAddressLineOne(String addressLineOne) {
        this.addressLineOne = addressLineOne;
    }

    public String getAddressLineTwo() {
        return addressLineTwo;
    }

    public void setAddressLineTwo(String addressLineTwo) {
        this.addressLineTwo = addressLineTwo;
    }

    public String getAddressLineThree() {
        return addressLineThree;
    }

    public void setAddressLineThree(String addressLineThree) {
        this.addressLineThree = addressLineThree;
    }

    public AddressType getAddressType() {
        return addressType;
    }

    public void setAddressType(AddressType addressType) {
        this.addressType = addressType;
    }

    public String getUnionName() {
        return unionName;
    }

    public void setUnionName(String unionName) {
        this.unionName = unionName;
    }

    public String getUpazilaName() {
        return upazilaName;
    }

    public void setUpazilaName(String upazilaName) {
        this.upazilaName = upazilaName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }
}
