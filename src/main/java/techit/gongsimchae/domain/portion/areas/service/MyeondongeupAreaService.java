package techit.gongsimchae.domain.portion.areas.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import techit.gongsimchae.domain.portion.areas.entity.MyeondongeupArea;
import techit.gongsimchae.domain.portion.areas.repository.MyeondongeupAreaRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MyeondongeupAreaService {
    private final MyeondongeupAreaRepository myeondongeupAreaRepository;

    public List<MyeondongeupArea> getMyeondongeupAreasBySigunguAreaId(Long sigunguAreaId) {
        return myeondongeupAreaRepository.findBySigunguAreaId(sigunguAreaId);
    }
}
