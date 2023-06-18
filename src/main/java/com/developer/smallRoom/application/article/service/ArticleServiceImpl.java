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
    public Long saveArticle(String memberId, ArticleRequest request) {
        Member member = findMemberById(memberId);
        Article savedArticle = articleRepository.save(request.toArticle(member));
        return savedArticle.getId();
    }

    @Override
    public Long updateArticle(ArticleUpdateRequest request, String memberId) {
        Member member = findMemberById(memberId);
        Article article = findArticleByIdAndMember(request.getArticleId(), member);
        article.update(request);
        return article.getId();
    }

    @Override
    public void deleteArticle(Long articleId, String memberId) {
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
    public ArticleResponse getArticleByIdAndMember(Long articleId, String memberGitHubId) {
        Member member = findMemberById(memberGitHubId);
        return new ArticleResponse(findArticleByIdAndMember(articleId, member));
    }

    private Article findArticleByIdAndMember(Long articleId, Member member) {
        return articleRepository.findByIdAndMember(articleId, member)
                .orElseThrow(() -> new NotAuthorizationException("잘못된 접근입니다."));
    }

    private Member findMemberById(String memberId) {
        return memberRepository.findByGitHubId(memberId).orElseThrow(() -> new IllegalArgumentException("not found memberId"));
    }

    private Article findArticleById(Long articleId) {
        return articleRepository.findById(articleId).orElseThrow(() -> new IllegalArgumentException("not fount articleId"));
    }

}
