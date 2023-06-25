import {updateArticleList} from "./card.js";

let dataLoadFlag = true;
let isLoading = false; // 중복 요청 방지를 위한 플래그 변수
let page = 1; // 초기 페이지 설정

function loadData(page) {
    if (isLoading) {
        return; // 이미 로딩 중인 경우 중복 요청 방지
    }

    isLoading = true; // 로딩 중 플래그 설정

    $.ajax({
        type: "GET",
        url: "/api/article?page=" + page,
        timeout: 3000,
        async: false,
        success: function (data) {
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
        },
        error: function (data, status, err) {
            alert("오류가 발생했습니다.");
            console.log(page);
            isLoading = false; // 로딩 완료 후 플래그 초기화
        }
    });
}



$(window).scroll(function () {
    let scrollHeight = $(document).height();
    let scrollPosition = $(window).height() + $(window).scrollTop();

    if (dataLoadFlag) {
        if (scrollPosition >= scrollHeight - 200) {
            loadData(page);
            page++;
        }
    }
});
