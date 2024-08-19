// JavaScript to handle tab switching
const tabs = document.querySelectorAll('.tab');
const sections = document.querySelectorAll('.review-section');

tabs.forEach(tab => {
    tab.addEventListener('click', () => {
        // Remove 'active' class from all tabs
        tabs.forEach(t => t.classList.remove('active'));
        // Add 'active' class to the clicked tab
        tab.classList.add('active');

        // Hide all sections
        sections.forEach(section => section.classList.remove('active'));
        // Show the targeted section
        const target = tab.getAttribute('data-target');
        document.getElementById(target).classList.add('active');
    });
});

document.getElementById('submitReviewButton').addEventListener('click', function(event) {
    event.preventDefault(); // 기본 폼 제출 막기

    // 폼 데이터 생성
    const form = document.getElementById('reviewForm');
    const formData = new FormData(form);

    // AJAX 요청
    fetch('/mypage/reviews/write', {
        method: 'POST',
        body: formData
    })
        .then(response => {
            if (response.ok) {
                // 요청 성공 시
                alert('리뷰가 작성되었습니다.');
                location.reload(); // 페이지 새로고침
            } else {
                return response.json().then(errorData => {
                    // 요청 실패 시 오류 출력
                    console.error('오류:', errorData);
                });
            }
        })
        .catch(error => {
            // 네트워크 오류 또는 기타 오류
            console.error('리뷰 작성 중 오류가 발생했습니다:', error);
        });
});

// 모든 '리뷰작성' 버튼에 대해 클릭 이벤트 추가
document.querySelectorAll('.openModalButton').forEach(button => {
    button.addEventListener('click', function() {
        // 버튼의 data-uid 속성에서 UID 값 가져오기
        const uid = this.getAttribute('data-uid');

        // 숨겨진 input 필드에 UID 값 설정
        document.getElementById('itemUID').value = uid;

        // 모달 열기
        document.getElementById('reviewModal').style.display = 'block';
        document.getElementById('modalOverlay').style.display = 'block';
    });
});

document.getElementById('closeModalButton').addEventListener('click', function() {
    document.getElementById('reviewModal').style.display = 'none';
    document.getElementById('modalOverlay').style.display = 'none';
});

// 모달 외부를 클릭했을 때 모달 닫기
document.getElementById('modalOverlay').addEventListener('click', function() {
    document.getElementById('reviewModal').style.display = 'none';
    document.getElementById('modalOverlay').style.display = 'none';
});

// 리뷰 이미지
document.getElementById('imageFile').addEventListener('change', function() {
    const previewContainer = document.getElementById('imagePreview');
    previewContainer.innerHTML = ''; // Clear previous preview

    const files = this.files; // Get all selected files
    for (let i = 0; i < files.length; i++) {
        const file = files[i];
        if (file) {
            const reader = new FileReader();
            reader.onload = function(e) {
                const img = document.createElement('img');
                img.src = e.target.result;
                const deleteBtn = document.createElement('button');
                deleteBtn.innerHTML = '&times;';
                deleteBtn.classList.add('delete-btn');
                deleteBtn.onclick = function() {
                    previewContainer.removeChild(previewItem); // Remove the image and button
                };
                const previewItem = document.createElement('div');
                previewItem.classList.add('preview-item');
                previewItem.appendChild(img);
                previewItem.appendChild(deleteBtn);
                previewContainer.appendChild(previewItem);
            };
            reader.readAsDataURL(file);
        }
    }
});