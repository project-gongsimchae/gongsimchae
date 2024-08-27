package techit.gongsimchae.domain.portion.report.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techit.gongsimchae.domain.common.user.entity.User;
import techit.gongsimchae.domain.common.user.repository.UserRepository;
import techit.gongsimchae.domain.portion.report.dto.ReportCreateReqDtoWeb;
import techit.gongsimchae.domain.portion.report.dto.ReportRespDtoWeb;
import techit.gongsimchae.domain.portion.report.entity.Report;
import techit.gongsimchae.domain.portion.report.repository.ReportRepository;
import techit.gongsimchae.domain.portion.subdivision.entity.Subdivision;
import techit.gongsimchae.domain.portion.subdivision.repository.SubdivisionRepository;
import techit.gongsimchae.global.dto.PrincipalDetails;
import techit.gongsimchae.global.exception.CustomWebException;
import techit.gongsimchae.global.message.ErrorMessage;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final SubdivisionRepository subdivisionRepository;


    @Transactional
    public Long createReport(ReportCreateReqDtoWeb reportCreateReqDto,PrincipalDetails principalDetails) {
        User user = userRepository.findByLoginId(principalDetails.getUsername()).orElseThrow(() -> new CustomWebException(ErrorMessage.USER_NOT_FOUND));
        Subdivision subdivision = subdivisionRepository.findByUID(reportCreateReqDto.getUid()).orElseThrow(() -> new CustomWebException(ErrorMessage.SUBDIVISION_NOT_FOUND));
        Report report = new Report(reportCreateReqDto, user, subdivision);
        Report savedReport = reportRepository.save(report);
        isReportCountMultipleOfFive(subdivision);
        return savedReport.getId();
    }

    public Page<ReportRespDtoWeb> getSubdivisionReport(Long id, Pageable pageable) {
        return reportRepository.findReportsForSubdivision(id,pageable);
    }
    @Transactional
    public void deleteAllReport(String subdivisionUID) {
        reportRepository.deleteAllBySubdivisionUID(subdivisionUID);
    }

    private void isReportCountMultipleOfFive(Subdivision subdivision) {
        Long count = reportRepository.countBySubdivision(subdivision);
        if (count != 0 && count % 5 == 0) {
            User user = subdivision.getUser();
            user.decrementMannerPoints();
        }
    }
}
