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

// 모달 열기 버튼과 닫기 버튼에 대한 이벤트 리스너 추가
document.getElementById('openModalButton').addEventListener('click', function() {
    document.getElementById('reviewModal').style.display = 'block';
    document.getElementById('modalOverlay').style.display = 'block';
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