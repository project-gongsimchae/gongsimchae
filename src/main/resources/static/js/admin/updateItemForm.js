let deleteImageIds = [];

function triggerFileInput() {
    document.getElementById('images').click();
}

function previewImages(event) {
    const container = document.getElementById('image-preview-container');
    const files = event.target.files;

    for (let i = 0; i < files.length; i++) {
        const file = files[i];
        const reader = new FileReader();

        reader.onload = function(e) {
            const wrapper = document.createElement('div');
            wrapper.className = 'image-preview-wrapper';

            const img = document.createElement('img');
            img.src = e.target.result;
            img.className = 'image-preview';

            const deleteButton = document.createElement('button');
            deleteButton.innerHTML = 'X';
            deleteButton.className = 'image-delete-button';
            deleteButton.onclick = function() {
                container.removeChild(wrapper);
            };

            wrapper.appendChild(img);
            wrapper.appendChild(deleteButton);
            container.appendChild(wrapper);
        }

        reader.readAsDataURL(file);
    }
}

function removeExistingImage(imageId, button) {
    deleteImageIds.push(imageId);
    updateDeleteImagesInput();
    button.closest('.image-preview-wrapper').remove();
}

function updateDeleteImagesInput() {
    document.getElementById('deleteImages').value = deleteImageIds.join(',');
}

// 폼 제출 전 deleteImages 입력 필드 업데이트
document.querySelector('form').addEventListener('submit', function(e) {
    updateDeleteImagesInput();
});

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