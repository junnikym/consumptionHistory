package name.junnikym.consumptionHistory.member.domain;

import lombok.*;
import name.junnikym.consumptionHistory.history.domain.History;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter @Builder
public class Member implements Serializable {

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "uuid2")
	@Column(columnDefinition = "BINARY(16)")
	private UUID id;

	@Column(unique = true)
	private String email;

	private String password;

	@CreationTimestamp
	private Timestamp createAt;

	@UpdateTimestamp
	private Timestamp updateAt;



	@OneToMany(mappedBy = "writer")
	private List<History> histories;


	/**
	 * 비밀번호를 암호화하는 Encoder
	 *
	 * @param passwordEncoder : SpringSecurity 의 PasswordEncoder
	 * @return this
	 */
	public Member encryptPassword(PasswordEncoder passwordEncoder) {
		this.password = passwordEncoder.encode(this.password);
		return this;
	}

}
