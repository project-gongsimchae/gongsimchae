// 이미지 미리보기 함수
function previewImages(event) {
    const previewContainer = document.getElementById('image-preview-container');
    previewContainer.innerHTML = '';  // 기존 미리보기 이미지 초기화

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

// 옵션 추가 및 제거 기능
var optionIndex = 0; // 옵션 인덱스 초기화

function addOptionField() {
    const optionContent = document.getElementById('optionContent').value;
    const optionPrice = document.getElementById('optionPrice').value;

    if (optionContent && optionPrice) {
        const optionContainer = document.getElementById('added-options');

        // 새로운 옵션 항목 생성
        const index = optionContainer.children.length;  // 현재 옵션 개수
        const newOption = document.createElement('div');
        newOption.className = 'input-option-set';
        newOption.innerHTML = `
            <div class="input-option-content">
                <input type="hidden" name="options[${index}].content" value="${optionContent}" />
                <input type="hidden" name="options[${index}].price" value="${optionPrice}" />
                <span>옵션명: ${optionContent}</span>
                <span>, 가격: ${optionPrice}</span>
            </div>
            <button type="button" class="custom-btn-outline custom-btn-remove-option" onclick="removeOption(this)">
                <i class="fas fa-minus"></i>
            </button>
        `;

        // 새로운 옵션을 추가할 영역에 삽입
        optionContainer.appendChild(newOption);

        // 입력 필드 초기화
        document.getElementById('optionContent').value = '';
        document.getElementById('optionPrice').value = '';
    } else {
        alert('옵션명과 가격을 모두 입력하세요.');
    }
}


function removeOption(element) {
    const optionContainer = document.getElementById('added-options');
    optionContainer.removeChild(element.parentElement);

    // 히든 필드에서 해당 옵션 삭제
    const optionsInput = document.getElementById('options');
    const optionText = element.parentElement.children[0].innerText + ':' + element.parentElement.children[1].innerText.replace('원', '');
    optionsInput.value = optionsInput.value.replace(optionText + ';', '');
}
