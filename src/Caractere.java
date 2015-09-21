
public class Caractere {
	//Variables
	private int nom;
	private int nbr = 1;
	
	//Constructeur en passant un int en paramètre
	public Caractere(int nom){
		this.nom = nom;
	}
	
	//getter
	public int getNom(){
		return this.nom;
	}
	public int getNbr(){
		return this.nbr;
	}
	
	//Incrémenter le nombre de fois qu'un int est présent.
	public void incrementerNbr(){
		this.nbr++;
	}
	
	public void setNbr(int nbr) {
		this.nbr = nbr;
	}
}
