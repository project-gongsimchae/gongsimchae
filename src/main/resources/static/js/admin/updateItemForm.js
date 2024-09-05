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
document.getElementById('mainForm').addEventListener('submit', function(e) {
    e.preventDefault(); // 기존 폼 제출을 막음
    updateDeleteImagesInput(); // 이미지 삭제 업데이트

    // 수정 완료 알림 표시
    alert("상품 내용이 수정되었습니다. 공동구매 재진행하기 위해서는 옆의 '공동구매 재진행' 버튼을 누르셔야 합니다.");

    // 알림 후 폼 제출
    e.target.submit();
});

function triggerDetailFileInput() {
    document.getElementById('detailImages').click();  // 이미지 선택 창 열기
}

let selectedDetailFiles = []; // 모든 선택된 파일을 저장할 배열

// 이미지 미리보기 함수
function previewDetailImages(event) {
    const container = document.getElementById('image-preview-container2');
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

// 이미지 제거 함수
function removeDetailImage(index) {
    selectedDetailFiles.splice(index, 1);  // 해당 인덱스의 파일을 배열에서 제거
    updateDetailImagePreview();  // 미리보기 업데이트
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

function validateGroupBuyingTime() {
    let limitTimeField = document.getElementById('groupBuyingLimitTime');
    let limitTime = new Date(limitTimeField.value);
    let currentTime = new Date();

    // 제한 시간이 현재 시간과 같거나 과거라면 알림창 표시
    if (limitTime <= currentTime) {
        alert('공동구매 제한 시간을 다시 한 번 확인해주세요. 미래 시간으로 설정해야 합니다.');
    } else {
        // 공동구매 제한 시간이 유효하면 모달창 띄움
        showModal();
    }
}

// 공동구매 제한 시간을 시, 분, 초 중 시만 수정 가능하게 설정
document.addEventListener('DOMContentLoaded', function() {
    let limitTimeField = document.getElementById('groupBuyingLimitTime');

    if (limitTimeField.value) {
        let originalTime = new Date(limitTimeField.value);
        // 초 단위에서 '분'과 '초'를 59분 58초로 고정
        // originalTime.setMinutes(59);
        // originalTime.setSeconds(59);
        limitTimeField.value = new Date(originalTime.getTime() - originalTime.getTimezoneOffset() * 60000).toISOString().slice(0, 16);
    }

    limitTimeField.addEventListener('input', function() {
        let date = new Date(this.value);
        // date.setMinutes(59);
        // date.setSeconds(59);
        this.value = new Date(date.getTime() - date.getTimezoneOffset() * 60000).toISOString().slice(0, 16);
    });
});

document.getElementById('group-buying-restart-form').addEventListener('submit', function (e) {
    e.preventDefault(); // 폼 제출을 막음

    let limitTimeField = document.getElementById('groupBuyingLimitTime');
    let limitTime = new Date(limitTimeField.value);
    let currentTime = new Date();

    // 제한 시간이 유효하면 모달 창 표시
    if (limitTime > currentTime) {
        showModal();
    } else {
        alert('공동구매 제한 시간을 다시 한 번 확인해주세요. 미래 시간으로 설정해야 합니다.');
    }
});

function showModal() {
    const modal = document.getElementById('confirmationModal');
    const confirmYes = document.getElementById('confirmYes');
    const confirmNo = document.getElementById('confirmNo');

    modal.style.display = "block"; // 모달 표시

    confirmYes.onclick = function () {
        modal.style.display = "none"; // 모달 숨기기
        document.getElementById('group-buying-restart-form').submit(); // 폼 제출
    };

    confirmNo.onclick = function () {
        modal.style.display = "none"; // 모달 숨기기
    };
}

// 모달 외부 클릭 시 닫기
window.onclick = function (event) {
    const modal = document.getElementById('confirmationModal');
    if (event.target == modal) {
        modal.style.display = "none";
    }
};
