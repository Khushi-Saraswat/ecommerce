package com.example.demo.service.methods;

import java.util.List;

import com.example.demo.dto.AddressDto;
import com.example.demo.response.Address.AddressResponse;

public interface AddressService {

    // Save a new address
    AddressResponse saveAddress(AddressDto addressDto);

    // Get all addresses for a user
    List<AddressDto> getAddresses();

    // Get default address for a user
    AddressDto getDefaultAddressByUserId(Long userId);

    // Update an address
    String updateAddress(Integer addressid, AddressDto addressDto);

    // Delete an address
    Boolean deleteAddress();

    // Set an address as default
    Boolean setDefaultAddress(Long userId, Long addressId);

}
