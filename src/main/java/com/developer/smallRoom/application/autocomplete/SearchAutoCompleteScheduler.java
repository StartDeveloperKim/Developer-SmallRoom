package com.developer.smallRoom.application.autocomplete;

import com.developer.smallRoom.domain.tag.Tag;
import com.developer.smallRoom.domain.tag.repository.TagRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@EnableScheduling
@PropertySource("classpath:application.properties")
@Component
public class SearchAutoCompleteScheduler {

    private final AutoCompleteRepository autoCompleteRepository;
    private final TagRepository tagRepository;

    private final String key;
    private final String suffix;

    // TODO :: 지금은 PostConstruct로 설정했다.
    //  나중에는 PostConstruct 보다는 스케쥴러를 사용하자
    //  매일 새벽 3시로 지정하자

    @Autowired
    public SearchAutoCompleteScheduler(AutoCompleteRepository autoCompleteRepository,
                                       TagRepository tagRepository,
                                       @Value("${autocomplete.key}") String key,
                                       @Value("${autocomplete.suffix}") String suffix) {
        this.autoCompleteRepository = autoCompleteRepository;
        this.tagRepository = tagRepository;
        this.key = key;
        this.suffix = suffix;
    }

    @Scheduled(cron = "0 0 3 * * *", zone = "Asia/Seoul")
    public void setAutoCompleteWords() {
        final double SCORE = 0.0;

        List<Tag> tags = tagRepository.findAll();
        List<String> autoCompleteList = new ArrayList<>();

        for (Tag tag : tags) {
            String name = tag.getName();
            for (int l = 1; l < name.length() + 1; l++) {
                String prefix = name.substring(0, l);
                autoCompleteList.add(prefix);
                log.info("prefix : {}", prefix);
            }
            autoCompleteList.add(name + suffix);
        }
        autoCompleteRepository.bulkSave(key, autoCompleteList, SCORE);

    }
}
