package name.junnikym.consumptionHistory.member.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.RequiredArgsConstructor;
import name.junnikym.consumptionHistory.member.domain.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@RequiredArgsConstructor
public class MemberDetailVO implements UserDetails {

	private final String email;

	@JsonIgnore
	private final String password;

//	private final Collection<? extends GrantedAuthority> authorities;



	public MemberDetailVO (Member member) {
		this.email = member.getEmail();
		this.password = member.getPassword();
	}



	@Override
	public Collection<? extends GrantedAuthority> getAuthorities () {
		return null;
	}

	@Override
	public String getUsername () {
		return this.email;
	}

	@Override
	public String getPassword () {
		return this.password;
	}

	@Override
	public boolean isAccountNonExpired () {
		return true;
	}

	@Override
	public boolean isAccountNonLocked () {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired () {
		return true;
	}

	@Override
	public boolean isEnabled () {
		return true;
	}
}
