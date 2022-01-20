package name.junnikym.consumptionHistory.auth.service;

import lombok.RequiredArgsConstructor;
import name.junnikym.consumptionHistory.auth.dto.SignupDTO;
import name.junnikym.consumptionHistory.auth.dto.LoginDTO;
import name.junnikym.consumptionHistory.auth.provider.JwtTokenProvider;
import name.junnikym.consumptionHistory.exception.AlreadyExistsException;
import name.junnikym.consumptionHistory.member.domain.Member;
import name.junnikym.consumptionHistory.member.repository.MemberRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

	private final MemberRepository memberRepository;

	private final JwtTokenProvider tokenProvider;

	private final AuthenticationManager authenticationManager;

	private final PasswordEncoder passwordEncoder;

	/**
	 * 로그인 기능
	 *
	 * @param dto : Email, Password
	 * @return 생성된 JWT
	 */
	public String login(LoginDTO dto) {
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		return tokenProvider.generateToken(authentication);
	}

	/**
	 * 회원가입 기능 / Email 이 이미 등록되 있을 시 예외처리
	 *
	 * @param dto : 회원정보
	 */
	public void signup(SignupDTO dto) {
		if (memberRepository.existsByEmail(dto.getEmail()))
			throw new AlreadyExistsException("Email");

		Member entity = dto.toEntity().encryptPassword(passwordEncoder);
		memberRepository.save(entity);
	}

}
