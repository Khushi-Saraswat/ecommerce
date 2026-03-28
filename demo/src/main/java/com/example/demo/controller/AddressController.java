
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

import com.example.demo.request.Address.AddressDTO;
import com.example.demo.response.Address.AddressResponse;
import com.example.demo.service.methods.AddressService;

@RestController
@RequestMapping("/api/address")
public class AddressController {

  @Autowired
  private AddressService addressService;

  @PostMapping(value = "/addAddress", consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> addAddress(
      @RequestBody AddressDTO addressDTO) {

    AddressResponse addresponse = addressService.saveAddress(addressDTO);
    return ResponseEntity.ok(addresponse);
  }

  @GetMapping("/getAddress")
  public ResponseEntity<?> getAddress() {
    List<AddressDTO> addressDtos = addressService.getAddresses();
    return ResponseEntity.ok(addressDtos);

  }

  @PutMapping(value = "/updateAddress", consumes = org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<?> updateAddress(@RequestParam Integer addressId,
      @RequestBody AddressDTO addressDTO) {
    String message = addressService.updateAddress(addressId, addressDTO);
    return ResponseEntity.ok(message);
  }

  @DeleteMapping("/delAddress")
  public ResponseEntity<?> deleteAddress(@RequestParam Integer addressId) {
    Boolean f = addressService.deleteAddress(addressId);
    if (f)
      return ResponseEntity.ok("address is deleted successfully !!");

    else
      return ResponseEntity.ok("address is not deleted successfully !!");

  }

 

}
