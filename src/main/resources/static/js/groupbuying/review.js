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