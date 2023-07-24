package com.developer.smallRoom.global.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Aspect
public class LoginCheckAspect {

    @Pointcut("execution(* com.developer.smallRoom.view.article.ArticleViewController.articlePostingView(..)) || " +
            "execution(* com.developer.smallRoom.view.article.ArticleViewController.articleUpdateView(..))")
    public void articleView() {
    }

    @Pointcut("execution(* com.developer.smallRoom.view.article.ArticleController.postArticle(..))")
    public void postArticle() {
    }

    @Pointcut("execution(* com.developer.smallRoom.view.article.ArticleController.updateArticle(..))")
    public void updateArticle() {
    }

    @Pointcut("execution(* com.developer.smallRoom.view.article.ArticleController.removeArticle(..))")
    public void removeArticle() {
    }

    @Pointcut("execution(* com.developer.smallRoom.view.article.ImageController.*(..))")
    public void imageUpload() {
    }

    @Pointcut("execution(* com.developer.smallRoom.view.like.ArticleLikeController.*(..))")
    public void articleLike() {
    }

    @Around("articleView() || postArticle() || updateArticle() || removeArticle() || imageUpload() || articleLike()")
    public Object checkLoginAtSecurityContext(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof String && principal.equals("anonymousUser")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 접근입니다.");
        }
        log.info("LoginCheck AOP 동작");

        return proceedingJoinPoint.proceed();
    }
}
