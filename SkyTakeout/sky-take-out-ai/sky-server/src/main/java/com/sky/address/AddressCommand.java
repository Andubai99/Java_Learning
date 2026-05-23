package com.sky.address;

public record AddressCommand(String consignee, String phone, String provinceName, String cityName, String detail) {
}
