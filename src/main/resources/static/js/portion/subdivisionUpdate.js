document.addEventListener('DOMContentLoaded', function () {
    const previewContainer = document.getElementById('imagePreview');
    const deleteImagesContainer = document.getElementById('deleteImagesContainer');
    let deleteImages = [];

    images.forEach(function (image) {
        const img = document.createElement('img');
        img.src = image.storeFilename;

        const deleteBtn = document.createElement('button');
        deleteBtn.innerHTML = '&times;';
        deleteBtn.classList.add('delete-btn');
        deleteBtn.onclick = function () {
            // 프론트엔드에서 이미지 미리보기 삭제
            previewContainer.removeChild(previewItem);

            // 삭제할 이미지 ID를 배열에 추가
            if (!deleteImages.includes(image.id)) {
                deleteImages.push(image.id);

                // 삭제할 이미지 ID를 추가하는 hidden input을 동적으로 생성하여 추가
                let input = document.createElement('input');
                input.type = 'hidden';
                input.name = 'deleteImages';
                input.value = image.id;
                deleteImagesContainer.appendChild(input);
            }
        };

        const previewItem = document.createElement('div');
        previewItem.classList.add('preview-item');
        previewItem.appendChild(img);
        previewItem.appendChild(deleteBtn);
        previewContainer.appendChild(previewItem);
    });
});

document.getElementById('imageFile').addEventListener('change', function() {
    const previewContainer = document.getElementById('imagePreview');

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

document.addEventListener('DOMContentLoaded', function () {
    let lat = document.getElementById("latitude").value;
    let lon = document.getElementById("longitude").value;

    console.log(lat)
    console.log(lon)

    function displayMarker(locPosition, message, lat, lon) {
        let mapContainer = document.getElementById('map');
        let mapOption = {
            center: new kakao.maps.LatLng(lat, lon),
            level: 3
        };

        let map = new kakao.maps.Map(mapContainer, mapOption);
        let geocoder = new kakao.maps.services.Geocoder();
        let marker = new kakao.maps.Marker({ position: new kakao.maps.LatLng(lat, lon) });

        marker.setMap(map);

        kakao.maps.event.addListener(map, 'click', function(mouseEvent) {
            searchDetailAddrFromCoords(mouseEvent.latLng, function(result, status) {
                if (status === kakao.maps.services.Status.OK) {
                    let detailAddr = result[0].address.address_name;
                    let sigungu = result[0].address.region_2depth_name;

                    marker.setPosition(mouseEvent.latLng);
                    marker.setMap(map);

                    // 도로명 주소, 위도, 경도 값을 hidden input에 설정합니다
                    document.getElementById('address').value = detailAddr;
                    document.getElementById('sigungu').value = sigungu;
                    document.getElementById('latitude').value = mouseEvent.latLng.getLat();
                    document.getElementById('longitude').value = mouseEvent.latLng.getLng();
                }
            });
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

    // 초기 마커 설정
    displayMarker(new kakao.maps.LatLng(lat, lon), 'Initial Location', lat, lon);
});