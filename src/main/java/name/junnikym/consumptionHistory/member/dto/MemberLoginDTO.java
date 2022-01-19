package name.junnikym.consumptionHistory.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data @Builder
@AllArgsConstructor
public class MemberLoginDTO {

	private String email;

	private String password;

}
