package shmax.rickandmortyapp.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import shmax.rickandmortyapp.dto.external.ApiCharacterDto;
import shmax.rickandmortyapp.dto.external.ApiResponseDto;
import shmax.rickandmortyapp.dto.mapper.MovieCharacterMapper;
import shmax.rickandmortyapp.model.Gender;
import shmax.rickandmortyapp.model.MovieCharacter;
import shmax.rickandmortyapp.model.Status;
import shmax.rickandmortyapp.repository.MovieCharacterRepository;
import shmax.rickandmortyapp.service.HttpClient;

class MovieCharacterServiceImplTest {
    private List<MovieCharacter> persistenceCharacters;
    private MovieCharacterServiceImpl movieCharacterService;
    @Mock
    private MovieCharacterRepository movieCharacterRepository;

    @BeforeEach
    void setUp() {
        movieCharacterRepository = Mockito.mock(MovieCharacterRepository.class);
        movieCharacterService = new MovieCharacterServiceImpl(
                new HttpClient(new ObjectMapper()),
                movieCharacterRepository,
                new MovieCharacterMapper()
        );

        persistenceCharacters = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            MovieCharacter movieCharacter = new MovieCharacter();
            movieCharacter.setId((long) i);
            movieCharacter.setName("Character#" + i);
            movieCharacter.setStatus(Status.UNKNOWN);
            movieCharacter.setExternalId((long) i);
            movieCharacter.setGender(Gender.UNKNOWN);
            persistenceCharacters.add(movieCharacter);
        }
    }

    @Test
    void saveDtosToDB_ok() {
        List<ApiCharacterDto> externalCharacters = new ArrayList<>();
        for (int i = 5; i <= 15; i++) {
            ApiCharacterDto movieCharacter = new ApiCharacterDto();
            movieCharacter.setName("Character#" + i);
            movieCharacter.setStatus("UNKNOWN");
            movieCharacter.setId((long) i);
            movieCharacter.setGender("UNKNOWN");
            externalCharacters.add(movieCharacter);
        }
        List<Long> externalIds = externalCharacters.stream()
                .map(ApiCharacterDto::getId)
                .toList();

        Mockito.when(movieCharacterRepository.findAllByExternalIdIn(
                externalCharacters.stream().map(ApiCharacterDto::getId)
                        .collect(Collectors.toSet())
        )).thenReturn(persistenceCharacters.stream()
                .filter(persistenceCharacter
                        -> externalIds.contains(persistenceCharacter.getExternalId()))
                .toList());
        Mockito.doAnswer(invocation -> {
            List<MovieCharacter> arg = invocation.getArgument(0);
            persistenceCharacters.addAll(arg);
            return null;
        }).when(movieCharacterRepository).saveAll(any());

        ApiResponseDto responseDto = new ApiResponseDto();
        responseDto.setResults(externalCharacters.toArray(new ApiCharacterDto[0]));
        movieCharacterService.saveDtosToDB(responseDto);
        int expectedSize = 15;
        int actualSize = persistenceCharacters.size();

        assertEquals(expectedSize, actualSize,
                "Test failed expected persistence size: "
                + expectedSize + ", but was " + actualSize);
    }
}