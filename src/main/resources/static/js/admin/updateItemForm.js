let selectedFiles = []; // 새로 선택된 파일을 저장할 배열
let existingFiles = []; // 기존 파일을 저장할 배열

// 페이지 로드 시 기존 이미지 정보 설정
window.onload = function() {
    const imageContainer = document.getElementById('image-preview-container');
    existingFiles = Array.from(imageContainer.querySelectorAll('.image-preview-wrapper')).map(wrapper => ({
        src: wrapper.querySelector('img').src,
        filename: wrapper.querySelector('img').src.split('/').pop() // 파일 이름 추출
    }));
    updateImagePreview();
};

function previewImages(event) {
    const files = Array.from(event.target.files);
    selectedFiles = selectedFiles.concat(files);
    updateImagePreview();
}

function updateImagePreview() {
    const previewContainer = document.getElementById('image-preview-container');
    previewContainer.innerHTML = '';

    // 기존 이미지 표시
    existingFiles.forEach((file, index) => {
        const wrapper = createImagePreviewElement(file.src, () => removeExistingImage(index));
        previewContainer.appendChild(wrapper);
    });

    // 새로 선택된 이미지 표시
    selectedFiles.forEach((file, index) => {
        const reader = new FileReader();
        reader.onload = function(e) {
            const wrapper = createImagePreviewElement(e.target.result, () => removeNewImage(index));
            previewContainer.appendChild(wrapper);
        };
        reader.readAsDataURL(file);
    });
}

function createImagePreviewElement(src, onDelete) {
    const wrapper = document.createElement('div');
    wrapper.classList.add('image-preview-wrapper');

    const img = document.createElement('img');
    img.src = src;
    img.classList.add('image-preview');

    const deleteButton = document.createElement('button');
    deleteButton.innerHTML = 'X';
    deleteButton.classList.add('image-delete-button');
    deleteButton.onclick = onDelete;

    wrapper.appendChild(img);
    wrapper.appendChild(deleteButton);
    return wrapper;
}

function removeExistingImage(index) {
    existingFiles.splice(index, 1);
    updateImagePreview();
}

function removeNewImage(index) {
    selectedFiles.splice(index, 1);
    updateImagePreview();
}

function beforeSubmit() {
    const form = document.querySelector('form');
    const dataTransfer = new DataTransfer();

    selectedFiles.forEach(file => {
        dataTransfer.items.add(file);
    });

    const fileInput = document.getElementById('images');
    fileInput.files = dataTransfer.files;

    // 삭제된 기존 이미지 정보를 서버에 전달하기 위한 hidden input 추가
    const deletedFilesInput = document.createElement('input');
    deletedFilesInput.type = 'hidden';
    deletedFilesInput.name = 'deletedFiles';
    deletedFilesInput.value = JSON.stringify(existingFiles.map(file => file.filename));
    form.appendChild(deletedFilesInput);
}

function triggerFileInput() {
    document.getElementById('images').click();
}

function addOptionField() {
    const container = document.getElementById('added-options');
    const currentOptionContent = document.getElementById('optionContent').value.trim();
    const currentOptionPrice = document.getElementById('optionPrice').value.trim();

    if (currentOptionContent !== '' && currentOptionPrice !== '') {
        const newOption = document.createElement('div');
        newOption.className = 'input-option-set';
        newOption.innerHTML = `
            <div class="input-option-content">
                <input type="hidden" name="options[${container.children.length}].id" value="0">
                <input type="text" class="form-control" name="options[${container.children.length}].content" value="${currentOptionContent}" readonly>
                <input type="number" class="form-control" name="options[${container.children.length}].price" value="${currentOptionPrice}" readonly step="0.01">
            </div>
            <button type="button" class="custom-btn-outline custom-btn-remove-option" onclick="removeOption(this)">
                <i class="fas fa-minus"></i>
            </button>
        `;
        container.appendChild(newOption);

        document.getElementById('optionContent').value = '';
        document.getElementById('optionPrice').value = '';
        updateOptionIndexes();
    } else {
        alert('옵션명과 가격을 입력해주세요.');
    }
}

function removeOption(button) {
    const optionSet = button.closest('.input-option-set');
    optionSet.remove();
    updateOptionIndexes();
}

function updateOptionIndexes() {
    const container = document.getElementById('added-options');
    const options = container.getElementsByClassName('input-option-set');

    for (let i = 0; i < options.length; i++) {
        const inputs = options[i].getElementsByTagName('input');
        inputs[0].name = `options[${i}].id`;
        inputs[1].name = `options[${i}].content`;
        inputs[2].name = `options[${i}].price`;
    }
}

// 페이지 로드 시 옵션 인덱스 업데이트
document.addEventListener('DOMContentLoaded', function() {
    updateOptionIndexes();
});