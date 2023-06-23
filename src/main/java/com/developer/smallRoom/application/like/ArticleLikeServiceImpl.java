package com.developer.smallRoom.application.like;

import com.developer.smallRoom.domain.article.Article;
import com.developer.smallRoom.domain.article.repository.ArticleRepository;
import com.developer.smallRoom.domain.like.ArticleLike;
import com.developer.smallRoom.domain.like.ArticleLikeRepository;
import com.developer.smallRoom.domain.member.Member;
import com.developer.smallRoom.domain.member.repository.MemberRepository;
import com.developer.smallRoom.dto.like.response.ArticleLikeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class ArticleLikeServiceImpl implements ArticleLikeService{

    private final ArticleRepository articleRepository;
    private final MemberRepository memberRepository;
    private final ArticleLikeRepository articleLikeRepository;

    @Transactional(readOnly = true)
    @Override
    public boolean existArticleLike(Long articleId, Long memberId) {
        Article article = getArticleById(articleId);
        Member member = getMemberById(memberId);
        return articleLikeRepository.existsArticleLikeByArticleAndMember(article, member);
    }

    @Override
    public ArticleLikeResponse saveArticleLike(Long articleId, Long memberId) {
        /*
        * TODO :: 나중에 성능테스트를 하고 좋아요 개수 최적화 작업을 하자
        * */
        Article article = getArticleById(articleId);
        Member member = getMemberById(memberId);

        boolean result = articleLikeRepository.existsArticleLikeByArticleAndMember(article, member);
        if (!result) {
            articleLikeRepository.save(new ArticleLike(article, member));
            return new ArticleLikeResponse(true);
        }

        return new ArticleLikeResponse(false);
    }

    @Override
    public ArticleLikeResponse removeArticleLike(Long articleId, Long memberId) {
        Article article = getArticleById(articleId);
        Member member = getMemberById(memberId);

        Optional<ArticleLike> articleLike = articleLikeRepository.findByArticleAndMember(article, member);
        if (articleLike.isPresent()) {
            articleLikeRepository.delete(articleLike.get());
            return new ArticleLikeResponse(true);
        }
        return new ArticleLikeResponse(false);
    }

    private Article getArticleById(Long articleId) {
        return articleRepository.findById(articleId).orElseThrow(() -> new IllegalArgumentException("없는 게시물입니다."));
    }

    private Member getMemberById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
    }

}
