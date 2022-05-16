package congestion.calculator.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import congestion.calculator.model.Tax;

@Repository
public interface CongestionTaxRepository extends JpaRepository<Tax, String> {
}
