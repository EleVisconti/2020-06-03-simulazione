package it.polito.tdp.PremierLeague.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.PremierLeague.model.Action;
import it.polito.tdp.PremierLeague.model.Adiacenza;
import it.polito.tdp.PremierLeague.model.Player;

public class PremierLeagueDAO {
	
/*	public List<Player> listAllPlayers(){
		String sql = "SELECT * FROM Players";
		List<Player> result = new ArrayList<Player>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Player player = new Player(res.getInt("PlayerID"), res.getString("Name"));
				
				result.add(player);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}*/
	
	public List<Action> listAllActions(){
		String sql = "SELECT * FROM Actions";
		List<Action> result = new ArrayList<Action>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Action action = new Action(res.getInt("PlayerID"),res.getInt("MatchID"),res.getInt("TeamID"),res.getInt("Starts"),res.getInt("Goals"),
						res.getInt("TimePlayed"),res.getInt("RedCards"),res.getInt("YellowCards"),res.getInt("TotalSuccessfulPassesAll"),res.getInt("totalUnsuccessfulPassesAll"),
						res.getInt("Assists"),res.getInt("TotalFoulsConceded"),res.getInt("Offsides"));
				
				result.add(action);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Player> getVertex(Double soglia){
		
		 String sql="SELECT a.PlayerID, a.TeamID, AVG(a.goals) as peso, a.MatchID, p.Name "
		 		+ "FROM actions a, players p "
		 		+ "WHERE a.PlayerID=p.playerID "
		 		+ "GROUP BY a.PlayerID "
		 		+ "HAVING AVG(a.goals)>? ";
		List<Player> result = new ArrayList<Player>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setDouble(1, soglia);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				Player p = new Player(res.getInt("a.PlayerID"), res.getString("p.Name"), res.getInt("a.TeamID"), res.getDouble("peso"));
				  result.add(p);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Adiacenza> getArchi(Map<Integer, Player> giocatoriId){
		
		 String sql="SELECT A1.PlayerID AS p1, A2.PlayerID AS p2, (SUM(A1.TimePlayed) - SUM(A2.TimePlayed)) AS peso " + 
					"FROM 	Actions A1, Actions A2 " + 
					"WHERE A1.TeamID != A2.TeamID " + 
					"	AND A1.MatchID = A2.MatchID " + 
					"	AND A1.starts = 1 AND A2.starts = 1 " + 
					"	AND A1.PlayerID > A2.PlayerID " + 
					"GROUP BY A1.PlayerID,A2.PlayerID";
		List<Adiacenza> result = new ArrayList<Adiacenza>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
			 Player p1 = giocatoriId.get(res.getInt("p1"));
			 Player p2 = giocatoriId.get(res.getInt("p2"));
			 double peso = res.getDouble("peso");
			  if(p1!=null&&p2!=null&&peso!=0) {
				 if(!p1.equals(p2)) {
				  Adiacenza a = new Adiacenza(p1, p2, peso);
				  result.add(a);}
			  }
			 
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	


}
