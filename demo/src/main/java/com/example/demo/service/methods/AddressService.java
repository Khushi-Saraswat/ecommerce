package com.example.demo.service.methods;

import java.util.List;

import com.example.demo.dto.AddressDto;
import com.example.demo.response.AddressResponse;

public interface AddressService {

    // Save a new address
    AddressResponse saveAddress(String jwt, AddressDto addressDto);

    // Get all addresses for a user
    List<AddressDto> getAddresses(String jwt);

    // Get default address for a user
    AddressDto getDefaultAddressByUserId(Long userId);

    // Update an address
    String updateAddress(Integer addressid, String jwt, AddressDto addressDto);

    // Delete an address
    Boolean deleteAddress(String jwt);

    // Set an address as default
    Boolean setDefaultAddress(Long userId, Long addressId);

}
