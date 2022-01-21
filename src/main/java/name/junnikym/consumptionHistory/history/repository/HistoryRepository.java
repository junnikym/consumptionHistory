package name.junnikym.consumptionHistory.history.repository;

import name.junnikym.consumptionHistory.history.domain.History;
import name.junnikym.consumptionHistory.history.dto.HistoryDetailDTO;
import name.junnikym.consumptionHistory.history.dto.HistorySummaryDTO;
import name.junnikym.consumptionHistory.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HistoryRepository extends JpaRepository<History, UUID> {

	Optional<List<History>> findByWriter(Member writer);

	Optional<List<HistorySummaryDTO>> getHistorySummaryDTOByWriterAndIsDeleted(Member writer, Boolean isDeleted);

	Optional<HistoryDetailDTO> getHistoryDetailDTOByWriterAndId(Member writer, UUID id);

	Optional<History> findByWriterAndId(Member writer, UUID id);

	@Modifying
	@Query("UPDATE History h SET h.isDeleted = :isDeleted WHERE h.id = :id AND h.writer = :writer")
	Integer deleteOrRecoverHistory(@Param("writer") Member writer, @Param("id") UUID id, @Param("isDeleted") Boolean isDeleted);

	@Modifying
	@Query("UPDATE History h SET h.isDeleted = :isDeleted WHERE h.id IN :ids AND h.writer = :writer")
	Integer deleteOrRecoverHistories(@Param("writer") Member writer, @Param("ids") Collection<UUID> ids, @Param("isDeleted") Boolean isDeleted);

}
