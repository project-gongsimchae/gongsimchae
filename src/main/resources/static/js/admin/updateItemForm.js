function previewImages(event) {
    const previewContainer = document.getElementById('image-preview-container');

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
    const index = container.children.length;
    const newOption = document.createElement('div');
    newOption.className = 'input-option-group';
    newOption.innerHTML = `
        <div class="input-option">
            <input type="text" class="form-control" name="options[${index}].content" placeholder="옵션명" aria-label="옵션명">
            <input type="number" class="form-control" name="options[${index}].price" placeholder="가격" aria-label="가격" step="0.01">
        </div>
        <div class="remove-option">
            <button class="custom-btn-outline custom-btn-remove-option" type="button" onclick="removeOptionField(this)">
                <i class="fas fa-minus"></i>
            </button>
        </div>
    `;
    container.appendChild(newOption);
}

function removeOptionField(button) {
    button.closest('.input-option-group').remove();
}