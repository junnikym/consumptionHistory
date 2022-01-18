package name.junnikym.consumptionHistory.history.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data @Builder
@AllArgsConstructor
public class HistoryCreateDTO {

	private UUID id;

	private Long amount;

	private String summaryMemo;

	private String detailMemo;

	//@TODO : Add Member

}
