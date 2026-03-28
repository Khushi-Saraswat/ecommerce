package com.example.demo.service.methods;

import java.util.List;

import com.example.demo.request.Address.AddressDTO;
import com.example.demo.response.Address.AddressResponse;

public interface AddressService {

    // Save a new address
    AddressResponse saveAddress(AddressDTO addressDto);

    // Get all addresses for a user
    List<AddressDTO> getAddresses();

    // Update an address
    String updateAddress(Integer addressId, AddressDTO addressDto);

    // Delete an address (current user address context)
    Boolean deleteAddress(Integer addressId);

}

