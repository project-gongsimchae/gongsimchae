// 페이지 언로드 직전에 스크롤 위치 저장
window.addEventListener('beforeunload', function() {
    sessionStorage.setItem('scrollPosition', window.pageYOffset);
});

// 페이지 로드 후 스크롤 위치 복원
window.addEventListener('load', function() {
    const scrollPosition = sessionStorage.getItem('scrollPosition');
    if (scrollPosition !== null) {
        // 페이지 로드가 완료되면 스크롤 위치를 복원
        setTimeout(function() {
            window.scrollTo(0, parseInt(scrollPosition, 10));
            sessionStorage.removeItem('scrollPosition'); // 복원 후 위치 삭제
        }, 0); // 페이지 로드 후 즉시 수행
    }
});
 // 요소 선택
document.addEventListener('DOMContentLoaded', async function () {
    const joinButton = document.getElementById('joinButton');
    const modal = document.getElementById('modal');
    const confirmButton = document.getElementById('confirmButton');
    const cancelButton = document.getElementById('cancelButton');

    joinButton.addEventListener('click', function () {
        modal.style.display = 'flex';
    });

    confirmButton.addEventListener('click', function () {
        const UID = subdivisionRespDto.UID; // subdivisionDto.UID를 사용하여 UID 값을 가져옴
        window.location.href = `/portioning/${UID}/join`;
    });

    cancelButton.addEventListener('click', function () {
        modal.style.display = 'none';
    });

    // 주소를 기반으로 Kakao API를 호출하여 위도와 경도 값을 가져옴
    const placeAddress = subdivisionRespDto.address;

    const url = `https://dapi.kakao.com/v2/local/search/address.json?query=${encodeURIComponent(placeAddress)}`;
    try {
        const response = await fetch(url, {
            method: 'GET',
            headers: {
                'Authorization': 'KakaoAK 208dd761015736055a3cf128a1642526', // KakaoAK 뒤에 REST API KEY를 입력
            },
        });

        if (!response.ok) {
            throw new Error('Kakao API request failed');
        }

        const axiosResult = await response.json();
        const latitude = axiosResult.documents[0].address.y;   // 위도(latitude)
        const longitude = axiosResult.documents[0].address.x;  // 경도(longitude)

        var mapContainer = document.getElementById('map'), // 지도를 표시할 div
            mapOption = {
                center: new kakao.maps.LatLng(latitude, longitude), // 지도의 중심좌표
                level: 3 // 지도의 확대 레벨
            };

        var map = new kakao.maps.Map(mapContainer, mapOption); // 지도를 생성합니다

        // 마커가 표시될 위치입니다
        var markerPosition  = new kakao.maps.LatLng(latitude, longitude);

        // 마커를 생성합니다
        var marker = new kakao.maps.Marker({
            position: markerPosition
        });

        // 마커가 지도 위에 표시되도록 설정합니다
        marker.setMap(map);

    } catch (error) {
        console.error('Error fetching location data:', error);
    }
});
