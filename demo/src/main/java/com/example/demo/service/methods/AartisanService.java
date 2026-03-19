package com.example.demo.service.methods;

import com.example.demo.request.Artisan.ArtisanRequestDTO;
import com.example.demo.response.Artisan.ArtisanDetailsDto;
import com.example.demo.response.Artisan.ArtisanKYCStatus;
import com.example.demo.response.Artisan.ArtisanResponseDTO;
import com.example.demo.response.Artisan.ArtisanSaveResponse;

public interface AartisanService {

    ArtisanSaveResponse SaveArtisanDetails(ArtisanRequestDTO artisanResponseDTO);

    ArtisanResponseDTO getArtisanDetails();

    ArtisanResponseDTO UpdateArtisanDetails(ArtisanRequestDTO artisanRequestDTO);

    ArtisanDetailsDto ArtisanProfileExist();

    ArtisanKYCStatus ArtisanKYCStatus();

}
