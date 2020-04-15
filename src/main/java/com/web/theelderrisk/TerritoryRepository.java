package com.web.theelderrisk;
import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TerritoryRepository extends JpaRepository<Territory, Integer> {

    @Query(value = "SELECT * FROM territories_adjacent WHERE territory_a = ?1 OR territory_b = ?1", nativeQuery = true)
    public ArrayList<ArrayList<Integer>> getAdjacentsTerritories(int id);
    
    // @Query(value = "SELECT (CASE WHEN (territory_a = ?1) THEN territory_b ELSE territory_a END) FROM territories_adjacent WHERE territory_a = ?1 OR territory_b = ?1", nativeQuery = true)
    // public ArrayList<Integer> getAdjacentsTerritoriesNew(int id);
    
}
