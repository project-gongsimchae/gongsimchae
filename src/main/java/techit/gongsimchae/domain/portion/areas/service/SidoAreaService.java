package techit.gongsimchae.domain.portion.areas.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import techit.gongsimchae.domain.portion.areas.entity.SidoArea;
import techit.gongsimchae.domain.portion.areas.repository.SidoAreaRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SidoAreaService {
    private final SidoAreaRepository sidoAreaRepository;

    public List<SidoArea> getAllSidoAreas() {
        return sidoAreaRepository.findAll();
    }
}
