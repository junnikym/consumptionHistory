package name.junnikym.consumptionHistory.history.api;

import name.junnikym.consumptionHistory.history.dto.HistoryCreateDTO;
import name.junnikym.consumptionHistory.history.dto.HistoryDetailDTO;
import name.junnikym.consumptionHistory.history.dto.HistorySummaryDTO;
import name.junnikym.consumptionHistory.history.dto.HistoryUpdateDTO;

import name.junnikym.consumptionHistory.history.service.HistoryService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1/history")
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
	public HistoryDetailDTO createHistory(@RequestBody HistoryCreateDTO dto) {
		return null;
	}

	/**
	 * 사용자의 소비내역 기록 리스트 조회
	 *
	 * @return 간단한 소비내역을 리스트형으로 반환
	 */
	@GetMapping()
	public List<HistorySummaryDTO> getOwnHistoryList() {
		return null;
	}

	/**
	 * 사용자의 소비내역 기록 한가지를 자세히 조회
	 *
	 * @param id : 조회 대상의 고유 ID
	 *
	 * @return 고유 ID에 해당하는 소비내역 반환
	 */
	@GetMapping("/{id}")
	public HistoryDetailDTO getHistoryDetail(@PathVariable("id") UUID id) {
		return null;
	}

	/**
	 * 사용자의 소비내역을 수정
	 *
	 * @param id : 수정 대상의 고유 ID
	 * @param dto : 수정하고자하는 내용
	 *
	 * @return 최종적으로 수정된 소비내역
	 */
	@PutMapping("/{id}")
	public HistoryDetailDTO updateHistory(
			@PathVariable("id") UUID id,
			@RequestBody HistoryUpdateDTO dto
	) {
		return null;
	}

	/**
	 * 사용자의 소비내역을 삭제
	 * @param id : 수정 대상의 고우 ID
	 */
	@DeleteMapping("/{id}")
	public void updateHistory(@PathVariable("id") UUID id) {
		return;
	}

	/**
	 * 삭제된 소비내역의 리스트 조회
	 *
	 * @return 삭제된 소비내역을 리스트로 반환
	 */
	@GetMapping("/deleted")
	public List<HistorySummaryDTO> updateHistory() {
		return null;
	}

	/**
	 * 고유 ID에 해당하는 삭제된 소비내역의 리스트를 복구
	 *
	 * @param id : 복구 대상의 고유 ID
	 */
	@PatchMapping("/recover/{id}")
	public void recoverHistory(@PathVariable("id") UUID id) {
		return;
	}

	/**
	 * 복구 목록에 해당하는 소비내역을 복구
	 *
	 * @param ids : 복구 대상의 고유 ID 리스트
	 */
	@PatchMapping("/recover")
	public void recoverHistoryList(@RequestBody List<UUID> ids) {
		return;
	}

}
