document.addEventListener("DOMContentLoaded", function() {
    const eventTypeSelect = document.getElementById('eventTypeName');
    const formGroups = document.querySelectorAll('.form-group');
    const categoryContainer = document.getElementById('categoryContainer');
    const selectedCategoriesContainer = document.getElementById('selectedCategories');

    // Initialize: show the event type select and category sections, hide other form groups
    formGroups.forEach(group => {
        if (group.querySelector('#eventTypeName') || group.contains(categoryContainer)) {
            group.style.display = 'block'; // Show event type and category fields
        } else {
            group.style.display = 'none'; // Hide other fields
        }
    });

    eventTypeSelect.addEventListener('change', function() {
        const selectedType = eventTypeSelect.value;

        // Hide all form fields except for event type and category
        formGroups.forEach(group => {
            if (!group.querySelector('#eventTypeName') && !group.contains(categoryContainer)) {
                group.style.display = 'none';
            }
        });

        // Show relevant fields based on selected event type
        if (selectedType === '할인') {
            document.getElementById('eventName').closest('.form-group').style.display = 'block';
            document.getElementById('discount').closest('.form-group').style.display = 'block';
            document.getElementById('maxDiscount').closest('.form-group').style.display = 'block';
            document.getElementById('expirationDate').closest('.form-group').style.display = 'block';
            document.getElementById('eventBannerImage').closest('div').style.display = 'block';
        } else if (selectedType === '쿠폰발급') {
            document.getElementById('eventName').closest('.form-group').style.display = 'block';
            document.getElementById('discount').closest('.form-group').style.display = 'block';
            document.getElementById('maxDiscount').closest('.form-group').style.display = 'block';
            document.getElementById('expirationDate').closest('.form-group').style.display = 'block';
            document.getElementById('couponCode').closest('.form-group').style.display = 'block';
            document.getElementById('eventBannerImage').closest('div').style.display = 'block';
        } else if (selectedType === '쿠폰코드발급') {
            document.getElementById('eventName').closest('.form-group').style.display = 'block';
            document.getElementById('discount').closest('.form-group').style.display = 'block';
            document.getElementById('maxDiscount').closest('.form-group').style.display = 'block';
            document.getElementById('expirationDate').closest('.form-group').style.display = 'block';
            document.getElementById('couponCode').closest('.form-group').style.display = 'block';
        }
    });

    // Handle category selection (existing code)
    categoryContainer.addEventListener('click', function(event) {
        const target = event.target;
        if (target.classList.contains('category-box')) {
            toggleCategorySelection(target);
        }
    });

    function toggleCategorySelection(categoryBox) {
        const categoryName = categoryBox.getAttribute('data-category-name');

        // Check if the category is already selected
        if (categoryBox.classList.contains('selected-category')) {
            categoryBox.classList.remove('selected-category');
            removeSelectedCategory(categoryName);
        } else {
            categoryBox.classList.add('selected-category');
            addSelectedCategory(categoryName);
        }

        updateCategoryNamesInput();
    }

    function addSelectedCategory(categoryName) {
        const selectedBox = document.createElement('div');
        selectedBox.textContent = categoryName;
        selectedBox.classList.add('category-box', 'selected-category');
        selectedBox.setAttribute('data-category-name', categoryName);
        selectedCategoriesContainer.appendChild(selectedBox);
    }

    function removeSelectedCategory(categoryName) {
        const selectedBoxes = selectedCategoriesContainer.getElementsByClassName('selected-category');
        for (let i = 0; i < selectedBoxes.length; i++) {
            if (selectedBoxes[i].getAttribute('data-category-name') === categoryName) {
                selectedCategoriesContainer.removeChild(selectedBoxes[i]);
                break;
            }
        }
    }

    function updateCategoryNamesInput() {
        const selectedBoxes = selectedCategoriesContainer.getElementsByClassName('selected-category');
        const selectedCategoryNames = Array.from(selectedBoxes).map(box => box.getAttribute('data-category-name'));
        document.getElementById('categoryNames').value = selectedCategoryNames.join(',');
    }
});
