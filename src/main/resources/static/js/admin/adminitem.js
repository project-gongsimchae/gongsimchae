document.querySelector('.search-filter').addEventListener('click', () => {
    document.getElementById('filter-popup').classList.toggle('active');

    // 필터 버튼 활성화 상태 설정 (URL 쿼리를 읽어서 활성화)
    setFilterStateFromUrl();
});

function setupSingleSelectOptions(containerId) {
    const container = document.getElementById(containerId);
    container.querySelectorAll('.filter-button').forEach(button => {
        button.addEventListener('click', function() {
            if (this.classList.contains('active')) {
                this.classList.remove('active');
            } else {
                container.querySelectorAll('.filter-button').forEach(btn => btn.classList.remove('active'));
                this.classList.add('active');
            }
        });
    });
}

function setupMultiSelectOptions(containerId) {
    const container = document.getElementById(containerId);
    container.querySelectorAll('.filter-button').forEach(button => {
        button.addEventListener('click', function() {
            this.classList.toggle('active');
        });
    });
}

// 필터 상태 설정 (URL에 있는 값으로 버튼 활성화)
function setFilterStateFromUrl() {
    const urlParams = new URLSearchParams(window.location.search);

    const itemIsDeleted = urlParams.get('itemIsDeleted');
    const remainingTime = urlParams.get('remainingTime');
    const itemIsClosed = urlParams.get('itemIsClosed');
    const categories = urlParams.get('categories') ? urlParams.get('categories').split(',') : [];

    // itemIsDeleted 버튼 상태 설정
    if (itemIsDeleted) {
        document.querySelector(`#itemIsDeletedOptions .filter-button[data-value="${itemIsDeleted}"]`)?.classList.add('active');
    }

    // remainingTime 버튼 상태 설정
    if (remainingTime) {
        document.querySelector(`#remainingTimeOptions .filter-button[data-value="${remainingTime}"]`)?.classList.add('active');
    }

    // itemIsClosed 버튼 상태 설정
    if (itemIsClosed) {
        document.querySelector(`#itemIsClosedOptions .filter-button[data-value="${itemIsClosed}"]`)?.classList.add('active');
    }

    // categories 버튼 상태 설정 (여러 개 선택 가능)
    categories.forEach(category => {
        document.querySelector(`#categoriesOptions .filter-button[data-value="${category}"]`)?.classList.add('active');
    });
}

setupSingleSelectOptions('itemIsDeletedOptions');
setupSingleSelectOptions('remainingTimeOptions');
setupSingleSelectOptions('itemIsClosedOptions');
setupMultiSelectOptions('categoriesOptions');

document.getElementById('apply-filters').addEventListener('click', () => {
    const itemIsDeleted = document.querySelector('#itemIsDeletedOptions .filter-button.active')?.getAttribute('data-value') || '';
    const remainingTime = document.querySelector('#remainingTimeOptions .filter-button.active')?.getAttribute('data-value') || '';
    const itemIsClosed = document.querySelector('#itemIsClosedOptions .filter-button.active')?.getAttribute('data-value') || '';
    const categories = Array.from(document.querySelectorAll('#categoriesOptions .filter-button.active'))
        .map(button => button.getAttribute('data-value'))
        .join(','); // 카테고리 리스트를 쉼표로 구분된 문자열로 변환

    // URL 형식으로 쿼리 문자열을 생성
    const queryString = new URLSearchParams({
        itemIsDeleted,
        remainingTime,
        itemIsClosed,
        categories
    }).toString();

    // 필터가 적용되면서 페이지를 1로 초기화
    const url = new URL(window.location.href);
    const baseUrl = url.origin + url.pathname;
    window.location.href = `${baseUrl}?${queryString}&page=0`;
});

document.getElementById('close-filters').addEventListener('click', () => {
    document.getElementById('filter-popup').classList.remove('active');
});

// 페이징 링크 클릭 시 필터 상태를 유지하며 페이지 이동
document.querySelectorAll('.pagination .page-link').forEach(link => {
    link.addEventListener('click', (event) => {
        event.preventDefault();

        const page = event.target.getAttribute('data-page');
        const url = new URL(window.location.href);
        const params = new URLSearchParams(url.search);

        // 이미 있는 쿼리 파라미터를 유지한 채로 페이지 번호만 업데이트
        params.set('page', page);

        // 쿼리가 있을 경우 &page=, 없을 경우 ?page= 처리
        const queryString = params.toString();
        const baseUrl = url.origin + url.pathname;

        window.location.href = `${baseUrl}?${queryString}`;
    });
});

// 필터 초기화 (URL 파라미터 삭제)
document.getElementById('reset-filters').addEventListener('click', () => {
    const url = new URL(window.location.href);
    const baseUrl = url.origin + url.pathname;

    // 필터를 초기화하고 쿼리 파라미터 제거
    window.location.href = `${baseUrl}`;
});

// 처음 페이지 로드 시 URL에 따라 필터 상태 설정
document.addEventListener('DOMContentLoaded', setFilterStateFromUrl);