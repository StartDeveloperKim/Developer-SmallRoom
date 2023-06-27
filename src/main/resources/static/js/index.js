import {updateArticleList} from "./card.js";
import {data} from '/js/stack.js'

const allTechStack = data.techStack;

const tag_input = document.getElementById("default-search");
let tagify = new Tagify(tag_input, {
    whitelist: allTechStack,
    dropdown: {
        classname: "tags-look",
        enabled: 0,
        closeOnSelect: false
    },
}); // initialize Tagify

// 태그가 추가될 떄 마다 AJAX 통신을 활용해서 태그 기반 검색을 하자
tagify.on('add', function (e) {
    const tags = tagify.value.map(obj => obj.value);
    $('#content').empty();
    searchFlag = true;
    dataLoadFlag = true;
    page = 0;
    loadSearchData(searchArticleUrl + page, tags.join(','));
    page++;
    query = tags.join(',');
    console.log('추가 전체 검색어:', tags);
});

let query;

/*===================================================================*/

let dataLoadFlag = true;
let isLoading = false; // 중복 요청 방지를 위한 플래그 변수
let page = 0; // 초기 페이지 설정

function loadSearchData(url, requestData) {
    $.ajax({
        type: "GET",
        url: url + "&query=" + requestData,
        timeout: 3000,
        async: false,
        success: function (data) {
            addArticleCards(data);
        },
        error: function (data, status, err) {
            alert("오류가 발생했습니다.");
            console.log(page);
            isLoading = false; // 로딩 완료 후 플래그 초기화
        }
    });
}

function loadData(url) {
    if (isLoading) return; // 이미 로딩 중인 경우 중복 요청 방지
    isLoading = true; // 로딩 중 플래그 설정

    $.ajax({
        type: "GET",
        url: url,
        timeout: 3000,
        async: false,
        success: function (data) {
            addArticleCards(data);
        },
        error: function (data, status, err) {
            alert("오류가 발생했습니다.");
            console.log(page);
            isLoading = false; // 로딩 완료 후 플래그 초기화
        }
    });
}

function addArticleCards(data) {
    if (data.length > 0) {
        const content = $('#content');
        content.append('<div>').addClass('flex flex-wrap justify-center mt-5');
        for (let i = 0; i < data.length; i++) {
            updateArticleList(data[i], content);
        }
    }
    isLoading = false; // 로딩 완료 후 플래그 초기화
    if (data.length < 4) {
        dataLoadFlag = false;
    }
}

let searchFlag = false;
let basicArticleUrl = "/api/article?page=";
let searchArticleUrl = "/api/search?page=";

$(document).ready(function () {
    loadData(basicArticleUrl + "0");
    page++;
});

$(window).scroll(function () {
    let scrollHeight = $(document).height();
    let scrollPosition = $(window).height() + $(window).scrollTop();

    if (dataLoadFlag) {
        // let url = searchFlag === true ? searchArticleUrl + '?query=' + query : basicArticleUrl;
        if (scrollPosition >= scrollHeight - 200 && searchFlag === false) {
            loadData(basicArticleUrl + page);
            page++;
        }
        if (scrollPosition >= scrollHeight - 200 && searchFlag === true) {
            loadSearchData(searchArticleUrl + page, query);
            page++;
        }
    }
});



