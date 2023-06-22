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
