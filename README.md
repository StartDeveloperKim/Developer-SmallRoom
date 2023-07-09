# 토이프로젝트 저장소
## 주제 선정 이유

개발자 준비를 하면서 여러사람들의 개인 프로젝트, 토이 프로젝트를 참고하거나 구경하려면 구글링을 통해 하나하나 찾는방법 밖에 없었다. 이러한 방법은 시간적인 소요가 너무 많이 들었다. 그래서 이러한 개인프로젝트, 토이프로젝트가 한 곳에 모여있으면 어떨까하는 생각을 해보았고 사람들이 이를 포스팅할 수 있는 플랫폼이 있으면 어떨까하는 생각에 이러한 주제를 선정하게 되었다.

## 개발환경
- IntelliJ
-   Postman
-   GitHub
-   SourceTree
-   H2
-   Visual Studio Code

## 사용기술

### “Backend”

-   Spring Boot 3.0.7, Java17
    -   스프링부터3버전 이상부터는 Java17버전이상만 가능하다.
-   Spring Security, JWT, OAuth2
-   SpringData JPA,
-   H2, MariaDB

### “Frontend”

-   HTML, CSS, Javascript, JQuery, Tailwind CSS
-   Toast Editor, Tagify

### “Infra”

-   AWS EC2, S3, CodeDeploy, GithubAction
## 주요기능
#### Github OAuth + JWT(AccessToken+RefreshToken)을 활용한 로그인 구현
AWS 프리티어 사용자인 현 상황을 고려하여 OAuth와 JWT를 활용하여 로그인을 구현했습니다. 이전 블로그 프로젝트에서는 AccessToken만을 활용했는데 보안을 고려하여 RefreshToken도 함께 구현하였습니다.

프론트엔드가 SSR이기 때문에 쿠키로서 클라이언트에게 전달했습니다. 또한 HttpOnly 설정을 활성화해서 자바스크립트를 통한 쿠키 탈취문제를 예방했습니다.

#### 게시글 CRUD
게시글 작성은 Toast Editor를 사용해서 마크다운형식으로 작성할 수 있도록 했습니다.


#### S3를 이미지 저장소로 사용
썸네일 또는 게시글에 포함되는 이미지를 AWS의 S3 서버에 저장하도록하여 DB에는 이미지의 URL만 저장되도록 구성하였습니다.

#### 태그
프론트엔드 단에서는 Tagify를 사용하여 태그를 등록할 수 있도록하였고 지정된 태그가 아니면 사용자는 임의로 태그를 등록할 수 없습니다. 

#### 댓글
깃허브 이슈를 기반으로 댓글을 달 수 있게 만들어주는 깃허브 앱이 Utterances를 활용하여 구현하였습니다. User는 깃허브 계정이 있다면 댓글을 작성할 수 있습니다.

#### 좋아요
게시글에 좋아요를 가능하게 했습니다. 

메인페이지에 좋아요개수를 나타낼 필요가 있는데 이를 매번 count하기 힘들기 때문에 게시글 테이블에 좋아요개수 컬럼을 두고 이를 보여주는 방식으로 성능 향상을 이끌어 냈습니다.

하지만 이 경우 데이터 정합성 문제가 발생할 수도 있기에 배치 작업을 통해 사용자가 적은 시간에 그 둘의 데이터를 맞추는 로직이 필요합니다.

#### 무한스크롤
제이쿼리의 scroll()을 통한 무한스크롤을 구현했습니다. 메인 페이지에서는 스크롤 위치에 따라 4개의 게시글을 읽어오며 4개이하의 게시글이 조회되면 모든 글을 읽어왔기에 무한스크롤이 종료됩니다.
