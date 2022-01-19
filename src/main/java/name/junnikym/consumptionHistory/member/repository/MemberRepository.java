package name.junnikym.consumptionHistory.member.repository;

import name.junnikym.consumptionHistory.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MemberRepository extends JpaRepository<Member, UUID> {

}

