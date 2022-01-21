package name.junnikym.consumptionHistory.history.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import name.junnikym.consumptionHistory.history.domain.History;

@Data @Builder
@AllArgsConstructor
public class HistoryCreateDTO {

	private Long amount;

	private String summaryMemo;

	private String detailMemo;

	public History toEntity() {

		return History.builder()
				.amount(this.amount)
				.summaryMemo(this.summaryMemo)
				.detailMemo(this.detailMemo)
				.build();

	}

}
