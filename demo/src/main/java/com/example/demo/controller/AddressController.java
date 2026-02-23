
package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.AddressDto;
import com.example.demo.response.Address.AddressResponse;
import com.example.demo.service.methods.AddressService;

@RestController
@RequestMapping("/api/address")
public class AddressController {

  @Autowired
  private AddressService addressService;

  @PostMapping(value = "/addAddress", consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> addAddress(
      @RequestBody AddressDto addressDto) {

    AddressResponse addresponse = addressService.saveAddress(addressDto);
    return ResponseEntity.ok(addresponse);
  }

  @GetMapping("/getAddress")
  public ResponseEntity<?> getAddress() {
    List<AddressDto> addressDtos = addressService.getAddresses();
    return ResponseEntity.ok(addressDtos);

  }

  @PutMapping(value = "/updateAddress", consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> updateAddress(@RequestParam Integer addressId,
      @RequestBody AddressDto addressDto) {
    String message = addressService.updateAddress(addressId, addressDto);
    return ResponseEntity.ok(message);
  }

  @DeleteMapping("/delAddress")
  public ResponseEntity<?> deleteAddress() {
    Boolean f = addressService.deleteAddress();
    if (f)
      return ResponseEntity.ok("address is deleted successfully !!");

    else
      return ResponseEntity.ok("address is not deleted successfully !!");

  }

  // Get default address for a user
  @GetMapping("/getDefaultAddress")
  public ResponseEntity<?> getDefaultAddress(@RequestParam Long userId) {
    return ResponseEntity.ok(addressService.getDefaultAddressByUserId(userId));
  }

  // Set an address as default
  @PutMapping("/setDefaultAddress")
  public ResponseEntity<?> setDefaultAddress(
      @RequestParam Long userId,
      @RequestParam Long addressId) {
    Boolean success = addressService.setDefaultAddress(userId, addressId);
    if (success) {
      return ResponseEntity.ok("Address set as default successfully");
    } else {
      return ResponseEntity.status(400).body("Failed to set address as default");
    }
  }

}
