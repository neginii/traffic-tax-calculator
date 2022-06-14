package congestion.calculator.repository;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import congestion.calculator.repository.tables.DailyTrafficEvents;

@Repository
public interface DailyTrafficEventsRepository extends CassandraRepository<DailyTrafficEvents, String> {
}
