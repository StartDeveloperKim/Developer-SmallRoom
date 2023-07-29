import {updateArticleList} from "./card.js";
import {getAutoComplete} from "./autoComplete.js";

const tag_input = document.getElementById("default-search");
let tagify = new Tagify(tag_input, {
    whitelist: [],
    dropdown: {
        maxItems: 20,
        classname: "tags-look",
        enabled: 0,
        closeOnSelect: false
    },
});

tagify.on('add', function (e) {
    setSearchResult();
});

tagify.on('remove', function(e) {
    setSearchResult();
});

tagify.on('input', async function(e) {
    tagify.whitelist = null;
    const inputWord = e.detail.value;
    console.log("입력문자 : " + inputWord);
    if (inputWord.length === 0) {
        tagify.whitelist = null;
        tagify.dropdown.hide();
    }
    try {
        tagify.whitelist = await getAutoComplete(inputWord);
        tagify.dropdown.show();
        console.log("Tagify 자동완성 : "+tagify.whitelist);
    } catch (err) {
        alert("오류가 발생했습니다.");
    }
});

let query;

function setSearchResult() {
    const tags = tagify.value.map(obj => obj.value);
    $('#content').empty();
    if (tags.length === 0) {
        searchFlag = false;
        dataLoadFlag = true;
        page = 0;
        loadData(basicArticleUrl + page + "&standard=" + standard);
        page++;
    }else{
        searchFlag = true;
        dataLoadFlag = true;
        page = 0;
        loadSearchData(searchArticleUrl + page, tags.join(','), standard);
        page++;
        query = tags.join(',');
        console.log('추가 전체 검색어:', tags);
    }
}

/*===================================================================*/

let dataLoadFlag = true;
let isLoading = false; // 중복 요청 방지를 위한 플래그 변수
let page = 0; // 초기 페이지 설정

function replaceSpecialWord(query) {
    query = query.replace('#', '%23');
    query = query.replace('++', '%2B%2B');
    console.log(query);
    return query;
}

function loadSearchData(url, requestData, standard) {
    $.ajax({
        type: "GET",
        url: url + "&query=" + replaceSpecialWord(requestData) + "&standard=" + standard,
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

let standard = "createAt";
let searchFlag = false;
let basicArticleUrl = "/api/article?page=";
let searchArticleUrl = "/api/search?page=";

$(document).ready(function () {
    loadData(basicArticleUrl + "0" + "&standard=" + standard);
    page++;
});

/*========================무한스크롤===============================*/

$(window).scroll(function () {
    let scrollHeight = $(document).height();
    let scrollPosition = $(window).height() + $(window).scrollTop();

    if (dataLoadFlag) {
        // let url = searchFlag === true ? searchArticleUrl + '?query=' + query : basicArticleUrl;
        if (scrollPosition >= scrollHeight - 200 && searchFlag === false) {
            loadData(basicArticleUrl + page + "&standard=" + standard);
            page++;
        }
        if (scrollPosition >= scrollHeight - 200 && searchFlag === true) {
            loadSearchData(searchArticleUrl + page, query, standard);
            page++;
        }
    }
});

/*========================좋아요 순, 최신 순===============================*/

window.reSortArticles = function (sortBy) {
    if (standard === sortBy) {
        return;
    }
    standard = sortBy;
    dataLoadFlag = true;

    const nowStandard = "text-blue-600";
    const prevStandard = "text-gray-400";
    if (standard === "createAt") {
        $('#createAt-emoge').addClass(nowStandard);
        $('#likeCount-emoge').removeClass(nowStandard);

        $('#createAt').removeClass(prevStandard).addClass(nowStandard);
        $('#likeCount').removeClass(nowStandard).addClass(prevStandard);
    }else{
        $('#likeCount-emoge').addClass(nowStandard);
        $('#createAt-emoge').removeClass(nowStandard);

        $('#likeCount').removeClass(prevStandard).addClass(nowStandard);
        $('#createAt').removeClass(nowStandard).addClass(prevStandard);
    }

    $('#content').empty();
    loadData(basicArticleUrl + "0" + "&standard=" + standard)
    page=1
}


