window.addEventListener('scroll', function() {
    var scrollButton = document.getElementById('scrollToTopButton');
    if (window.pageYOffset > 0) {
        scrollButton.style.display = 'flex';
        scrollButton.style.animation = 'none';
    } else {
        scrollButton.style.animation = 'fadeOut 0.5s ease-in-out forwards';
    }
});

function scrollToTop() {
    window.scrollTo({
        top: 0,
        behavior: 'smooth'
    });
}