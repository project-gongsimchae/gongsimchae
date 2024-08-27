
// 모달 열기와 닫기 동작을 위한 JavaScript 코드
const modal = document.getElementById("createCategoryModal");
const openModalBtn = document.getElementById("openModalBtn");
const closeModalBtn = document.getElementById("closeModalBtn");
const closeModalFooterBtn = document.getElementById("closeModalFooterBtn");

// 모달 열기
openModalBtn.onclick = function() {
    modal.style.display = "block";
}

// 모달 닫기 (x 버튼)
closeModalBtn.onclick = function() {
    modal.style.display = "none";
}

// 모달 닫기 (닫기 버튼)
closeModalFooterBtn.onclick = function() {
    modal.style.display = "none";
}

// 모달 외부 클릭 시 모달 닫기
window.onclick = function(event) {
    if (event.target == modal) {
        modal.style.display = "none";
    }
}
