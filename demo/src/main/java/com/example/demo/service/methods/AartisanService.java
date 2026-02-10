package com.example.demo.service.methods;

import com.example.demo.request.Artisan.ArtisanRequestDTO;
import com.example.demo.response.Artisan.ArtisanResponseDTO;
import com.example.demo.response.Artisan.ArtisanSaveResponse;

public interface AartisanService {

    ArtisanSaveResponse SaveArtisanDetails(ArtisanRequestDTO artisanResponseDTO);

    ArtisanResponseDTO getArtisanDetails(String jwt);

    ArtisanResponseDTO UpdateArtisanDetails(ArtisanRequestDTO artisanRequestDTO, String jwt);

}
