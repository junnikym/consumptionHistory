package name.junnikym.consumptionHistory.history.repository;

import name.junnikym.consumptionHistory.history.domain.History;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryRepository extends JpaRepository<History, Long> {

}
