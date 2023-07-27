package com.developer.smallRoom.application.autocomplete;

import com.developer.smallRoom.domain.tag.Tag;
import com.developer.smallRoom.domain.tag.repository.TagRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
@EnableScheduling
public class SearchAutoCompleteScheduler {

    private final AutoCompleteRepository autoCompleteRepository;
    private final TagRepository tagRepository;

    // TODO :: 지금은 PostConstruct로 설정했다.
    //  나중에는 PostConstruct 보다는 스케쥴러를 사용하자

    @PostConstruct
    public void setAutoCompleteWords() {
        final String KEY = "AUTO_COMPLETE";
        final double SCORE = 0.0;

        List<Tag> tags = tagRepository.findAll();
        for (Tag tag : tags) {
            String name = tag.getName();
            for (int l = 1; l < name.length() + 1; l++) {
                String prefix = name.substring(0, l);
                log.info("prefix : {}", prefix);
                autoCompleteRepository.save(KEY, prefix, SCORE);
            }
            autoCompleteRepository.save(KEY, name+"*", SCORE);
        }
    }
}
