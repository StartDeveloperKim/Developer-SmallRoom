function articleRemove(articleId) {
    if (confirm("게시글을 삭제하시겠습니까?")) {
        $.ajax({
            url: '/api/article/' + articleId,
            type: 'DELETE',
            success: function(response) {
                console.log('게시글이 삭제되었습니다.');
                window.location.href = "/";
            },
            error: function(xhr, status, error) {
                console.error('Error deleting article:', error);
            }
        });
    }
}

function onclickLikeButton(articleId) {
    if ($('#like-button').hasClass('bg-red-400')) {
        saveLikeInfo(articleId, 'DELETE');
    }else{
        saveLikeInfo(articleId, 'POST');
    }
}

function saveLikeInfo(articleId, method) {
    $.ajax({
        url: '/api/like/'+articleId,
        method: method,
        success: function(response) {
            const inputButton = $('#like-button');
            if (method === 'POST') {
                inputButton.removeClass('bg-slate-400').addClass('bg-red-400');
            }else{
                inputButton.removeClass('bg-red-400').addClass('bg-slate-400');
            }
        },
        error: function(xhr, status, error) {
            alert("잘못된 접근입니다.");
            console.error('Error sending like request:', error);
        }
    });
}
