package name.junnikym.consumptionHistory.history.dto;

import lombok.*;
import name.junnikym.consumptionHistory.history.domain.History;

@Data @Builder
@AllArgsConstructor
public class HistoryDetailDTO {

	private Long amount;

	private String summaryMemo;

	private String detailMemo;

}
