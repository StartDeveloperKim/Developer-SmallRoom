package com.developer.smallRoom.application.tag;

import com.developer.smallRoom.domain.tag.Tag;
import com.developer.smallRoom.domain.tag.repository.TagRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TagService {

    private final TagRepository tagRepository;

//    @PostConstruct
//    public void tagInitialize() {
//        String[] programmingLanguages = {"JavaScript", "Python", "Java", "C++", "C#", "Ruby", "Go", "Swift",
//                "Kotlin", "TypeScript", "Rust", "PHP", "Perl", "HTML", "CSS"};
//        String[] backendTechStack = {"Node.js", "Express", "Ruby on Rails", "Django", "ASP.NET", "Spring Boot", "Flask", "Laravel"};
//        String[] frontendTechStack = {"React", "Angular", "Vue.js", "Ember.js", "Svelte"};
//        String[] infrastructureTechStack = {"AWS", "Google Cloud Platform", "Azure", "Docker", "Kubernetes", "Nginx", "Apache", "Redis", "MongoDB", "PostgreSQL"};
//
//        for (String programmingLanguage : programmingLanguages) {
//            tagRepository.save(new Tag(programmingLanguage));
//        }
//        for (String s : backendTechStack) {
//            tagRepository.save(new Tag(s));
//        }
//        for (String s : frontendTechStack) {
//            tagRepository.save(new Tag(s));
//        }
//        for (String s : infrastructureTechStack) {
//            tagRepository.save(new Tag(s));
//        }
//    }
}
