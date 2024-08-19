document.getElementById('imageFile').addEventListener('change', function() {
    const previewContainer = document.getElementById('imagePreview');
    previewContainer.innerHTML = ''; // Clear previous preview

    const files = this.files; // Get all selected files
    for (let i = 0; i < files.length; i++) {
        const file = files[i];
        if (file) {
            const reader = new FileReader();
            reader.onload = function(e) {
                const img = document.createElement('img');
                img.src = e.target.result;
                const deleteBtn = document.createElement('button');
                deleteBtn.innerHTML = '&times;';
                deleteBtn.classList.add('delete-btn');
                deleteBtn.onclick = function() {
                    previewContainer.removeChild(previewItem); // Remove the image and button
                };
                const previewItem = document.createElement('div');
                previewItem.classList.add('preview-item');
                previewItem.appendChild(img);
                previewItem.appendChild(deleteBtn);
                previewContainer.appendChild(previewItem);
            };
            reader.readAsDataURL(file);
        }
    }
});

document.addEventListener('DOMContentLoaded', async function () {
    function currentLocation() {
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(function(position) {
                let lat = position.coords.latitude;
                let lon = position.coords.longitude;
                let locPosition = new kakao.maps.LatLng(lat, lon);
                let message = '<div style="padding:5px;">현위치</div>';

                displayMarker(locPosition, message, lat, lon);
            });
        } else {
            let locPosition = new kakao.maps.LatLng(37.4812845080678, 126.952713197762),
                message = '현재 위치를 알 수 없어 기본 위치로 이동합니다.';

            displayMarker(locPosition, message, 37.4812845080678, 126.952713197762);
        }
    }

    function displayMarker(locPosition, message, lat, lon) {
        let mapContainer = document.getElementById('map');
        let mapOption = {
            center: new kakao.maps.LatLng(lat, lon),
            level: 3
        };

        let map = new kakao.maps.Map(mapContainer, mapOption);
        let geocoder = new kakao.maps.services.Geocoder();
        let marker = new kakao.maps.Marker();

        searchAddrFromCoords(map.getCenter(), displayCenterInfo);

        kakao.maps.event.addListener(map, 'click', function(mouseEvent) {
            searchDetailAddrFromCoords(mouseEvent.latLng, function(result, status) {
                if (status === kakao.maps.services.Status.OK) {
                    let detailAddr = result[0].address.address_name;

                    marker.setPosition(mouseEvent.latLng);
                    marker.setMap(map);

                    // 도로명 주소, 위도, 경도 값을 hidden input에 설정합니다
                    document.getElementById('address').value = detailAddr;
                    document.getElementById('latitude').value = mouseEvent.latLng.getLat();
                    document.getElementById('longitude').value = mouseEvent.latLng.getLng();
                }
            });
        });

        kakao.maps.event.addListener(map, 'idle', function() {
            searchAddrFromCoords(map.getCenter(), displayCenterInfo);
        });

        function searchAddrFromCoords(coords, callback) {
            geocoder.coord2RegionCode(coords.getLng(), coords.getLat(), callback);
        }

        function searchDetailAddrFromCoords(coords, callback) {
            geocoder.coord2Address(coords.getLng(), coords.getLat(), callback);
        }

        function displayCenterInfo(result, status) {
        }
    }

    currentLocation();
});
