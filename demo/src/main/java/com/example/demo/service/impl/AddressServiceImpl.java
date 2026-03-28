package com.example.demo.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Common.AbstractMapperService;
import com.example.demo.constants.errorTypes.AddressErrorType;
import com.example.demo.request.Address.AddressDTO;
import com.example.demo.exception.Address.AddressException;
import com.example.demo.model.Address;
import com.example.demo.model.User;
import com.example.demo.repository.AddressRepository;
import com.example.demo.response.Address.AddressResponse;
import com.example.demo.service.methods.AddressService;
import com.example.demo.service.methods.AuthService;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AuthService authService;

    @Autowired
    private AbstractMapperService abstractMapperService;

    @Autowired
    private AddressRepository addressRepository;

    @Override
    public Boolean deleteAddress(Integer addressId) {
        try {
            User user = authService.getCurrentUser();
            Address address = addressRepository
                    .findByUser(abstractMapperService.toEntity(user, User.class));
            if (address != null) {
                addressRepository.delete(address);
                return true;
            }
            throw new AddressException("No address found for user", AddressErrorType.ADDRESS_NOT_FOUND);
        } catch (AddressException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new AddressException("Error deleting address: " + ex.getMessage(),
                    AddressErrorType.ADDRESS_NOT_FOUND);
        }
    }

    @Override
    public List<AddressDTO> getAddresses() {
        try {
            User user = authService.getCurrentUser();
            User user2 = abstractMapperService.toEntity(user, User.class);
            List<Address> address = addressRepository.findAddressByUserId(user2.getUserId());

            if (address == null || address.isEmpty()) {
                throw new AddressException("No addresses found for user", AddressErrorType.NO_ADDRESSES_FOUND);
            }

            return address.stream().map(add -> {
                return abstractMapperService.toDto(add, AddressDTO.class);
            }).toList();
        } catch (AddressException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new AddressException("Error fetching addresses: " + ex.getMessage(),
                    AddressErrorType.NO_ADDRESSES_FOUND);
        }
    }

    @Override
    public AddressResponse saveAddress(AddressDTO addressDto) {

        if (addressDto == null) {
            throw new AddressException("Address data cannot be empty", AddressErrorType.ADDRESS_NOT_SAVED);
        }

        try {
            User user = authService.getCurrentUser();
            Address address = abstractMapperService.toEntity(addressDto, Address.class);
            address.setUser(abstractMapperService.toEntity(user, User.class));
            Address address2 = addressRepository.save(address);

            if (address2 != null) {
                AddressResponse addressResponse = new AddressResponse();
                addressResponse.setAddressId(address2.getId());
                addressResponse.setMessage("Address Saved Successfully");
                return addressResponse;
            } else {
                throw new AddressException("Failed to save address", AddressErrorType.ADDRESS_NOT_SAVED);
            }
        } catch (AddressException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new AddressException("Error saving address: " + ex.getMessage(), AddressErrorType.ADDRESS_NOT_SAVED);
        }
    }

    @Override
    public String updateAddress(Integer addressId, AddressDTO addressDto) {

        try {
            if (addressId == null || addressId <= 0) {
                throw new AddressException("Invalid address ID provided", AddressErrorType.INVALID_ADDRESS_ID);
            }

            if (addressDto == null) {
                throw new AddressException("Address data cannot be empty", AddressErrorType.ADDRESS_NOT_UPDATED);
            }

            User user = authService.getCurrentUser();

            Optional<Address> optionalAddress = addressRepository.findById(addressId);

            // 1. Check if address exists
            if (optionalAddress.isEmpty()) {
                throw new AddressException("Address not found with ID: " + addressId,
                        AddressErrorType.ADDRESS_NOT_FOUND);
            }

            // get optional address...
            Address existingAddress = optionalAddress.get();

            // 2. Check ownership
            if (!existingAddress.getUser().getUserId().equals(user.getUserId())) {
                throw new AddressException("User is not authorized to update this address",
                        AddressErrorType.USER_NOT_AUTHORIZED);
            }

            // 3. Update fields
            existingAddress.setAddressName(addressDto.getAddressName());
            existingAddress.setAddressLandMark(addressDto.getAddressLandMark());
            existingAddress.setAddressState(addressDto.getAddressState());
            existingAddress.setAddressPhoneNumber(addressDto.getAddressPhoneNumber());
            existingAddress.setAddressZipCode(addressDto.getAddressZipCode());
            

            // 4. Save
            Address saved = addressRepository.save(existingAddress);

            // 5. Return result
            if (saved != null) {
                return "Address updated successfully";
            } else {
                throw new AddressException("Failed to update address", AddressErrorType.ADDRESS_NOT_UPDATED);
            }
        } catch (AddressException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new AddressException("Error updating address: " + ex.getMessage(),
                    AddressErrorType.ADDRESS_NOT_UPDATED);
        }
    }

}