package com.jd.controller;

import com.jd.common.Result;
import com.jd.common.UserContext;
import com.jd.entity.Address;
import com.jd.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @GetMapping
    public Result<?> list() {
        return Result.success(addressService.getUserAddresses(UserContext.getUserId()));
    }

    @PostMapping
    public Result<?> add(@RequestBody Address address) {
        return Result.success("添加成功", addressService.addAddress(UserContext.getUserId(), address));
    }

    @PutMapping("/{id}")
    public Result<?> update(@PathVariable Long id, @RequestBody Address address) {
        addressService.updateAddress(UserContext.getUserId(), id, address);
        return Result.success("更新成功", null);
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        addressService.deleteAddress(UserContext.getUserId(), id);
        return Result.success("删除成功", null);
    }

    @PutMapping("/{id}/default")
    public Result<?> setDefault(@PathVariable Long id) {
        addressService.setDefault(UserContext.getUserId(), id);
        return Result.success("设置成功", null);
    }
}
