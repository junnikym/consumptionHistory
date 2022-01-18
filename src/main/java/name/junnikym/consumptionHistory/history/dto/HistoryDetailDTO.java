package name.junnikym.consumptionHistory.history.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data @Builder
@AllArgsConstructor
public class HistoryDetailDTO {

	private String summaryMemo;

	private String detailMemo;

}
