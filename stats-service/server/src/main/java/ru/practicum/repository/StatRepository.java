package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStats;

import java.sql.Timestamp;
import java.util.List;

public interface StatRepository extends JpaRepository<EndpointHit, Long> {

    @Query("SELECT NEW ru.practicum.model.ViewStats(e.app, e.uri, COUNT(e.uri)) " +
            "FROM EndpointHit e " +
            "WHERE e.timestamp BETWEEN ?1 AND ?2 " +
            "AND (e.uri IN ?3 OR ?3 IS NULL) " +
            "GROUP BY e.app, e.uri " +
            "ORDER BY COUNT(e.uri) DESC")
    List<ViewStats> getViewStatsWithUris(Timestamp start, Timestamp end, List<String> uris);

    @Query("SELECT NEW ru.practicum.model.ViewStats(e.app, e.uri, COUNT(DISTINCT e.ip)) " +
            "FROM EndpointHit e " +
            "WHERE e.timestamp BETWEEN ?1 AND ?2 " +
            "AND (e.uri IN ?3 OR ?3 IS NULL) " +
            "GROUP BY e.app, e.uri " +
            "ORDER BY COUNT(DISTINCT e.ip) DESC")
    List<ViewStats> getViewStatsWithUrisAndIp(Timestamp start, Timestamp end, List<String> uris);
}
