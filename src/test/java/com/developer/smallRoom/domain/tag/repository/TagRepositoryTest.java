package com.developer.smallRoom.domain.tag.repository;

import com.developer.smallRoom.domain.tag.Tag;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TagRepositoryTest {

    @Autowired
    private TagRepository tagRepository;

    @BeforeEach
    void setUp() {
        tagRepository.deleteAll();
    }

    @DisplayName("findTagByName() : 태그이름으로 태그엔티티를 조회한다.")
    @Test
    void findTagByName() {
        //given
        String name = "test";
        Tag savedTag = tagRepository.save(new Tag(name));
        //when
        Optional<Tag> findTag = tagRepository.findTagByName(name);

        //then
        assertThat(findTag.get().getName()).isEqualTo(name);
    }
}