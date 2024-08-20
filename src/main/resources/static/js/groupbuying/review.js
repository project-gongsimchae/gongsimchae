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

document.addEventListener('DOMContentLoaded', function() {
    // 리뷰 작성 버튼 클릭 이벤트
    document.querySelectorAll('.openModalButton').forEach(button => {
        button.addEventListener('click', function() {
            const uid = this.getAttribute('data-uid');
            openModalForReview(uid, '작성하기', 'POST', `/mypage/reviews/write/${uid}`); // post
        });
    });

    // 리뷰 수정 버튼 클릭 이벤트
    document.querySelectorAll('.openUpdateModalButton').forEach(button => {
        button.addEventListener('click', function() {
            const uid = this.getAttribute('data-uid');

            // 리뷰 수정 모드: 데이터를 서버에서 가져오거나 페이지에서 찾습니다.
            fetch(`/mypage/reviews/${uid}`)  // 수정할 리뷰 데이터를 가져오는 API 경로, get
                .then(response => response.json())
                .then(data => {
                    // 데이터를 가져온 후 필드에 바인딩
                    bindDataToForm(data);
                    openModalForReview(uid, '수정하기', 'POST', `/mypage/reviews/update/${uid}`); // put
                })
                .catch(error => {
                    console.error('리뷰 데이터를 불러오는 중 오류 발생:', error);
                });
        });
    });

    // 모달 열기 함수
    function openModalForReview(uid, buttonText, method, actionUrl) {
        const submitButton = document.getElementById('submitReviewButton');

        // 버튼 텍스트 및 요청 메서드 변경
        submitButton.textContent = buttonText;

        // 폼 메서드와 액션 변경
        const form = document.getElementById('reviewForm');
        form.setAttribute('method', 'post');  // HTML form 태그에서 method 속성은 GET/POST만 지원
        form.setAttribute('action', actionUrl);

        // 폼 전송 이벤트 설정
        submitButton.onclick = function(event) {
            event.preventDefault();

            const formData = new FormData(form);

            fetch(actionUrl, {
                method: method, // 동적으로 설정된 메서드 (POST/PUT)
                body: formData
            })
                .then(response => {
                    if (response.ok) {
                        alert(`${buttonText}가 완료되었습니다.`);
                        location.reload(); // 페이지 새로고침
                    } else {
                        return response.json().then(errorData => {
                            console.error('오류:', errorData);
                        });
                    }
                })
                .catch(error => {
                    console.error(`${buttonText} 중 오류가 발생했습니다:`, error);
                });
        };

        // 모달 열기
        document.getElementById('reviewModal').style.display = 'block';
        document.getElementById('modalOverlay').style.display = 'block';
    }

    // 데이터 바인딩 함수
    function bindDataToForm(data) {
        document.getElementById('title').value = data.title;
        document.getElementById('starPoint').value = data.starPoint;
        document.getElementById('content').value = data.content;
        document.getElementById('secretStatus').checked = data.secretStatus === true;
    }

    // 데이터 비우기 함수
    function clearFormData() {
        document.getElementById('title').value = null;
        document.getElementById('starPoint').value = null;
        document.getElementById('content').value = null;
        document.getElementById('secretStatus').checked = false;
    }

    // 모달 닫기 버튼 클릭 이벤트
    document.getElementById('closeModalButton').addEventListener('click', function() {
        document.getElementById('reviewModal').style.display = 'none';
        document.getElementById('modalOverlay').style.display = 'none';
        clearFormData();
    });

    // 모달 외부 클릭 시 닫기
    document.getElementById('modalOverlay').addEventListener('click', function() {
        document.getElementById('reviewModal').style.display = 'none';
        document.getElementById('modalOverlay').style.display = 'none';
        clearFormData();
    });
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