INSERT INTO member (MEMBER_ID, CREATE_AT, GIT_HUB_ID, IMAGE_URL, NAME, ROLE)
VALUES
(1, '2023-06-26 15:39:11.054002', 'StartDeveloperKim', 'https://avatars.githubusercontent.com/u/97887047?v=4', 'Kims', 'ADMIN');

INSERT INTO article (ARTICLE_ID, CONTENT, CREATE_AT, GITHUB_LINK, HIT, SUB_TITLE, TAGS, THUMBNAIL_URL, TITLE, MEMBER_ID, LIKE_COUNT)
VALUES
(1, '<h2>안녕하세요1</h2>', '2023-06-26 15:39:32.646494', '안녕하세요1', 7, '안녕하세요1', 'Python,Kotlin,PHP,CSS,Express', 'https://yozm.wishket.com/media/news/1674/image001.png', '안녕하세요1', 1, 15),
(2, '<h2>안녕하세요2</h2>', '2023-06-26 15:40:00.131184', '안녕하세요2', 12, '안녕하세요2', 'JavaScript,Go,C++,Rust,HTML', 'https://images.velog.io/images/youngerjesus/post/74ba448d-59f7-486f-b4bf-702e8e124fdd/java.png', '안녕하세요2', 1, 20),
(3, '<h2>안녕하세요3</h2>', '2023-06-26 15:40:26.1019', '안녕하세요3', 8, '안녕하세요3', 'JavaScript,Go,Rust,C++,TypeScript', 'https://velog.velcdn.com/images/ohzzi/post/bf15baae-bb0a-4383-be10-7110b37be1b7/image.png', '안녕하세요3', 1, 30),
(4, '<h2>안녕하세요4</h2>', '2023-06-26 15:41:15.733002', '안녕하세요4', 3, '안녕하세요4', 'JavaScript,C#,Kotlin,HTML,Go', 'https://velog.velcdn.com/images/ohzzi/post/95cf2c8d-965b-4452-8da5-c38e3cc147dd/image.png', '안녕하세요4', 1, 110),
(5, '<h2>안녕하세요5</h2>', '2023-06-26 15:41:32.166842', '안녕하세요5', 17, '안녕하세요5', 'JavaScript,C#,Kotlin,Rust,Express', 'https://velog.velcdn.com/images/ohzzi/post/3c5302d9-1e7a-4e5e-a7c5-6faee4458644/image.png', '안녕하세요5', 1, 5),
(6, '<p>안녕하세요6</p>', '2023-06-26 15:41:48.863614', '안녕하세요6', 15, '안녕하세요6', 'JavaScript,C#,Kotlin,Perl,TypeScript,Express', 'https://velog.velcdn.com/images/ohzzi/post/b92caeb5-73fd-4e6e-88a3-61d5cb17d558/image.png', '안녕하세요6', 1, 124),
(7, '<p>asdasdasdas</p>', '2023-06-27 19:48:28.088075', '안녕하세요7', 8, '안녕하세요7', 'JavaScript,C#,TypeScript,HTML,Express', 'https://velog.velcdn.com/images/ohzzi/post/c0e5b992-3f07-4ec5-8529-72bfeeb90549/image.jpeg', 'ㅁㄴㅇㅁㄴㅇㅁㄴㄴ7', 1, 15),
(8, '<p>asdasd</p>', '2023-06-27 19:49:41.564023', '안녕하세요8', 8, '안녕하세요8', 'JavaScript,C++,Ruby,Kotlin,TypeScript', 'https://velog.velcdn.com/images/ohzzi/post/b355c997-785e-4493-b71e-f19179d85c5a/image.png', 'ㅁㄴㅇㅁㄴㅇㅁㄴㄴ8', 1, 456),
(9, '<p>asdasd</p>', '2023-06-27 19:49:41.564023', '안녕하세요9', 8, '안녕하세요9', 'JavaScript,Kotlin,CSS,TypeScript,Ruby', 'https://velog.velcdn.com/images/ohzzi/post/0980fac3-7791-4fc0-bd89-007b1dc1472b/image.png', 'ㅁㄴㅇㅁㄴㅇㅁㄴㄴ9', 1, 10);


INSERT INTO tag (TAG_ID, TAG_NAME)
VALUES
(1, 'JavaScript'),
(2, 'Python'),
(3, 'Java'),
(4, 'C++'),
(5, 'C#'),
(6, 'Ruby'),
(7, 'Go'),
(8, 'Swift'),
(9, 'Kotlin'),
(10, 'TypeScript'),
(11, 'Rust'),
(12, 'PHP'),
(13, 'Perl'),
(14, 'HTML'),
(15, 'CSS'),
(16, 'Node.js'),
(17, 'Express'),
(18, 'Ruby on Rails'),
(19, 'Django'),
(20, 'ASP.NET'),
(21, 'Spring Boot'),
(22, 'Flask'),
(23, 'Laravel'),
(24, 'React'),
(25, 'Angular'),
(26, 'Vue.js'),
(27, 'Ember.js'),
(28, 'Svelte'),
(29, 'JSP'),
(30, 'Thymeleaf'),
(31, 'Mustache'),
(32, 'AWS'),
(33, 'Google Cloud Platform'),
(34, 'Azure'),
(35, 'Docker'),
(36, 'Kubernetes'),
(37, 'Nginx'),
(38, 'Apache'),
(39, 'Redis'),
(40, 'MongoDB'),
(41, 'PostgreSQL'),
(42, 'Oracle'),
(43, 'MySQL'),
(44, 'Memcached');


INSERT INTO board_tag (BOARD_TAG_ID, ARTICLE_ID, TAG_ID)
VALUES
(59, 8, 1),
(60, 8, 4),
(61, 8, 6),
(62, 8, 9),
(63, 8, 10),
(64, 9, 1),
(65, 9, 9),
(66, 9, 15),
(67, 9, 10),
(68, 9, 6),
(69, 7, 1),
(70, 7, 5),
(71, 7, 10),
(72, 7, 14),
(73, 7, 17),
(74, 6, 1),
(75, 6, 5),
(76, 6, 9),
(77, 6, 13),
(78, 6, 10),
(79, 6, 17),
(80, 5, 1),
(81, 5, 5),
(82, 5, 9),
(83, 5, 11),
(84, 5, 17),
(85, 4, 1),
(86, 4, 5),
(87, 4, 9),
(88, 4, 14),
(89, 4, 7),
(90, 3, 1),
(91, 3, 7),
(92, 3, 11),
(93, 3, 4),
(94, 3, 10),
(95, 2, 1),
(96, 2, 7),
(97, 2, 4),
(98, 2, 11),
(99, 2, 14),
(100, 1, 2),
(101, 1, 9),
(102, 1, 12),
(103, 1, 15),
(104, 1, 17);
