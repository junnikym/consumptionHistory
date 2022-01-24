package name.junnikym.consumptionHistory.config;

import name.junnikym.consumptionHistory.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@Profile("test")
public class TestConfig {

	@Autowired
	MemberService memberService;

	@Bean
	public UserDetailsService memberService() {
		return memberService;
	}

}
