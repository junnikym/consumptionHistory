package name.junnikym.consumptionHistory.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import name.junnikym.consumptionHistory.member.domain.Member;

@Data @Builder
@AllArgsConstructor
public class MemberSignupDTO {

	private String email;

	private String password;

	public Member toEntity() {
		return Member.builder()
				.email(this.email)
				.password(this.password)
				.build();
	}

}
