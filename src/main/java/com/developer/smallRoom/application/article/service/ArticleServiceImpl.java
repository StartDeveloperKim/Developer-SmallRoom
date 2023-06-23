package com.developer.smallRoom.application.article.service;

import com.developer.smallRoom.domain.article.Article;
import com.developer.smallRoom.domain.article.repository.ArticleRepository;
import com.developer.smallRoom.domain.member.Member;
import com.developer.smallRoom.domain.member.repository.MemberRepository;
import com.developer.smallRoom.dto.article.request.ArticleRequest;
import com.developer.smallRoom.dto.article.request.ArticleUpdateRequest;
import com.developer.smallRoom.dto.article.response.ArticleResponse;
import com.developer.smallRoom.global.exception.auth.NotAuthorizationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class ArticleServiceImpl implements ArticleService{

    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;

    @Override
    public Long saveArticle(Long memberId, ArticleRequest request) {
        Member member = findMemberById(memberId);
        Article savedArticle = articleRepository.save(request.toArticle(member));
        return savedArticle.getId();
    }

    @Override
    public Long updateArticle(ArticleUpdateRequest request, Long memberId) {
        Article article = findArticleByArticleAndMember(request.getArticleId(), memberId);
        article.update(request);
        System.out.println("article.toString() = " + article.toString());
        return article.getId();
    }

    @Override
    public void deleteArticle(Long articleId, Long memberId) {
        Member member = findMemberById(memberId);
        if (articleRepository.existsByIdAndMember(articleId, member)) {
            articleRepository.deleteById(articleId);
        }else{
            throw new NotAuthorizationException("잘못된 접근입니다.");
        }
    }

    @Transactional(readOnly = true)
    @Override
    public ArticleResponse getArticleById(Long articleId) {
        Article article = findArticleById(articleId);
        return new ArticleResponse(article);
    }

    @Transactional(readOnly = true)
    @Override
    public ArticleResponse getArticleByIdAndMember(Long articleId, Long memberId) {
        return new ArticleResponse(findArticleByArticleAndMember(articleId, memberId));
    }

    private Article findArticleByArticleAndMember(Long articleId, Long memberId) {
        return articleRepository.findByIdAndMemberId(articleId, memberId)
                .orElseThrow(() -> new NotAuthorizationException("잘못된 접근입니다."));
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("not found memberId"));
    }

    private Article findArticleById(Long articleId) {
        return articleRepository.findById(articleId).orElseThrow(() -> new IllegalArgumentException("not fount articleId"));
    }

}
