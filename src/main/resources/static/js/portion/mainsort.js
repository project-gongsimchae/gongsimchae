function updateURLAndFetch(page) {
    let url = new URL(window.location.origin + '/portioning');

    // 페이지 번호 추가
    if (page !== undefined) {
        url.searchParams.set('page', page);
    } else {
        // 페이지 번호가 없는 경우 URL에서 기존 페이지 번호를 가져옴
        let currentPage = new URLSearchParams(window.location.search).get('page');
        if (currentPage) {
            url.searchParams.set('page', currentPage);
        } else {
            url.searchParams.delete('page'); // 기본값은 첫 페이지이므로 삭제
        }
    }

    // 검색어 추가
    let query = document.querySelector('.content-wrapper input').value;
    if (query) {
        url.searchParams.set('query', query);
    } else {
        url.searchParams.delete('query');
    }

    // 판매중 체크박스 상태 반영
    let onSaleChecked = document.getElementById('onSaleCheckbox').checked;
    if (onSaleChecked) {
        url.searchParams.set('onSale', 'true');
    } else {
        url.searchParams.delete('onSale');
    }

    // 정렬 옵션 반영
    let sortNew = document.getElementById('sortNew');
    let sortPopular = document.getElementById('sortPopular');
    if (sortNew.classList.contains('active')) {
        url.searchParams.set('sort', 'new');
    } else if (sortPopular.classList.contains('active')) {
        url.searchParams.set('sort', 'popular');
    } else {
        url.searchParams.delete('sort');
    }

    // URL 업데이트
    window.history.pushState({}, '', url);

    // AJAX 요청
    fetch(url)
        .then(response => response.text())
        .then(html => {
            const parser = new DOMParser();
            const doc = parser.parseFromString(html, 'text/html');
            const newSubdivisionList = doc.getElementById('subdivisionList');
            document.getElementById('subdivisionList').innerHTML = newSubdivisionList.innerHTML;

            // 현재 페이지에 active 클래스 추가
            const currentPage = doc.querySelector('.pagination .page-item.active a')?.textContent.trim() || '1';
            document.querySelectorAll('.pagination .page-item').forEach(item => {
                item.classList.remove('active');
                if (item.querySelector('a').textContent.trim() === currentPage) {
                    item.classList.add('active');
                }
            });
        })
        .catch(error => console.error('Error:', error));
}

// 페이지네이션 링크 클릭 시 AJAX 요청
document.querySelectorAll('.pagination .page-link').forEach(link => {
    link.addEventListener('click', function(e) {
        e.preventDefault();
        let page = this.getAttribute('data-page');
        if (page) {
            updateURLAndFetch(page);
        }
    });
});

// 체크박스 클릭 시 URL 업데이트 및 데이터 가져오기
document.getElementById('onSaleCheckbox').addEventListener('change', function() {
    updateURLAndFetch();
});

// 정렬 링크 클릭 시 URL 업데이트 및 스타일 변경, 데이터 가져오기
document.getElementById('sortNew').addEventListener('click', function(e) {
    e.preventDefault();
    let isActive = this.classList.contains('active');
    document.getElementById('sortPopular').classList.remove('active');

    if (isActive) {
        this.classList.remove('active');
        updateURLAndFetch(); // 'new' 정렬 파라미터 제거
    } else {
        this.classList.add('active');
        updateURLAndFetch(); // 'new' 정렬 파라미터 추가
    }
});

document.getElementById('sortPopular').addEventListener('click', function(e) {
    e.preventDefault();
    let isActive = this.classList.contains('active');
    document.getElementById('sortNew').classList.remove('active');

    if (isActive) {
        this.classList.remove('active');
        updateURLAndFetch(); // 'popular' 정렬 파라미터 제거
    } else {
        this.classList.add('active');
        updateURLAndFetch(); // 'popular' 정렬 파라미터 추가
    }
});

// 검색어 입력 필드에 입력할 때마다 URL 업데이트 및 데이터 가져오기
let debounceTimer;
document.querySelector('.content-wrapper input').addEventListener('input', function() {
    clearTimeout(debounceTimer);
    debounceTimer = setTimeout(updateURLAndFetch, 300); // 300ms 디바운스
});

// 페이지 로드 시 URL 파라미터에 따라 UI 상태 설정
window.addEventListener('load', function() {
    let params = new URLSearchParams(window.location.search);

    // 검색어 설정
    let query = params.get('query');
    if (query) {
        document.querySelector('.content-wrapper input').value = query;
    }

    // 판매중 체크박스 설정
    let onSale = params.get('onSale');
    document.getElementById('onSaleCheckbox').checked = onSale === 'true';

    // 정렬 옵션 설정
    let sort = params.get('sort');
    if (sort === 'new') {
        document.getElementById('sortNew').classList.add('active');
    } else if (sort === 'popular') {
        document.getElementById('sortPopular').classList.add('active');
    }
});
