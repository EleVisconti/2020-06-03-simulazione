package it.polito.tdp.PremierLeague.model;

import java.util.Objects;

public class GiocatoriBattuti implements Comparable<GiocatoriBattuti> {
 Player p;
 double peso;
 
 
 
public GiocatoriBattuti(Player p, double peso) {
	super();
	this.p = p;
	this.peso = peso;
}



@Override
public int hashCode() {
	return Objects.hash(p, peso);
}



@Override
public boolean equals(Object obj) {
	if (this == obj)
		return true;
	if (obj == null)
		return false;
	if (getClass() != obj.getClass())
		return false;
	GiocatoriBattuti other = (GiocatoriBattuti) obj;
	return Objects.equals(p, other.p) && Double.doubleToLongBits(peso) == Double.doubleToLongBits(other.peso);
}



public Player getP() {
	return p;
}



public void setP(Player p) {
	this.p = p;
}



public double getPeso() {
	return peso;
}



public void setPeso(double peso) {
	this.peso = peso;
}






@Override
public int compareTo(GiocatoriBattuti o) {
	if(this.getPeso()>o.getPeso())
		return -1;
	else return 1;
}
 
 
	
	
}
