package it.polito.tdp.PremierLeague.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;


import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;


public class Model {
	
	private PremierLeagueDAO dao;
	private Graph<Player, DefaultWeightedEdge> grafo;
	private List<Player> giocatori;
	private Map<Integer, Player> giocatoriId;
	private int bestDegree;
	private ArrayList<Player> dreamTeam;
	
	public Model() {
		 dao = new PremierLeagueDAO();
		 }

	public void creaGrafo(double soglia) {
	 grafo = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
	 giocatori=new ArrayList<Player>(this.dao.getVertex(soglia));
	 giocatoriId=new HashMap<>();
	 for(Player p : giocatori) {
		 giocatoriId.put(p.getPlayerID(), p);
	 }
	 Graphs.addAllVertices(this.grafo, giocatori);
	 
	 List<Adiacenza> archi = new ArrayList<>(this.dao.getArchi(giocatoriId));
	 for(Adiacenza a : archi) {
		 if(a.getPeso()<0)
			Graphs.addEdge(this.grafo, a.p2, a.p1,Math.abs(a.getPeso()));
		 else
			 Graphs.addEdge(this.grafo, a.p1, a.p2, Math.abs(a.getPeso()));
	 }
	 
		
	}

	public List<Player> getGiocatori() {
		return giocatori;
	}

	public int nVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public void setGiocatori(List<Player> giocatori) {
		this.giocatori = giocatori;
	}

	public String trovaTop() {
		String s ="";
		int max=-1;
		Player top=null;
		List<GiocatoriBattuti> giocatoriBattuti = new ArrayList<>();
		for(Player p : this.grafo.vertexSet()){
			if (this.grafo.outDegreeOf(p)>max) {
				top=p;
				max=this.grafo.outDegreeOf(p);	
		}}
		
		s+="Top player: "+top.getName()+"\n";
		for(DefaultWeightedEdge e: this.grafo.outgoingEdgesOf(top)) {
			GiocatoriBattuti g = new GiocatoriBattuti(this.grafo.getEdgeTarget(e), this.grafo.getEdgeWeight(e));
			giocatoriBattuti.add(g);
		}
		Collections.sort(giocatoriBattuti);
		for(GiocatoriBattuti g1 : giocatoriBattuti) {
			s+="\n"+g1.getP().getName()+" "+g1.getPeso();
		}
		return s;
	}

	
	//ricorsione

	/*public String calcolaInsieme(int n){
		String s ="";
	    best = new LinkedList<Player>();
		List<Player> parziale = new LinkedList<>();
		allPlayers = new LinkedList<>(this.giocatori);
		cerca(allPlayers, parziale, n);
		s+="\n grado di titolarità: "+calcolaPeso(best);
		for(Player p : best) {
			s+="\n"+p.getName();
		}
		return s;
	}

	
	//memoria è la memoria attualmente occupata, m è la memoria massima
	private void cerca(List<Player> allPlayers, List<Player> parziale, int n) {
		//condizione di terminazione, controllo se è la soluzione migliore
		if(calcolaPeso(parziale) > calcolaPeso(best)) {
			best = new LinkedList<>(parziale);
		}
		
		if(parziale.size()==n) {
			return;
		}
	
		for(Player p1 : allPlayers) {
			if(!parziale.contains(p1)) {
				List<Player> daRimuovere = new ArrayList<Player>();
				parziale.add(p1);
				for(DefaultWeightedEdge e1: this.grafo.outgoingEdgesOf(p1)) {
				 Player p2 = this.grafo.getEdgeTarget(e1);
				 daRimuovere.add(p2);
				}
				for(Player p3 : daRimuovere)
				 allPlayers.remove(p3);
				cerca(parziale, allPlayers, n); //aggiungo alla ricorsione
				parziale.remove(parziale.size()-1); //tolgo
			}
		}
	}

	private double calcolaPeso(List<Player> parziale) {
		double grado = 0;
		double gradoTot =0;
		for(Player p : parziale) {
			for(DefaultWeightedEdge e : this.grafo.outgoingEdgesOf(p)) {
				grado+=this.grafo.getEdgeWeight(e);
			}	
			for(DefaultWeightedEdge e : this.grafo.incomingEdgesOf(p)) {
				grado-=this.grafo.getEdgeWeight(e);
			}
			gradoTot+=grado;
		}
			
		return gradoTot;
	}
*/	
	public String getDreamTeam(int k){
		String s="";
		this.bestDegree = 0;
		this.dreamTeam = new ArrayList<Player>();
		List<Player> partial = new ArrayList<Player>();
		
		this.recursive(partial, new ArrayList<Player>(this.grafo.vertexSet()), k);

		for(Player p : dreamTeam) {
			s+="\n"+p.getName();
		}
		return s;
	}
	
	public void recursive(List<Player> partial, List<Player> players, int k) {
		if(partial.size() == k) {
			int degree = this.getDegree(partial);
			if(degree > this.bestDegree) {
				dreamTeam = new ArrayList<>(partial);
				bestDegree = degree;
			}
			return;
		}
		
		for(Player p : players) {
			if(!partial.contains(p)) {
				partial.add(p);
				//i "battuti" di p non possono più essere considerati
				List<Player> remainingPlayers = new ArrayList<>(players);
				remainingPlayers.removeAll(Graphs.successorListOf(grafo, p));
				recursive(partial, remainingPlayers, k);
				partial.remove(p);
				
			}
		}
	}
	
	private int getDegree(List<Player> team) {
		int degree = 0;
		int in;
		int out;

		for(Player p : team) {
			in = 0;
			out = 0;
			for(DefaultWeightedEdge edge : this.grafo.incomingEdgesOf(p))
				in += (int) this.grafo.getEdgeWeight(edge);
			
			for(DefaultWeightedEdge edge : grafo.outgoingEdgesOf(p))
				out += (int) grafo.getEdgeWeight(edge);
		
			degree += (out-in);
		}
		return degree;
	}

	public Integer getBestDegree() {
		return bestDegree;
	}
	
}
