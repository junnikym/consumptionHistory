package name.junnikym.consumptionHistory.member.service;

import lombok.RequiredArgsConstructor;
import name.junnikym.consumptionHistory.member.domain.Member;
import name.junnikym.consumptionHistory.member.dto.MemberSignupDTO;
import name.junnikym.consumptionHistory.member.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

	private final MemberRepository memberRepository;

	private final PasswordEncoder passwordEncoder;

	public void signup(MemberSignupDTO dto) {
		return;
	}

	public void login() {
		return;
	}

	public void logout() {
		return;
	}

}
