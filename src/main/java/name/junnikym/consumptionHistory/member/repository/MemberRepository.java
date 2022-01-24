package name.junnikym.consumptionHistory.member.repository;

import name.junnikym.consumptionHistory.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface MemberRepository extends JpaRepository<Member, UUID> {

	boolean existsByEmail(String email);

	Optional<Member> findByEmail(String email);

}

