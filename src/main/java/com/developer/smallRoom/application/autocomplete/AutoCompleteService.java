package com.developer.smallRoom.application.autocomplete;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@PropertySource("classpath:application.properties")
@Service
public class AutoCompleteService {

    private final AutoCompleteRepository autoCompleteRepository;
    private final String key;
    private final String suffix;

    public AutoCompleteService(AutoCompleteRepository autoCompleteRepository,
                               @Value("${autocomplete.key}") String key,
                               @Value("${autocomplete.suffix}") String suffix) {
        this.autoCompleteRepository = autoCompleteRepository;
        this.key = key;
        this.suffix = suffix;
    }

    public List<String> findAutoCompleteWords(String query) {
        final long limit = 100;

        Optional<Long> findIndex = autoCompleteRepository.findIndexByValue(key, query);

        if (findIndex.isPresent()) {
            long index = findIndex.get();
            return autoCompleteRepository.findWordsListByIndex(key, index, index + limit).stream()
                    .filter(word -> word.startsWith(query) && word.endsWith(suffix))
                    .map(filteringWord -> filteringWord.substring(0, filteringWord.length() - 1))
                    . collect(Collectors.toList()) ;
        }
        return new ArrayList<>();
    }
}
