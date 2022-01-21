package name.junnikym.consumptionHistory.history.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import name.junnikym.consumptionHistory.history.domain.History;

import java.util.UUID;

@Data @Builder
@AllArgsConstructor
public class HistoryUpdateDTO {

	private Long amount;

	private String summaryMemo;

	private String detailMemo;

	public History toEntity(UUID id) {
		return History.builder()
				.id(id)
				.amount(this.amount)
				.summaryMemo(this.summaryMemo)
				.detailMemo(this.detailMemo)
				.build();
	}

}
