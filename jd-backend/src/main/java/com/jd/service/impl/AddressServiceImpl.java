package com.jd.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jd.entity.Address;
import com.jd.mapper.AddressMapper;
import com.jd.service.AddressService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements AddressService {

    @Override
    public List<Address> getUserAddresses(Long userId) {
        return list(new LambdaQueryWrapper<Address>()
                .eq(Address::getUserId, userId)
                .orderByDesc(Address::getIsDefault));
    }

    @Override
    @Transactional
    public Address addAddress(Long userId, Address address) {
        address.setUserId(userId);
        if (Boolean.TRUE.equals(address.getIsDefault())) {
            clearDefault(userId);
        } else if (count(new LambdaQueryWrapper<Address>().eq(Address::getUserId, userId)) == 0) {
            address.setIsDefault(true);
        }
        save(address);
        return address;
    }

    @Override
    public void updateAddress(Long userId, Long addressId, Address address) {
        Address existing = getById(addressId);
        if (existing == null || !existing.getUserId().equals(userId)) {
            throw new RuntimeException("地址不存在");
        }
        if (Boolean.TRUE.equals(address.getIsDefault())) {
            clearDefault(userId);
        }
        address.setId(addressId);
        address.setUserId(userId);
        updateById(address);
    }

    @Override
    public void deleteAddress(Long userId, Long addressId) {
        remove(new LambdaQueryWrapper<Address>()
                .eq(Address::getId, addressId)
                .eq(Address::getUserId, userId));
    }

    @Override
    @Transactional
    public void setDefault(Long userId, Long addressId) {
        clearDefault(userId);
        update(new LambdaUpdateWrapper<Address>()
                .eq(Address::getId, addressId)
                .eq(Address::getUserId, userId)
                .set(Address::getIsDefault, true));
    }

    private void clearDefault(Long userId) {
        update(new LambdaUpdateWrapper<Address>()
                .eq(Address::getUserId, userId)
                .set(Address::getIsDefault, false));
    }
}
