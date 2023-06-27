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
//        String[] frontendTechStack = {"React", "Angular", "Vue.js", "Ember.js", "Svelte", "JSP", "Thymeleaf", "Mustache"};
//        String[] infrastructureTechStack = {"AWS", "Google Cloud Platform", "Azure", "Docker", "Kubernetes", "Nginx", "Apache", "Redis", "MongoDB", "PostgreSQL"
//                , "Oracle", "MySQL", "Redis", "Memcached"};
//
//        saveTag(programmingLanguages);
//        saveTag(backendTechStack);
//        saveTag(frontendTechStack);
//        saveTag(infrastructureTechStack);
//    }
//    private void saveTag(String[] tags) {
//        for (String tag : tags) {
//            if (!tagRepository.existsByName(tag)){
//                tagRepository.save(new Tag(tag));
//            }
//        }
//    }
}
