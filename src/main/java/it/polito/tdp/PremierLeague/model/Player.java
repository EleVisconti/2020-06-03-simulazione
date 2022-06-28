package it.polito.tdp.PremierLeague.model;

public class Player {
	Integer playerID;
	String name;
	Integer MatchID;
	Double avg;
	

	
	public Integer getMatchID() {
		return MatchID;
	}
	public void setMatchID(Integer matchID) {
		MatchID = matchID;
	}
	public Double getAvg() {
		return avg;
	}
	public void setAvg(Double avg) {
		this.avg = avg;
	}
	public Player(Integer playerID, String name, Integer matchID, Double avg) {
		super();
		this.playerID = playerID;
		this.name = name;
		MatchID = matchID;
		this.avg = avg;
	}
	public Integer getPlayerID() {
		return playerID;
	}
	public void setPlayerID(Integer playerID) {
		this.playerID = playerID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((playerID == null) ? 0 : playerID.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		if (playerID == null) {
			if (other.playerID != null)
				return false;
		} else if (!playerID.equals(other.playerID))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return playerID + " - " + name;
	}
	
	
	
}
