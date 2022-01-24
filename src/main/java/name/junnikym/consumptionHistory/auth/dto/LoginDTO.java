package name.junnikym.consumptionHistory.auth.dto;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;

@Data @Builder
@AllArgsConstructor
public class LoginDTO {

	@Email
	@NotNull
	private String email;

	@NotNull
	private String password;

}
