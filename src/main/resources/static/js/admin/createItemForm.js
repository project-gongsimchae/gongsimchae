let selectedFiles = []; // 모든 선택된 파일을 저장할 배열

// 이미지 미리보기 함수
function previewImages(event) {
    const files = Array.from(event.target.files);  // 새로운 파일을 배열로 변환
    selectedFiles = selectedFiles.concat(files);  // 새로운 파일을 기존 배열에 추가

    updateImagePreview();  // 미리보기 업데이트
}

// 미리보기 업데이트 함수
function updateImagePreview() {
    const previewContainer = document.getElementById('image-preview-container');
    previewContainer.innerHTML = '';  // 기존 미리보기 이미지 초기화

    selectedFiles.forEach((file, index) => {
        const reader = new FileReader();

        reader.onload = function(e) {
            const wrapper = document.createElement('div');
            wrapper.classList.add('image-preview-wrapper');

            const img = document.createElement('img');
            img.src = e.target.result;
            img.classList.add('image-preview');

            const deleteButton = document.createElement('button');
            deleteButton.innerHTML = 'X';
            deleteButton.classList.add('image-delete-button');
            deleteButton.onclick = function() {
                removeImage(index);
            };

            wrapper.appendChild(img);
            wrapper.appendChild(deleteButton);
            previewContainer.appendChild(wrapper);
        };

        reader.readAsDataURL(file);  // 파일을 읽어서 미리보기로 표시
    });
}

// 이미지 제거 함수
function removeImage(index) {
    selectedFiles.splice(index, 1);  // 해당 인덱스의 파일을 배열에서 제거
    updateImagePreview();  // 미리보기 업데이트
}

// 폼 제출 전에 파일들을 폼에 추가
function beforeSubmit() {
    const form = document.querySelector('form');

    // DataTransfer 객체를 사용하여 모든 파일을 추가
    const dataTransfer = new DataTransfer();
    const detailImagesDataTransfer = new DataTransfer();

    selectedFiles.forEach(file => {
        dataTransfer.items.add(file);
    });

    selectedDetailFiles.forEach(file => {
        detailImagesDataTransfer.items.add(file);
    });

    // 기존 파일 인풋 필드를 가져와 파일들을 설정
    const fileInput = document.getElementById('images');
    fileInput.files = dataTransfer.files;

    const detailFileInput = document.getElementById('detailImages');
    detailFileInput.files = detailImagesDataTransfer.files;
}

// 파일 선택창을 열기 위한 함수
function triggerFileInput() {
    document.getElementById('images').click();  // 이미지 선택 창 열기
}

function triggerDetailFileInput() {
    document.getElementById('detailImages').click();  // 이미지 선택 창 열기
}

let selectedDetailFiles = []; // 모든 선택된 파일을 저장할 배열

// 이미지 미리보기 함수
function previewDetailImages(event) {
    const files = Array.from(event.target.files);  // 새로운 파일을 배열로 변환
    selectedDetailFiles = selectedDetailFiles.concat(files);  // 새로운 파일을 기존 배열에 추가

    updateDetailImagePreview();  // 미리보기 업데이트
}

function updateDetailImagePreview() {
    const previewContainer = document.getElementById('image-preview-container2');
    previewContainer.innerHTML = '';  // 기존 미리보기 이미지 초기화

    selectedDetailFiles.forEach((file, index) => {
        const reader = new FileReader();

        reader.onload = function(e) {
            const wrapper = document.createElement('div');
            wrapper.classList.add('image-preview-wrapper');

            const img = document.createElement('img');
            img.src = e.target.result;
            img.classList.add('image-preview');

            const deleteButton = document.createElement('button');
            deleteButton.innerHTML = 'X';
            deleteButton.classList.add('image-delete-button');
            deleteButton.onclick = function() {
                removeDetailImage(index);
            };

            wrapper.appendChild(img);
            wrapper.appendChild(deleteButton);
            previewContainer.appendChild(wrapper);
        };

        reader.readAsDataURL(file);  // 파일을 읽어서 미리보기로 표시
    });
}

// 이미지 제거 함수
function removeDetailImage(index) {
    selectedDetailFiles.splice(index, 1);  // 해당 인덱스의 파일을 배열에서 제거
    updateDetailImagePreview();  // 미리보기 업데이트
}

// 옵션 추가 및 제거 기능
function addOptionField() {
    const optionContent = document.getElementById('optionContent').value.trim();
    const optionPrice = document.getElementById('optionPrice').value.trim();

    if (optionContent && optionPrice) {
        const optionContainer = document.getElementById('added-options');

        // 새로운 옵션 항목 생성
        const index = optionContainer.children.length;  // 현재 옵션 개수
        const newOption = document.createElement('div');
        newOption.className = 'input-option-set';
        newOption.innerHTML = `
            <div class="input-option-content">
                <input type="hidden" name="options[${index}].content" value="${optionContent}">
                <input type="hidden" name="options[${index}].price" value="${optionPrice}">
                <input type="text" class="form-control" value="${optionContent}" readonly>
                <input type="number" class="form-control" value="${optionPrice}" step="0.01" readonly>
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

        // 옵션 추가 버튼 스타일 초기화
        toggleAddOptionButton();
    } else {
        alert('옵션명과 가격을 모두 입력하세요.');
    }
}

function removeOption(button) {
    const optionSet = button.closest('.input-option-set');
    optionSet.remove();
}

document.addEventListener('DOMContentLoaded', function() {
    const groupBuyingLimitTimeInput = document.getElementById('groupBuyingLimitTime');
    const addOptionButton = document.querySelector('.custom-btn-add-option');
    const optionContentInput = document.getElementById('optionContent');
    const optionPriceInput = document.getElementById('optionPrice');

    // 오늘 날짜로 초기화
    function setInitialDateTime() {
        const now = new Date();
        now.setSeconds(59);
        now.setMilliseconds(0);
        const formattedDate = now.toISOString().slice(0, 16); // "YYYY-MM-DDTHH:MM"
        groupBuyingLimitTimeInput.value = formattedDate;
        groupBuyingLimitTimeInput.min = formattedDate; // 과거 날짜 선택 방지
    }

    setInitialDateTime();

    groupBuyingLimitTimeInput.addEventListener('input', function() {
        const date = new Date(groupBuyingLimitTimeInput.value);
        date.setMinutes(59);
        date.setSeconds(59);
        groupBuyingLimitTimeInput.value = date.toISOString().slice(0, 16);
    });

    const originalPriceInput = document.getElementById('originalPrice');
    const discountRateInput = document.getElementById('discountRate');
    const pointAccumulationRateInput = document.getElementById('pointAccumulationRate');
    const discountedPriceDisplay = document.getElementById('discountedPrice');
    const accumulatedPointsDisplay = document.getElementById('accumulatedPoints');

    function updateDiscountedPrice() {
        const originalPrice = parseFloat(originalPriceInput.value) || 0;
        const discountRate = parseFloat(discountRateInput.value) || 0;
        const discountedPrice = originalPrice * (1 - discountRate / 100);
        discountedPriceDisplay.textContent = `할인가 : ${discountedPrice.toLocaleString()}원`;
    }

    function updateAccumulatedPoints() {
        const originalPrice = parseFloat(originalPriceInput.value) || 0;
        const pointRate = parseFloat(pointAccumulationRateInput.value) || 0;
        const accumulatedPoints = originalPrice * (pointRate / 100);
        accumulatedPointsDisplay.textContent = `포인트 적립 금액 : ${accumulatedPoints.toLocaleString()}원`;
    }

    originalPriceInput.addEventListener('input', function() {
        updateDiscountedPrice();
        updateAccumulatedPoints();
    });

    discountRateInput.addEventListener('input', function() {
        if (parseFloat(discountRateInput.value) > 99) {
            discountRateInput.value = 99;
        }
        updateDiscountedPrice();
    });

    pointAccumulationRateInput.addEventListener('input', function() {
        if (parseFloat(pointAccumulationRateInput.value) > 99) {
            pointAccumulationRateInput.value = 99;
        }
        updateAccumulatedPoints();
    });

    // 옵션 추가 버튼 활성화 제어
    function toggleAddOptionButton() {
        if (optionContentInput.value.trim() !== '' && optionPriceInput.value.trim() !== '') {
            addOptionButton.classList.add('enabled');
        } else {
            addOptionButton.classList.remove('enabled');
        }
    }

    optionContentInput.addEventListener('input', toggleAddOptionButton);
    optionPriceInput.addEventListener('input', toggleAddOptionButton);
});
