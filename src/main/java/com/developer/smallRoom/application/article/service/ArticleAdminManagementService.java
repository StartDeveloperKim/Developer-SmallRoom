package com.developer.smallRoom.application.article.service;

import com.developer.smallRoom.application.auth.jwt.MemberPrincipal;
import com.developer.smallRoom.domain.article.Article;
import com.developer.smallRoom.domain.article.repository.ArticleRepository;
import com.developer.smallRoom.domain.member.Member;
import com.developer.smallRoom.domain.member.repository.MemberRepository;
import com.developer.smallRoom.dto.article.request.ArticleRequest;
import com.developer.smallRoom.dto.article.request.ArticleUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
@Qualifier("ArticleAdminManagement")
public class ArticleAdminManagementService implements ArticleManagementService{

    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;

    @Override
    public Long saveArticle(MemberPrincipal memberPrincipal, ArticleRequest request) {
        Optional<Member> findMember = memberRepository.findByGitHubId(request.getGithubId());

        Member member;
        if (findMember.isEmpty())
            member = memberRepository.save(Member.builder().gitHubId(request.getGithubId()).build());
        else
            member = findMember.get();

        return articleRepository.save(request.toArticle(member)).getId();
    }

    @Override
    public Long updateArticle(ArticleUpdateRequest request, MemberPrincipal memberPrincipal) {
        Article article = articleRepository.findById(request.getArticleId()).get();
        article.update(request);
        return article.getId();
    }

    @Override
    public void deleteArticle(Long articleId, MemberPrincipal memberPrincipal) {
        articleRepository.deleteById(articleId);
    }
}
