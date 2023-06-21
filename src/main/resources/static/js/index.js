import {data} from '/js/stack.js'

const allTechStack = data.techStack;
let searchWord = [];

const tag_input = document.getElementById("default-search");
let tagify = new Tagify(tag_input, {
    whitelist: allTechStack,
    dropdown: {
        classname: "tags-look",
        enabled: 0,
        closeOnSelect: false
    },
}); // initialize Tagify
// 태그가 추가되면 이벤트 발생

// 태그가 추가될 떄 마다 AJAX 통신을 활용해서 태그 기반 검색을 하자
tagify.on('add', function (e) {
    const tags = e.detail.tagify.value.map(tag => tag.value);
    searchWord=[...tags]
    console.log('추가 전체 검색어:', searchWord);
});

tagify.on('remove', function (e) {
    const tags = e.detail.tagify.value.map(tag => tag.value);
    searchWord=[...tags]

    console.log('삭제 전체 검색어:', searchWord);
});