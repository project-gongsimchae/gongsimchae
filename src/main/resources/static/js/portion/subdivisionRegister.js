document.getElementById('imageFile').addEventListener('change', function() {
    const previewContainer = document.getElementById('imagePreview');
    previewContainer.innerHTML = ''; // Clear previous preview
    const file = this.files[0];
    if (file) {
        const reader = new FileReader();
        reader.onload = function(e) {
            const img = document.createElement('img');
            img.src = e.target.result;
            const deleteBtn = document.createElement('button');
            deleteBtn.innerHTML = '&times;';
            deleteBtn.classList.add('delete-btn');
            deleteBtn.onclick = function() {
                previewContainer.innerHTML = ''; // Remove the image and button
                document.getElementById('imageFile').value = ''; // Clear the input value
            };
            const previewItem = document.createElement('div');
            previewItem.appendChild(img);
            previewItem.appendChild(deleteBtn);
            previewContainer.appendChild(previewItem);
        };
        reader.readAsDataURL(file);
    }
});
