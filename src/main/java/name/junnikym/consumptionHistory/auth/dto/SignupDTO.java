package name.junnikym.consumptionHistory.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import name.junnikym.consumptionHistory.member.domain.Member;

@Data @Builder
@AllArgsConstructor
public class SignupDTO {

	private String email;

	private String password;

	public Member toEntity() {
		return Member.builder()
				.email(this.email)
				.password(this.password)
				.build();
	}

}
