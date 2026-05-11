package com.jd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jd.entity.Address;

import java.util.List;

public interface AddressService extends IService<Address> {
    List<Address> getUserAddresses(Long userId);
    Address addAddress(Long userId, Address address);
    void updateAddress(Long userId, Long addressId, Address address);
    void deleteAddress(Long userId, Long addressId);
    void setDefault(Long userId, Long addressId);
}
