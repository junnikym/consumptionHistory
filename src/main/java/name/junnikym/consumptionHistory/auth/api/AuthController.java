package name.junnikym.consumptionHistory.auth.api;

import lombok.RequiredArgsConstructor;
import name.junnikym.consumptionHistory.auth.dto.SignupDTO;
import name.junnikym.consumptionHistory.auth.dto.LoginDTO;
import name.junnikym.consumptionHistory.auth.service.AuthService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	/**
	 * 회원가입 기능
	 *
	 * @param dto : 회원정보
	 * @throws Exception : 인자로 들어온 Email이 이미 존재 시 예외처리
	 */
	@PostMapping("/signup")
	public void signup(@RequestBody SignupDTO dto) throws Exception {
		authService.signup(dto);
	}

	/**
	 * 로그인 기능
	 *
	 * @param dto : Email, Password
	 * @return
	 */
	@PostMapping("/login")
	public String login(@RequestBody LoginDTO dto) {
		return authService.login(dto);
	}

	/**
	 * 로그아웃 기능
	 *
	 */
	@PostMapping("/logout")
	public void logout() {
		return;
	}

}
