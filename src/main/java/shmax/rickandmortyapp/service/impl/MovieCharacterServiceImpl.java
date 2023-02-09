package shmax.rickandmortyapp.service.impl;

import jakarta.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import shmax.rickandmortyapp.dto.external.ApiCharacterDto;
import shmax.rickandmortyapp.dto.external.ApiResponseDto;
import shmax.rickandmortyapp.dto.mapper.MovieCharacterMapper;
import shmax.rickandmortyapp.model.MovieCharacter;
import shmax.rickandmortyapp.repository.MovieCharacterRepository;
import shmax.rickandmortyapp.service.HttpClient;
import shmax.rickandmortyapp.service.MovieCharacterService;

@Log4j2
@Service
public class MovieCharacterServiceImpl implements MovieCharacterService {
    private final HttpClient httpClient;
    private final MovieCharacterRepository movieCharacterRepository;
    private final MovieCharacterMapper movieCharacterMapper;

    public MovieCharacterServiceImpl(HttpClient httpClient,
                                     MovieCharacterRepository movieCharacterRepository,
                                     MovieCharacterMapper movieCharacterMapper) {
        this.httpClient = httpClient;
        this.movieCharacterRepository = movieCharacterRepository;
        this.movieCharacterMapper = movieCharacterMapper;
    }

    @Scheduled(cron = "0 0 8 * * *")
    @Override
    public void syncExternalCharacters() {
        ApiResponseDto dto = httpClient.get("https://rickandmortyapi.com/api/character",
                ApiResponseDto.class);
        while (dto.getInfo().getNext() != null) {
            saveDtosToDB(dto);
            dto = httpClient.get(dto.getInfo().getNext(),
                    ApiResponseDto.class);
        }
    }

    @Override
    public MovieCharacter getRandomCharacter() {
        long count = movieCharacterRepository.count();
        long randomId = new Random().nextLong(1, count + 1);
        return movieCharacterRepository.findById(randomId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "There are no character with id: " + randomId));
    }

    @Override
    public List<MovieCharacter> getByName(String name) {
        return movieCharacterRepository.findAllByNameContainsIgnoreCase(name);
    }

    void saveDtosToDB(ApiResponseDto dto) {
        Map<Long, ApiCharacterDto> characterDtos = Arrays.stream(dto.getResults())
                .collect(Collectors.toMap(ApiCharacterDto::getId, Function.identity()));
        Set<Long> externalIds = characterDtos.keySet();
        List<MovieCharacter> existingCharacters
                = movieCharacterRepository.findAllByExternalIdIn(externalIds);
        Map<Long, MovieCharacter> existingCharactersWithIds = existingCharacters.stream()
                .collect(Collectors.toMap(MovieCharacter::getExternalId, Function.identity()));
        Set<Long> existingIds = existingCharactersWithIds.keySet();
        externalIds.removeAll(existingIds);
        movieCharacterRepository.saveAll(externalIds.stream()
                .map(i -> movieCharacterMapper.parseApiDto(characterDtos.get(i)))
                .toList());
    }
}
