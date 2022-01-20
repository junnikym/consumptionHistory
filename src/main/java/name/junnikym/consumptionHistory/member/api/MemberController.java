package name.junnikym.consumptionHistory.member.api;

import lombok.RequiredArgsConstructor;
import name.junnikym.consumptionHistory.member.service.MemberService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/user", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class MemberController {

	private final MemberService memberService;

}
