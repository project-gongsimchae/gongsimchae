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
            // 전체 페이지 새로 고침
            location.reload();
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

// 정렬 링크 클릭 시 URL 업데이트 및 데이터 가져오기
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

// 페이지 로드 시 URL 파라미터에 따라 UI 상태 설정
window.addEventListener('load', function() {
    let params = new URLSearchParams(window.location.search);

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
