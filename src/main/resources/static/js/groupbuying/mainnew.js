let currentPage = 0;
const pageSize = 8;
let isLoading = false;

function formatNumber(num) {
    // 정수만 나오도록 설정
    return Math.floor(num).toLocaleString('en-US');
}

// 데이터를 받아와서 화면에 추가하는 함수
function appendRecentItems(items) {
    const recentProductList = document.getElementById('recent-product-list');
    items.forEach(item => {
        console.log(item)
        const itemElement = document.createElement('div');
        itemElement.className = 'product-item';

        const originalPrice = formatNumber(item.originalPrice);
        const discountPrice = formatNumber(item.originalPrice * (100 - item.discountRate) / 100);
        const progressBarWidth = (item.groupBuyingQuantity / 100.0) * 100;

        itemElement.innerHTML = `
            <a href="/product/${item.id}">
                <img class="product-image-placeholder" src="${item.itemBannerImage}" alt="썸네일 이미지" />
                <div class="item-simple-content">
                    <h3 class="item-name">${item.name}</h3>
                    <div class="price-container">
                        <div class="price-progress-container">
                            <span class="original-price">${originalPrice}원</span>
                            <div class="progress-container">
                                <div class="custom-progress">
                                    <div class="custom-progress-bar" role="progressbar"
                                         style="width: ${progressBarWidth}%; max-width: 100%;"
                                         aria-valuenow="${item.groupBuyingQuantity}"
                                         aria-valuemin="0" aria-valuemax="100">
                                        <span>${item.groupBuyingQuantity}%</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="discount-info">
                            <span class="price-info">
                                <span class="discount-rate">${item.discountRate}%</span>
                                <span class="discount-price">${discountPrice}원</span>
                            </span>
                            <span class="buying-people">${item.groupBuyingQuantity}명 구매중</span>
                        </div>
                    </div>
                </div>
            </a>
        `;

        recentProductList.appendChild(itemElement);
    });
}

// API에서 데이터 불러오는 함수
async function loadRecentItems(page, size) {
    if (isLoading) return;
    isLoading = true;
    document.getElementById('loading').style.display = 'block';

    try {
        const response = await fetch(`/product/recent?page=${page}&size=${size}`);
        const data = await response.json();

        appendRecentItems(data.content);

        document.getElementById('loading').style.display = 'none';
        isLoading = false;

        // 마지막 페이지에 도달하면 더 이상 스크롤 이벤트 발생하지 않음
        if (data.last) {
            window.removeEventListener('scroll', handleScroll);
        }
    } catch (error) {
        console.error('Error loading recent items:', error);
        document.getElementById('loading').style.display = 'none';
        isLoading = false;
    }
}

// 스크롤 감지 및 무한 스크롤 처리
function handleScroll() {
    const { scrollTop, scrollHeight, clientHeight } = document.documentElement;
    if (scrollTop + clientHeight >= scrollHeight - 100 && !isLoading) {
        currentPage++;
        loadRecentItems(currentPage, pageSize);
    }
}

// 초기 로딩
loadRecentItems(currentPage, pageSize);

// 스크롤 이벤트 추가
window.addEventListener('scroll', handleScroll)