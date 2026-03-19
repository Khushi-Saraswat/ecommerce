package com.example.demo.service.impl;

import java.time.LocalDateTime;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.Common.AbstractMapperService;
import com.example.demo.constants.KycStatus;
import com.example.demo.constants.errorTypes.UserErrorType;
import com.example.demo.exception.User.UserException;
import com.example.demo.model.Artisan;
import com.example.demo.model.User;
import com.example.demo.repository.ArtisanRepository;
import com.example.demo.request.Artisan.ArtisanRequestDTO;
import com.example.demo.response.Artisan.ArtisanDetailsDto;
import com.example.demo.response.Artisan.ArtisanKYCStatus;
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

    @Autowired
    private ModelMapper mapper;

    @Override
    public ArtisanSaveResponse SaveArtisanDetails(ArtisanRequestDTO artisanrequestDTO) {

        ArtisanSaveResponse a;
        // Validate request
        if (artisanrequestDTO == null) {
            throw new UserException("artisan is null", UserErrorType.USER_NULL);

        }

        User user = authService.getCurrentUser();

        System.out.println(user + "****************** in save profile..");

        System.out.println(artisanRepository.findByUser_UserId(user.getUserId()).isPresent() + "id");
        // Prevent duplicate artisan for same user
        if (artisanRepository.findByUser_UserId(user.getUserId()).isPresent()) {
            throw new UserException(
                    "Artisan profile already exists for this user", UserErrorType.PROFILE_ALREADY_EXIST);

        }

        // Build entity explicitly

        // Artisan artisan = abstractMapperService.toEntity(artisanrequestDTO,
        // Artisan.class);
        // System.out.println(artisan + "******************");

        Artisan artisan = mapper.map(artisanrequestDTO, Artisan.class);
        System.out.println(artisan + "get artisan response");
        artisan.setUser(user);
        artisan.setKycStatus(KycStatus.PENDING);

        try {

            artisanRepository.save(artisan);
            a = new ArtisanSaveResponse();
            a.setMessage("waiting for admin approval");
            a.setCreatedAt(LocalDateTime.now());

        } catch (Exception ex) {

            ex.printStackTrace();
            throw new UserException(
                    "Failed to save artisan profile", UserErrorType.REGISTRATION_FAILED);
        }

        return a;

    }

    @Transactional
    @Override
    public ArtisanResponseDTO getArtisanDetails() {

        User user = authService.getCurrentUser();
        System.out.println(user + " ************ artisan user");

        Artisan artisan = artisanRepository
                .findByUser_UserId(user.getUserId())
                .orElseThrow(() -> new UserException(
                        "Artisan profile not found",
                        UserErrorType.PROFILE_INCOMPLETE));

        System.out.println(artisan + " ************ artisan entity");

        ArtisanResponseDTO artisanResponseDTO = abstractMapperService.toDto(artisan, ArtisanResponseDTO.class);

        System.out.println(artisanResponseDTO + " get artisan response");

        return artisanResponseDTO;
    }

    @Transactional
    @Override
    public ArtisanResponseDTO UpdateArtisanDetails(ArtisanRequestDTO artisanRequestDTO) {
        System.out.println(artisanRequestDTO + " artisanRequestDto in update");

        // 1. Authenticate user
        User user = authService.getCurrentUser();
        System.out.println(user + " user in update");

        // 2. Fetch artisan
        Artisan oldArtisan = artisanRepository
                .findByUser_UserId(user.getUserId())
                .orElseThrow(() -> new UserException(
                        "Artisan profile not found. Please complete your artisan registration first.",
                        UserErrorType.PROFILE_INCOMPLETE));

        System.out.println(oldArtisan + " oldArtisan");

        // 3. Update fields
        oldArtisan.setBrandName(artisanRequestDTO.getBrandName());
        oldArtisan.setArtisianType(artisanRequestDTO.getArtisianType().trim());
        oldArtisan.setBio(artisanRequestDTO.getBio());
        oldArtisan.setCity(artisanRequestDTO.getCity());
        oldArtisan.setState(artisanRequestDTO.getState());
        oldArtisan.setPincode(artisanRequestDTO.getPincode());

        // 4. Save
        Artisan savedArtisan = artisanRepository.save(oldArtisan);

        // 5. Return DTO
        return abstractMapperService.toDto(savedArtisan, ArtisanResponseDTO.class);

    }

    @Override
    public ArtisanDetailsDto ArtisanProfileExist() {

        ArtisanDetailsDto response = new ArtisanDetailsDto();
        User user = authService.getCurrentUser();

        Optional<Artisan> artisanOptional = artisanRepository.findByUser_UserId(user.getUserId());

        if (artisanOptional.isEmpty()) {

            response.setSetProfileExists(false);
            return response;

        }

        response.setSetProfileExists(true);
        return response;

    }

    @Transactional
    @Override
    public ArtisanKYCStatus ArtisanKYCStatus() {

        ArtisanKYCStatus status = new ArtisanKYCStatus();
        User user = authService.getCurrentUser();
        System.out.println("user :" + "" + user);

        Optional<Artisan> artisanOptional = artisanRepository.findByUser_UserId(user.getUserId());

        System.out.println("artisanOptional" + "" + artisanOptional);
        if (!artisanOptional.isEmpty()) {

            status.setKycStatus(artisanOptional.get().getKycStatus());
            System.out.println(status + "" + "status");
            return status;

        }

        System.out.println(status + "" + "status");
        return status;

    }
}
