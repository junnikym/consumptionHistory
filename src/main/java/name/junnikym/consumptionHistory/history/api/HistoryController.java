package name.junnikym.consumptionHistory.history.api;

import name.junnikym.consumptionHistory.history.domain.History;
import name.junnikym.consumptionHistory.history.dto.HistoryCreateDTO;
import name.junnikym.consumptionHistory.history.dto.HistoryDetailDTO;
import name.junnikym.consumptionHistory.history.dto.HistorySummaryDTO;
import name.junnikym.consumptionHistory.history.dto.HistoryUpdateDTO;

import name.junnikym.consumptionHistory.history.service.HistoryService;

import lombok.RequiredArgsConstructor;
import name.junnikym.consumptionHistory.member.domain.Member;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1/history", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor()
public class HistoryController {

	private final HistoryService historyService;

	/**
	 * 소비내역 기록을 생성
	 *
	 * @param dto : 소비내역 기록내용
	 * @return 기록된 소비내역
	 */
	@PostMapping()
	public History createHistory(
			@AuthenticationPrincipal Member writer,
			@RequestBody HistoryCreateDTO dto
	) {
		return historyService.createHistory(writer, dto);
	}

	/**
	 * 사용자의 소비내역 기록 리스트 조회
	 *
	 * @return 간단한 소비내역을 리스트형으로 반환
	 */
	@GetMapping()
	public List<HistorySummaryDTO> getOwnHistoryList(
			@AuthenticationPrincipal Member writer
	) {
		return historyService.getOwnHistoryList(writer, false);
	}

	/**
	 * 사용자의 소비내역 기록 한가지를 자세히 조회
	 *
	 * @param id : 조회 대상의 고유 ID
	 *
	 * @return 소비내역 ( 자세히 )
	 */
	@GetMapping("/{id}")
	public HistoryDetailDTO getHistoryDetail(
			@AuthenticationPrincipal Member writer,
			@PathVariable("id") UUID id
	) {
		return historyService.getHistoryDetail(writer, id);
	}

	/**
	 * 사용자의 소비내역을 수정
	 *
	 * @param id : 소비내역 고유 ID
	 * @param dto : 수정하고자하는 내용
	 *
	 * @return 수정된 소비내역
	 */
	@PutMapping("/{id}")
	public HistoryDetailDTO updateHistory(
			@AuthenticationPrincipal Member writer,
			@PathVariable("id") UUID id,
			@RequestBody HistoryUpdateDTO dto
	) throws InvocationTargetException, IllegalAccessException {
		return historyService.updateHistory(writer, id, dto);
	}

	/**
	 * 사용자의 소비내역을 삭제
	 * @param id : 수정 대상의 고우 ID
	 */
	@DeleteMapping("/{id}")
	public void deleteHistory(
			@AuthenticationPrincipal Member writer,
			@PathVariable("id") UUID id
	) throws Exception {
		historyService.deleteOrRecoverHistory(writer, id, true);
	}

	/**
	 * 삭제 목록에 해당하는 소비내역을 삭제
	 *
	 * @param ids : 복구 대상의 고유 ID 리스트
	 */
	@DeleteMapping()
	public void deleteHistoryList(
			@AuthenticationPrincipal Member writer,
			@RequestParam("id") List<UUID> ids
	) {
		Integer result = historyService.deleteOrRecoverHistoryList(writer, ids, true);
	}

	/**
	 * 삭제된 소비내역의 리스트 조회
	 *
	 * @return 삭제된 소비내역을 리스트로 반환
	 */
	@GetMapping("/delete")
	public List<HistorySummaryDTO> getDeletedHistory(
			@AuthenticationPrincipal Member writer
	) {
		return historyService.getOwnHistoryList(writer, true);
	}

	/**
	 * 고유 ID에 해당하는 삭제된 소비내역의 리스트를 복구
	 *
	 * @param id : 복구 대상의 고유 ID
	 */
	@PatchMapping("/recover/{id}")
	public void recoverHistory(
			@AuthenticationPrincipal Member writer,
			@PathVariable("id") UUID id
	) throws Exception {
		historyService.deleteOrRecoverHistory(writer, id, false);
	}

	/**
	 * 복구 목록에 해당하는 소비내역을 복구
	 *
	 * @param ids : 복구 대상의 고유 ID 리스트
	 */
	@PatchMapping("/recover")
	public void recoverHistoryList(
			@AuthenticationPrincipal Member writer,
			@RequestParam("id") List<UUID> ids
	) {
		historyService.deleteOrRecoverHistoryList(writer, ids, false);
	}

}
