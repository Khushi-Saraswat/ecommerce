package com.example.demo.service.methods;

import com.example.demo.request.ArtisanRequestDTO;
import com.example.demo.response.ArtisanResponseDTO;
import com.example.demo.response.ArtisanSaveResponse;

public interface AartisanService {

    ArtisanSaveResponse SaveArtisanDetails(ArtisanRequestDTO artisanResponseDTO);

    ArtisanResponseDTO getArtisanDetails(String jwt);

    ArtisanResponseDTO UpdateArtisanDetails(ArtisanRequestDTO artisanRequestDTO, String jwt);

}
