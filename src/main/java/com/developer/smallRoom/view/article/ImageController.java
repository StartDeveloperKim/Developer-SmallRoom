package com.developer.smallRoom.view.article;

import com.developer.smallRoom.application.article.image.ImageStore;
import com.developer.smallRoom.application.auth.jwt.MemberPrincipal;
import com.developer.smallRoom.application.member.LoginMember;
import com.developer.smallRoom.dto.article.request.ImageRequest;
import com.developer.smallRoom.global.exception.auth.NotAuthorizationException;
import com.developer.smallRoom.global.exception.image.FailUploadImageAtS3;
import com.developer.smallRoom.global.exception.image.NotHaveImageException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/image")
public class ImageController {

    private final ImageStore imageStore;

    @PostMapping
    public ResponseEntity<String> imageUpload(@ModelAttribute ImageRequest request,
                                                     @LoginMember MemberPrincipal memberPrincipal) {
        if (memberPrincipal == null) throw new NotAuthorizationException(); // TODO :: 나중에 수정 필요 중복된 코드가 계속 발생함

        try {
            String imageUrl = imageStore.saveImage(request.getImage(), memberPrincipal.getUsername());
            log.info("ImageUrl : {}", imageUrl);
            return ResponseEntity.ok().body(imageUrl);
        } catch (FailUploadImageAtS3 | NotHaveImageException | IOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
