package shmax.rickandmortyapp.dto.mapper;

import org.springframework.stereotype.Component;
import shmax.rickandmortyapp.dto.CharacterResponseDto;
import shmax.rickandmortyapp.dto.external.ApiCharacterDto;
import shmax.rickandmortyapp.model.Gender;
import shmax.rickandmortyapp.model.MovieCharacter;
import shmax.rickandmortyapp.model.Status;

@Component
public class MovieCharacterMapper {
    public MovieCharacter parseApiDto(ApiCharacterDto dto) {
        MovieCharacter movieCharacter = new MovieCharacter();
        movieCharacter.setName(dto.getName());
        movieCharacter.setStatus(Status.valueOf(dto.getStatus().toUpperCase()));
        movieCharacter.setGender(Gender.valueOf(dto.getGender().toUpperCase()));
        movieCharacter.setExternalId(dto.getId());
        return movieCharacter;
    }

    public CharacterResponseDto mapToResponseDto(MovieCharacter movieCharacter) {
        CharacterResponseDto dto = new CharacterResponseDto();
        dto.setId(movieCharacter.getId());
        dto.setName(movieCharacter.getName());
        dto.setGender(movieCharacter.getGender().getValue());
        dto.setStatus(movieCharacter.getStatus().getValue());
        return dto;
    }
}
