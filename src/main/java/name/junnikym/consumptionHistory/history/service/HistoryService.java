package name.junnikym.consumptionHistory.history.service;

import lombok.RequiredArgsConstructor;
import name.junnikym.consumptionHistory.exception.NotFoundException;
import name.junnikym.consumptionHistory.history.domain.History;
import name.junnikym.consumptionHistory.history.dto.HistoryCreateDTO;
import name.junnikym.consumptionHistory.history.dto.HistoryDetailDTO;
import name.junnikym.consumptionHistory.history.dto.HistorySummaryDTO;
import name.junnikym.consumptionHistory.history.dto.HistoryUpdateDTO;
import name.junnikym.consumptionHistory.history.repository.HistoryRepository;
import name.junnikym.consumptionHistory.member.domain.Member;
import name.junnikym.consumptionHistory.util.FindNullProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class HistoryService {

	private final HistoryRepository historyRepository;

	/**
	 * 소비내역 기록을 생성
	 *
	 * @param writer : 기록자 ( Memeber )
	 * @param dto : 소비내역 기록내용
	 * @return 기록된 소비내역 내용
	 */
	public History createHistory(Member writer, HistoryCreateDTO dto) {
		History entity = dto.toEntity();
		entity.setWriter(writer);

		return historyRepository.save(entity);
	}

	/**
	 * 사용자가 쓴 소비내역 목록 조회
	 *
	 * @param writer : 기록자 ( Memeber )
	 * @param isDeleted : True 시 삭제된 기록 / False 시 정상적인 소비내역 목록
	 * @return 소비내역 목록
	 */
	public List<HistorySummaryDTO> getOwnHistoryList(Member writer, Boolean isDeleted) {
		return historyRepository.getHistorySummaryDTOByWriterAndIsDeleted(writer, isDeleted)
				.orElseThrow(IllegalArgumentException::new);
	}

	/**
	 * 선택된 소비내역 기록을 자세히 조회
	 *
	 * @param writer : 기록자 ( Memeber )
	 * @param id : 조회 소비내역 고유 ID
	 * @return 소비내역 ( 자세히 )
	 */
	public HistoryDetailDTO getHistoryDetail(Member writer, UUID id) {
		return historyRepository.getHistoryDetailDTOByWriterAndId(writer, id)
				.orElseThrow(()->new NotFoundException("History"));
	}

	/**
	 * 선택된 소비내역 기록을 수정
	 *
	 * @param id : 소비내역 고유 ID
	 * @param dto : 수정하고자하는 내용
	 * @return 수정된 소비내역
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public HistoryDetailDTO updateHistory(
			Member writer,
			UUID id,
			HistoryUpdateDTO dto
	) throws InvocationTargetException, IllegalAccessException {
		History history = historyRepository.findByWriterAndId(writer, id)
				.orElseThrow(()->new NotFoundException("History"));

		FindNullProperties.convertWithoutNull(dto, history);
		historyRepository.save(history);

		HistoryDetailDTO result = HistoryDetailDTO.builder().build();
		return FindNullProperties.convertWithoutNull(history, result);
	}

	/**
	 * 선택된 소비내역 하나를 삭제/복구
	 *
	 * @param id : 소비내역 고유 ID
	 * @param isDelete : true 시 삭제 기능 / false 시 복구 기능
	 * @throws Exception
	 */
	public void deleteOrRecoverHistory(
			Member writer,
			UUID id,
			Boolean isDelete
	) throws Exception {
		Integer result = historyRepository.deleteOrRecoverHistory(writer, id, isDelete);

		if(result == 1)
			return;
		else if(result == 0)
			throw new NotFoundException("History");

		throw new Exception();
	}

	/**
	 * 선택된 소비내역 리스트를 모두 삭제/복구
	 *
	 * @param ids : 소비내역 고유 ID 리스트
	 * @param isDelete : true 시 삭제 기능 / false 시 복구 기능
	 */
	public void recoverHistoryList(
			Member writer,
			List<UUID> ids,
			Boolean isDelete
	) {
		Integer result = historyRepository.deleteOrRecoverHistories(writer, ids, isDelete);
	}

}
