package name.junnikym.consumptionHistory.history.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data @Builder
@AllArgsConstructor
public class HistorySummaryDTO {

	private UUID id;

	private String summaryMemo;

}
