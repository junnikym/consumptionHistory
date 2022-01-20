package name.junnikym.consumptionHistory.member.service;

import lombok.RequiredArgsConstructor;
import name.junnikym.consumptionHistory.exception.NotFoundException;
import name.junnikym.consumptionHistory.member.domain.Member;
import name.junnikym.consumptionHistory.member.repository.MemberRepository;
import name.junnikym.consumptionHistory.member.vo.MemberDetailVO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService implements UserDetailsService {

	private final MemberRepository memberRepository;

	/**
	 * Spring Security 의 Username 역할에 해당하는 필드(Email)에 해당하는 Memeber 객체 반환
	 *
	 * @param email : email
	 * @return Email에 해당하는 Member 객체
	 */
	@Override
	public UserDetails loadUserByUsername (String email) {
		Member member = memberRepository.findByEmail(email)
				.orElseThrow(() -> new NotFoundException(email));

		return new MemberDetailVO (member);
	}

}
