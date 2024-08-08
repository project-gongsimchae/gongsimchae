document.addEventListener('DOMContentLoaded', function () {
    const joinButton = document.getElementById('joinButton');
    const modal = document.getElementById('modal');
    const confirmButton = document.getElementById('confirmButton');
    const cancelButton = document.getElementById('cancelButton');

    joinButton.addEventListener('click', function () {
        modal.style.display = 'flex';
    });

    confirmButton.addEventListener('click', function () {
        const UID = subdivisionRespDto.UID; // subdivisionDto.UID를 사용하여 UID 값을 가져옴
        window.location.href = `/portioning/${UID}/join`;
    });

    cancelButton.addEventListener('click', function () {
        modal.style.display = 'none';
    });
});
