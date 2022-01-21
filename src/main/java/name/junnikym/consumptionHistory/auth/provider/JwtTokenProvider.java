package name.junnikym.consumptionHistory.auth.provider;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import name.junnikym.consumptionHistory.member.domain.Member;
import name.junnikym.consumptionHistory.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

	@Value("${spring.jwt.secret}")
	private String secretKey;

	@Value("${spring.jwt.token-expiration-period}")
	private long tokenExpirationPeriod;

	@Autowired
	private final MemberService memberService;

	/**
	 * JWT 생성
	 *
	 * @param authentication : 로그인 인증된 객체
	 * @return 생성된 JWT
	 */
	public String generateToken(Authentication authentication) {
		Member detail = (Member) authentication.getPrincipal();

		return Jwts.builder()
				.setSubject(detail.getUsername())
				.setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + tokenExpirationPeriod))
				.signWith(SignatureAlgorithm.HS256, secretKey)
				.compact();
	}

	/**
	 * JWT 에 담긴 사용자 정보를 Authentication 객체로 반환
	 *
	 * @param token : 사용자의 JWT
	 * @return Authentication 객체
	 */
	public Authentication getAuthentication(String token) {
		UserDetails userDetails = memberService.loadUserByUsername( this.getUsernameFromToken(token) );

		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}

	/**
	 * JWT 에 담긴 Username (Email) 을 반환
	 *
	 * @param token : 사용자의 JWT
	 * @return Username (Email)
	 */
	public String getUsernameFromToken(String token) {
		return Jwts.parser()
				.setSigningKey(secretKey)
				.parseClaimsJws(token)
				.getBody()
				.getSubject();
	}

	/**
	 * Request Header 에 명시되있는 JWT를 반환
	 *
	 * @param request : HTTP Request
	 * @return JWT
	 */
	public String getTokenFromRequest(HttpServletRequest request) {
		String header = request.getHeader("Authorization");
		if (StringUtils.hasText(header) && header.startsWith("Bearer "))
			return header.substring(7);	// Remove "Bearer " -> Only JWT

		return null;
	}

	/**
	 * 인자로 들어온 토큰이 유효 여부 반환
	 *
	 * @param token : 사용자 JWT
	 * @return 유효 여부
	 */
	public boolean validateToken(String token) {
		try {
			Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
