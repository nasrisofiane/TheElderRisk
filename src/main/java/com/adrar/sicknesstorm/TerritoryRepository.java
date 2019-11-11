package com.adrar.sicknesstorm;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TerritoryRepository extends JpaRepository<Territory, Integer> {
	
	@Query(value = "SELECT * FROM territories WHERE player_id = :playerid", nativeQuery = true)
	public Set<Territory> findTerritoryByPlayerId(@Param("playerid") Integer playerId);
}
