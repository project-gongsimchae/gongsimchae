package techit.gongsimchae.domain.portion.areas.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import techit.gongsimchae.domain.portion.areas.entity.SigunguArea;
import techit.gongsimchae.domain.portion.areas.repository.SigunguAreaRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SigunguAreaService {
    private final SigunguAreaRepository sigunguAreaRepository;

    public List<SigunguArea> getSigunguAreasBySidoAreaId(Long sidoAreaId){
        return sigunguAreaRepository.findBySidoAreaId(sidoAreaId);
    }
}
