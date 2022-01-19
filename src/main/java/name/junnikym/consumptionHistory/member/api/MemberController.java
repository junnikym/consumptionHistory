package name.junnikym.consumptionHistory.member.api;

import lombok.RequiredArgsConstructor;
import name.junnikym.consumptionHistory.member.dto.MemberLoginDTO;
import name.junnikym.consumptionHistory.member.dto.MemberSignupDTO;
import name.junnikym.consumptionHistory.member.service.MemberService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/user")
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;

	@PostMapping("/signup")
	public void signup(@RequestBody MemberSignupDTO dto) {
		return;
	}

	@PostMapping("/login")
	public void login(@RequestBody MemberLoginDTO dto) {
		return;
	}

	@PostMapping("/logout")
	public void logout() {
		return;
	}

}
