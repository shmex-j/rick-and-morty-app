package shmax.rickandmortyapp.service;

import java.util.List;
import shmax.rickandmortyapp.model.MovieCharacter;

public interface MovieCharacterService {
    void syncExternalCharacters();

    MovieCharacter getRandomCharacter();

    List<MovieCharacter> getByName(String name);
}
