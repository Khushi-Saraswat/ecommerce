package com.example.demo.service.impl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Common.AbstractMapperService;
import com.example.demo.constants.KycStatus;
import com.example.demo.constants.Role;
import com.example.demo.constants.errorTypes.AuthErrorType;
import com.example.demo.constants.errorTypes.UserErrorType;
import com.example.demo.exception.Auth.AuthException;
import com.example.demo.exception.User.UserException;
import com.example.demo.model.Artisan;
import com.example.demo.model.User;
import com.example.demo.repository.ArtisanRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.request.Artisan.ArtisanRequestDTO;
import com.example.demo.response.Artisan.ArtisanResponseDTO;
import com.example.demo.response.Artisan.ArtisanSaveResponse;
import com.example.demo.service.methods.AartisanService;
import com.example.demo.service.methods.AuthService;
import com.example.demo.service.methods.UserService;

import jakarta.transaction.Transactional;

@Service
public class ArtisanServiceImpl implements AartisanService {

    @Autowired
    private ArtisanRepository artisanRepository;

    @Autowired
    private AbstractMapperService abstractMapperService;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @Override
    public ArtisanSaveResponse SaveArtisanDetails(ArtisanRequestDTO artisanrequestDTO) {

        ArtisanSaveResponse a;
        // Validate request
        if (artisanrequestDTO == null) {
            throw new UserException("artisan is null", UserErrorType.USER_NULL);

        }

        // Authentication authentication =
        // SecurityContextHolder.getContext().getAuthentication();
        // Authenticate user
        // User user = userService.getUserByJwt(jwt);
        // if (user == null) {
        // throw new AuthException("invalid or missing token",
        // AuthErrorType.TOKEN_INVALID);
        // }

        // Ensure role
        /*
         * if (user.getRole() == null || !user.getRole().equals(Role.ARTISAN)) {
         * 
         * throw new
         * UserException("User is not permitted to create an artisan profile. Only ARTISAN role is allowed."
         * ,
         * UserErrorType.UNAUTHORIZED);
         * 
         * }
         */

        User user = authService.getCurrentUser();

        System.out.println(user + "******************");

        // Prevent duplicate artisan for same user
        if (artisanRepository.findByUserUserId(user.getUserId()).isPresent()) {
            throw new UserException(
                    "Artisan profile already exists for this user", UserErrorType.PROFILE_ALREADY_EXIST);

        }

        // Build entity explicitly

        Artisan artisan = abstractMapperService.toEntity(artisanrequestDTO, Artisan.class);
        System.out.println(artisan + "******************");
        artisan.setUser(user);
        artisan.setKycStatus(KycStatus.PENDING);

        try {

            artisanRepository.save(artisan);
            a = new ArtisanSaveResponse();
            a.setMessage("waiting for admin approval");
            a.setCreatedAt(LocalDateTime.now());

        } catch (Exception ex) {
            throw new UserException(
                    "Failed to save artisan profile", UserErrorType.REGISTRATION_FAILED);
        }

        return a;

    }

    @Transactional
    @Override
    public ArtisanResponseDTO getArtisanDetails(String jwt) {

        User user = userService.getUserByJwt(jwt);
        Artisan artisan = artisanRepository.findById(user.getUserId()).orElse(null);
        System.out.println(artisan + "******************");
        return abstractMapperService.toDto(artisan, ArtisanResponseDTO.class);

    }

    @Override
    public ArtisanResponseDTO UpdateArtisanDetails(ArtisanRequestDTO artisanRequestDTO, String jwt) {
        // 1. Authenticate user
        User user = userService.getUserByJwt(jwt);
        if (user == null) {
            throw new AuthException("token is invalid or missing", AuthErrorType.TOKEN_INVALID);
        }

        // 2. Role check
        if (user.getRole() != Role.ARTISAN) {

            throw new AuthException("only artisan can update profile", AuthErrorType.UNAUTHORIZED_ACCESS);
        }

        // 3. Fetch existing artisan from DB (source of truth)
        Artisan oldArtisan = artisanRepository.findById(user.getUserId()).orElse(null);
        if (oldArtisan != null) { // Update fields from the request DTO
            oldArtisan.setBrandName(artisanRequestDTO.getBrandName());
            oldArtisan.setArtisianType(artisanRequestDTO.getArtisianType().trim());
            oldArtisan.setBio(artisanRequestDTO.getBio());
            oldArtisan.setCity(artisanRequestDTO.getCity());
            oldArtisan.setState(artisanRequestDTO.getState());
            oldArtisan.setPincode(artisanRequestDTO.getPincode()); // Save the updated artisan
            artisanRepository.save(oldArtisan);

            return abstractMapperService.toDto(oldArtisan, ArtisanResponseDTO.class);

        }

        throw new UserException(
                "Artisan profile not found. Please complete your artisan registration first.",
                UserErrorType.PROFILE_INCOMPLETE);

    }
}
