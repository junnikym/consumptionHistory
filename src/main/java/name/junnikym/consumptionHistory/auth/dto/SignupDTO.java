package name.junnikym.consumptionHistory.auth.dto;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import name.junnikym.consumptionHistory.member.domain.Member;

import javax.validation.constraints.Email;

@Data @Builder
@AllArgsConstructor
public class SignupDTO {

	@Email
	@NotNull
	private String email;

	@NotNull
	private String password;

	public Member toEntity() {
		return Member.builder()
				.email(this.email)
				.password(this.password)
				.build();
	}

}
