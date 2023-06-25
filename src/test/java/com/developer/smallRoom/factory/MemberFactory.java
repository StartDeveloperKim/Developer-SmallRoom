package com.developer.smallRoom.factory;

import com.developer.smallRoom.domain.member.Member;
import lombok.Builder;

public class MemberFactory {

    private String githubId = "testGithubId";
    private String name = "testName";
    private String imageUrl = "testImageUrl";

    @Builder
    public MemberFactory(String githubId, String name, String imageUrl) {
        this.githubId = githubId != null ? githubId : this.githubId;
        this.name = name != null ? name : this.name;
        this.imageUrl = imageUrl != null ? imageUrl : this.imageUrl;
    }

    public static Member getMemberDefaultValue() {
        return MemberFactory.builder().build().getMember();
    }

    public Member getMember() {
        return Member.builder()
                .gitHubId(this.githubId)
                .name(this.name)
                .imageUrl(this.imageUrl)
                .build();
    }
}
