package name.junnikym.consumptionHistory.history.service;

import lombok.RequiredArgsConstructor;
import name.junnikym.consumptionHistory.history.dto.HistoryCreateDTO;
import name.junnikym.consumptionHistory.history.dto.HistoryDetailDTO;
import name.junnikym.consumptionHistory.history.dto.HistorySummaryDTO;
import name.junnikym.consumptionHistory.history.dto.HistoryUpdateDTO;
import name.junnikym.consumptionHistory.history.repository.HistoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HistoryService {

	private final HistoryRepository historyRepository;

	public HistoryDetailDTO createHistory(@RequestBody HistoryCreateDTO dto) {
		return null;
	}

	public List<HistorySummaryDTO> getOwnHistoryList() {
		return null;
	}

	public HistoryDetailDTO getHistoryDetail(@PathVariable("id") UUID id) {
		return null;
	}

	public HistoryDetailDTO updateHistory(
			@PathVariable("id") UUID id,
			@RequestBody HistoryUpdateDTO dto
	) {
		return null;
	}

	public void updateHistory(@PathVariable("id") UUID id) {
		return;
	}

	public List<HistorySummaryDTO> updateHistory() {
		return null;
	}

	public void recoverHistory(@PathVariable("id") UUID id) {
		return;
	}

	public void recoverHistoryList(@RequestBody List<UUID> ids) {
		return;
	}

}
