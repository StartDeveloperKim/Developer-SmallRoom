import {data} from '/js/stack.js'

let editor = new toastui.Editor({
    el: document.querySelector('#editor'),
    previewStyle: 'vertical',
    height: '600px',
    hooks: {
        addImageBlobHook: function (blob, callback) {
            const formData = new FormData();
            formData.append("image", blob);
            let imageURL;
            $.ajax({
                type: "POST",
                url: "/api/image",
                processData: false,
                contentType: false,
                data: formData,
                success: function (data) {
                    imageURL = data;
                    console.log("AJAX", imageURL);
                    callback(imageURL, "image");
                },
                error: function (request, status, error) {
                    alert(request + ", " + status + ", " + error);
                },
            });
        },
    },
    language: "ko-KR"
});

const tag_input = document.getElementById("tags-input");
let tagify = new Tagify(tag_input, {
    whitelist: data.techStack,
    dropdown: {
        maxItems: 20,
        classname: "tags-look",
        enabled: 0,
        closeOnSelect: false
    },
    enforceWhitelist: true
});


window.tagify = tagify;
window.editor = editor;


$(document).ready(function() {
    $('#cancel-button').on('click', function() {cancelPost();});
    $('#post-button').on('click', function () {showModal();});
    $('#save-button').on('click', function() {saveArticle()})
    $('#update-button').on('click', function() {updateArticle()})

    $('#dropzone-file').on('change', function (event){
        let file = event.target.files[0];

        if (file) {
            let formData = new FormData();
            formData.append('image', file);

            $.ajax({
                url: '/api/image',
                type: 'POST',
                data: formData,
                processData: false,
                contentType: false,
                success: function(data) {
                    $('#image-url').val(data);
                    $('#preview-image').attr('src', data).removeClass('hidden');
                    $('#modal-info1').addClass('hidden');
                    $('#modal-info2').addClass('hidden');
                    $('#modal-img').addClass('hidden');
                },
                error: function(error) {
                    console.log('썸네일 업로드 실패', error);
                    // 실패 처리
                }
            });
        }
    })
});

function cancelPost() {
    if (confirm("글이 저장되지 않았습니다.")) {
        window.history.back();
    }
}

function showModal() {
    const title = $('#title').val();
    const github_link = $('#gitHub-link').val()
    console.log(title, github_link);
    $('#temp-title').html('<div>' + title + '</div>');
    $('#temp-github').html('<div>' + github_link + '</div>');
}

function getRequestData() {
    const articleId = $('#article-id').val();
    const title = $('#title').val();
    const gitHubLink = $('#gitHub-link').val();
    const content = editor.getHTML();
    const subTitle = $('#sub-title').val();
    const thumbnail = $('#image-url').val();

    const tempTags = tagify.value
    const tags = tempTags.map(obj => obj.value);

    return {
        articleId: articleId,
        title: title,
        subTitle: subTitle,
        content: content, // You'll need to provide the content data
        gitHubLink: gitHubLink,
        thumbnailUrl: thumbnail,
        tags: tags
    };
}

function saveArticle() {
    const requestData = getRequestData();
    toServer(requestData, 'POST');
}

function updateArticle() {
    const requestData = getRequestData();
    toServer(requestData, 'PUT');
}

function toServer(requestData, method) {
    $.ajax({
        url: '/api/article',
        type: method,
        dataType: 'json',
        contentType: 'application/json',
        data: JSON.stringify(requestData),
        success: function (response) {
            const message = response.message;
            const responseData = response.responseData;
            alert(message);

            window.location.href = '/article/' + responseData;
        },
        error: function (xhr, textStatus, errorThrown) {
            console.log(errorThrown);
        }
    });
}
