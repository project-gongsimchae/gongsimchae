document.addEventListener('DOMContentLoaded', function () {
    const joinButton = document.getElementById('joinButton');
    const modal = document.getElementById('modal');
    const confirmButton = document.getElementById('confirmButton');
    const cancelButton = document.getElementById('cancelButton');

    joinButton.addEventListener('click', function () {
        modal.style.display = 'flex';
    });

    confirmButton.addEventListener('click', function () {
        window.location.href = '/join-link'; // Replace with your actual link
    });

    cancelButton.addEventListener('click', function () {
        modal.style.display = 'none';
    });
});
