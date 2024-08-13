document.addEventListener('DOMContentLoaded', function () {
    let joinButton;

    // 참여 버튼 클릭 시 모달 표시
    if (document.getElementById('joinButton') != null) {
        joinButton = document.getElementById('joinButton');

        joinButton.addEventListener('click', function () {
            modal.style.display = 'flex';
        });
    }

    const modal = document.getElementById('modal');
    const confirmButton = document.getElementById('confirmButton');
    const cancelButton = document.getElementById('cancelButton');

    confirmButton.addEventListener('click', function () {
        const UID = subdivisionRespDto.UID; // subdivisionDto.UID를 사용하여 UID 값을 가져옴
        window.location.href = `/portioning/${UID}/join`;
    });

    cancelButton.addEventListener('click', function () {
        modal.style.display = 'none';
    });

    // 수정 버튼 클릭 시 URL로 리다이렉트
    const editButton = document.getElementById('editButton');
    if (editButton) {
        editButton.addEventListener('click', function () {
            const UID = subdivisionRespDto.UID;
            window.location.href = `/portioning/${UID}/update`;
        });
    }

    // 삭제 버튼 관련 모달 처리
    const deleteModal = document.getElementById('deleteModal');
    const deleteButton = document.getElementById('deleteButton');
    const cancelDeleteButton = document.getElementById('cancelDeleteButton');

    if (deleteButton) {
        deleteButton.addEventListener('click', function () {
            deleteModal.style.display = 'flex';
        });
    }

    cancelDeleteButton.addEventListener('click', function () {
        deleteModal.style.display = 'none';
    });

    // 주소를 기반으로 Kakao API를 호출하여 위도와 경도 값을 가져옴
    const placeAddress = subdivisionRespDto.address;

    const url = `https://dapi.kakao.com/v2/local/search/address.json?query=${encodeURIComponent(placeAddress)}`;
    fetch(url, {
        method: 'GET',
        headers: {
            'Authorization': 'KakaoAK 208dd761015736055a3cf128a1642526', // KakaoAK 뒤에 REST API KEY를 입력
        },
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Kakao API request failed');
            }
            return response.json();
        })
        .then(axiosResult => {
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

        })
        .catch(error => {
            console.error('Error fetching location data:', error);
        });
});
