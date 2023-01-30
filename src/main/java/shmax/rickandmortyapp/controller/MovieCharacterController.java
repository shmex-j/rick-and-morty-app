package shmax.rickandmortyapp.controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shmax.rickandmortyapp.dto.CharacterResponseDto;
import shmax.rickandmortyapp.dto.mapper.MovieCharacterMapper;
import shmax.rickandmortyapp.service.MovieCharacterService;

@RestController
@RequestMapping("/movie-characters")
public class MovieCharacterController {
    private final MovieCharacterService movieCharacterService;
    private final MovieCharacterMapper characterMapper;

    public MovieCharacterController(MovieCharacterService movieCharacterService,
                                    MovieCharacterMapper characterMapper) {
        this.movieCharacterService = movieCharacterService;
        this.characterMapper = characterMapper;
    }

    @GetMapping("/random")
    public CharacterResponseDto getRandom() {
        return characterMapper.mapToResponseDto(
                movieCharacterService.getRandomCharacter());
    }

    @GetMapping("/by-name")
    public List<CharacterResponseDto> getByName(@RequestParam String name) {
        return movieCharacterService.getByName(name)
                .stream()
                .map(characterMapper::mapToResponseDto)
                .toList();
    }
}
