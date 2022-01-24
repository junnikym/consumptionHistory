package name.junnikym.consumptionHistory.history.dto;

import lombok.*;
import name.junnikym.consumptionHistory.history.domain.History;

import java.util.UUID;

@Data @Builder
@AllArgsConstructor
public class HistoryDetailDTO {

	private UUID id;

	private Long amount;

	private String summaryMemo;

	private String detailMemo;

}
