package com.developer.smallRoom.application.article.service;

import com.developer.smallRoom.application.auth.jwt.MemberPrincipal;
import com.developer.smallRoom.domain.article.Article;
import com.developer.smallRoom.domain.article.repository.ArticleRepository;
import com.developer.smallRoom.domain.member.Member;
import com.developer.smallRoom.domain.member.repository.MemberRepository;
import com.developer.smallRoom.dto.article.request.ArticleRequest;
import com.developer.smallRoom.dto.article.request.ArticleUpdateRequest;
import com.developer.smallRoom.global.exception.auth.NotAuthorizationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
@Qualifier("ArticleUserManagement")
public class ArticleUserManagementService implements ArticleManagementService{

    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;

    @Override
    public Long saveArticle(MemberPrincipal memberPrincipal, ArticleRequest request) {
        Member member = findMemberById(memberPrincipal.getMemberId());
        Article savedArticle = articleRepository.save(request.toArticle(member));
        return savedArticle.getId();
    }

    @Override
    public Long updateArticle(ArticleUpdateRequest request, MemberPrincipal memberPrincipal) {
        Article article = getArticleByUpdateRequest(request, memberPrincipal);
        article.update(request);

        return article.getId();
    }

    private Article getArticleByUpdateRequest(ArticleUpdateRequest request, MemberPrincipal memberPrincipal) {
        return articleRepository.findByIdAndMemberId(request.getArticleId(), memberPrincipal.getMemberId())
                .orElseThrow(() -> new NotAuthorizationException("잘못된 접근입니다."));
    }

    @Override
    public void deleteArticle(Long articleId, MemberPrincipal memberPrincipal) {
        Member member = findMemberById(memberPrincipal.getMemberId());
        if (isExistsByIdAndMember(articleId, member))
            articleRepository.deleteById(articleId);
        else
            throw new NotAuthorizationException("잘못된 접근입니다.");
    }
    private boolean isExistsByIdAndMember(Long articleId, Member member) {
        return articleRepository.existsByIdAndMember(articleId, member);
    }

    private Member findMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("not found memberId"));
    }
}
