function previewImages(event) {
    const previewContainer = document.getElementById('image-preview-container');
    previewContainer.innerHTML = ''; // 이전 미리보기 이미지 삭제
    const files = event.target.files;

    for (let i = 0; i < files.length; i++) {
        const file = files[i];
        const reader = new FileReader();

        reader.onload = function(e) {
            const img = document.createElement('img');
            img.src = e.target.result;
            img.classList.add('image-preview');
            previewContainer.appendChild(img);
        };

        reader.readAsDataURL(file);
    }
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