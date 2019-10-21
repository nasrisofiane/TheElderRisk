package com.adrar.sicknesstorm;

import java.util.*;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;


@Entity
@Table(name = "players")
@JsonIgnoreProperties({"hibernateLazyInitializer"})
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Player {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name ="name")
	private String name;
	
	@JsonIgnoreProperties("territoryAdjacent")
	@OneToMany(mappedBy = "player" ,cascade = CascadeType.ALL)
	private Set<Territory> playerTerritories = new HashSet<Territory>();

	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Set<Territory> getPlayerTerritories() {
		return playerTerritories;
	}
	public void setPlayerTerritories(Set<Territory> playerTerritories) {
		this.playerTerritories = playerTerritories;
	}
	
	public void addTerritory(Territory territory) {
		playerTerritories.add(territory);
	}

	public void deleteTerritory(Territory territory) {
		playerTerritories.remove(territory);
	}
}




