let selectedTags = {
    sidoArea: null,
    sigunguArea: null,
    myeondongeupArea: null
};

let tags = []; // 선택된 태그들을 저장할 배열

function selectSidoArea(element) {
    setActiveSidoArea(element);
    selectedTags.sidoArea = element.textContent;
    // 시도 선택 시 기존 동(읍면동) 정보는 초기화하고 태그 업데이트를 하지 않음
    selectedTags.myeondongeupArea = null;
    $('#myeondongeupAreaList').empty();
    $('.myeondongeup-areas').hide();
    updateTags();

    const sidoAreaId = element.getAttribute('data-sidoArea-id');
    $.get(`${contextPath}sigungus?sidoAreaId=${sidoAreaId}`, function(data) {
        $('#sigunguAreaList').html(data).addClass('area-list');
        $('.sigungu-areas').show();
    }).fail(function(jqXHR, textStatus, errorThrown) {
        console.error("AJAX request failed:", textStatus, errorThrown);
    });
}

function selectSigunguArea(element) {
    setActiveSigunguArea(element);
    selectedTags.sigunguArea = element.textContent;
    // 시군구 선택 시 기존 동(읍면동) 정보는 초기화하고 태그 업데이트를 하지 않음
    selectedTags.myeondongeupArea = null;
    $('#myeondongeupAreaList').empty();
    $('.myeondongeup-areas').hide();
    updateTags();

    const sigunguAreaId = element.getAttribute('data-sigunguArea-id');
    $.get(`${contextPath}myeondongeups?sigunguAreaId=${sigunguAreaId}`, function(data) {
        $('#myeondongeupAreaList').html(data).addClass('area-list');
        $('.myeondongeup-areas').show();
    }).fail(function(jqXHR, textStatus, errorThrown) {
        console.error("AJAX request failed:", textStatus, errorThrown);
    });
}

function selectMyeondongeupArea(element) {
    setActiveMyeondongeupArea(element);
    selectedTags.myeondongeupArea = element.textContent;
    updateTags(); // 읍면동 선택 시 태그 업데이트
}

function setActiveSidoArea(element) {
    $('#sidoAreaList li').removeClass('active');
    $(element).addClass('active');
}

function setActiveSigunguArea(element) {
    $('#sigunguAreaList li').removeClass('active');
    $(element).addClass('active');
}

function setActiveMyeondongeupArea(element) {
    $('#myeondongeupAreaList li').removeClass('active');
    $(element).addClass('active');
}

function updateTags() {
    // 모든 필드가 선택된 경우에만 태그를 추가
    if (selectedTags.sidoArea && selectedTags.sigunguArea && selectedTags.myeondongeupArea) {
        const tagText = `${selectedTags.sidoArea} ${selectedTags.sigunguArea} ${selectedTags.myeondongeupArea}`;

        // 태그가 배열에 이미 있는지 확인
        if (!tags.includes(tagText)) {
            tags.push(tagText);
            addTag(tagText);
        }
    }
}

function addTag(area) {
    // 태그가 이미 존재하는지 확인
    if ($('#selectedTags .tag').filter(function() { return $(this).text().includes(area); }).length === 0) {
        const tag = $('<span>').text(area).addClass('tag');
        const closeButton = $('<span>').html('&times;').addClass('close-btn');

        closeButton.on('click', function() {
            removeTag(area);
        });

        tag.append(closeButton);
        $('#selectedTags').append(tag);
    }
}

function removeTag(area) {
    const tagElement = $('#selectedTags .tag').filter(function() { return $(this).text().includes(area); });
    if (tagElement.length) {
        tagElement.remove();

        // 태그 배열에서 해당 태그 제거
        tags = tags.filter(tag => tag !== area);

        // 해당 태그에 맞는 selectedTags 값을 null로 설정
        const parts = area.split(' ');
        if (parts[0] === selectedTags.sidoArea) {
            selectedTags.sidoArea = null;
        }
        if (parts[1] === selectedTags.sigunguArea) {
            selectedTags.sigunguArea = null;
        }
        if (parts[2] === selectedTags.myeondongeupArea) {
            selectedTags.myeondongeupArea = null;
        }

        updateTags();
    }
}

document.addEventListener('DOMContentLoaded', function() {
    $('.sigungu-areas, .myeondongeup-areas').hide();
});

$(document).ready(function() {
    $('#searchTagsButton').on('click', function() {
        // 선택된 태그를 가져옵니다
        let tags = [];
        $('#selectedTags .tag').each(function() {
            tags.push($(this).text().trim());
        });

        // 태그를 기반으로 검색을 수행
        if (tags.length > 0) {
            // 검색 쿼리 문자열 생성
            let query = tags.join(' ');
            // 검색 로직 구현 (예: 서버로 검색 요청 보내기)
            console.log('검색 쿼리:', query);

            // 여기서 검색 요청을 보낼 수 있습니다 (예: AJAX)
            // $.get(`${contextPath}/search?query=${encodeURIComponent(query)}`, function(data) {
            //     // 검색 결과 처리
            // }).fail(function(jqXHR, textStatus, errorThrown) {
            //     console.error("검색 요청 실패:", textStatus, errorThrown);
            // });
        } else {
            alert('검색할 태그를 선택하세요.');
        }
    });
});
