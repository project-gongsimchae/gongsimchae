document.addEventListener('DOMContentLoaded', function() {
    document.querySelectorAll('.wishlist-item').forEach(function(item) {
        item.addEventListener('click', function() {
            window.location.href = item.getAttribute('data-url');
        });
    });
});