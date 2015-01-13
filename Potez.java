/*
// 	Ova klasa racuna i proverava poteze u igri Dame.
// 	Klasa je apstraktna jer definise samo ona polja i metode koji su zajednicki za sve verzije igre.
// 
// 	Svaki potez je definisan kao uredjena cetvorka (x1, y1, x2, y2) 
//      i implementiran kao niz od cetiri elementa 											
// 	- to su pocetne i zavrsne koordinate poteza .
//
//
// 	Da bismo mogli da zabelezimo visestruke poteze (skokove), vezani niz poteza:
//		(x1, y1, x2, y2)
//		(x2, y2, x3, y3)
//		(x3, y3, x4, y4)...
//
//	se belezi kao uredjena cetvorka: 
//	(x1, y1, x2 + x3*10 + ... + x[i]*10^(i-2), y2 + y3*10 + ... + y[i]*10^(i-2)).		 
// 	
// 	primer: niz skokova
//	(1, 5, 2, 6)
//	(2, 6, 3, 5)
//	(3, 5, 4, 6)
//
//	belezi se kao
//	(1, 5, 432, 656).
//
//	@author Dejan Ačanski & Gordana Rakić
//
*/



package dame;


import java.util.Vector;
import java.io.*;


abstract class Potez {

	static final int DOVRSEN = 1;		// pravilan potez posle kojeg data figura ne moze vise da skace
	static final int NEDOVRSEN = 2; 	// pravilan potez posle kojeg data figura moze jos da skace
	static final int NEPRAVILAN = 3;


	// metod naTabli(x, y) proverava da li je dato polje u okvirima table
	
	protected boolean naTabli(int x, int y){
		return (-1<x && x<8 && -1<y && y<8);
	}


	// metod dozvoljenHod(pozicija, x1, y1, x2, y2) 
        // proverava da li je dati hod (pomeranje figure za jedno mesto u skladu sa pravilima igre	
	
	abstract boolean dozvoljenHod(int[][] pozicija, int x1, int y1, int x2, int y2);
				
	
	// metod dozvoljenSkok(pozicija, x1, y1, x2, y2) 
        // proverava da li je dati skok (preko protivnicke figure) u skladu sa pravilima igre
			
	abstract boolean dozvoljenSkok(int[][] pozicija, int x1, int y1, int x2, int y2);
	
	
	// metod proveriPotez(pozicija, x1, y1, x2, y2) ispituje da li je uneti potez u skladu sa pravilima igre 
	// on zapravo poziva dozvoljenHod ili dozvoljenSkok i javlja da li su moguci dalji potezi
	
	int proveriPotez(int[][] pozicija, int x1, int y1, int x2, int y2){
	
		if ( (!(naTabli(x1, y1) && naTabli(x2, y2))) || (!(pozicija[x2][y2] == Dame.PRAZNO)) ){
		
			return NEPRAVILAN;
			
		} else {
		
			if ((Math.abs(x1-x2) == 1) && (Math.abs(y1-y2) == 1)) { 							
			
				if(dozvoljenHod(pozicija, x1, y1, x2, y2)){	
					return DOVRSEN;
				} else {
					return NEPRAVILAN;
				}
			} else {																															// inace je skok
			
				if(dozvoljenSkok(pozicija, x1, y1, x2, y2)){
					return NEDOVRSEN;																									// mozda moze jos da skace...
				} else {
					return NEPRAVILAN;
				}
			} // if Math.abs
		} 
		
	} // proveriPotez


	// metod primeniPotez(pozicija, x1, y1, x2, y2) sprovodi dati potez na tabli (ako nije nepravilan)
	// u slucaju nedovrsenog poteza, javlja da li postoji mogucnost za dalje skokove
	
	public int primeniPotez(int[][] pozicija, int x1, int y1, int x2, int y2){
		
		int status = proveriPotez(pozicija, x1, y1, x2, y2);
		
		if (status == NEPRAVILAN){ 					
			
			return NEPRAVILAN;
			
		} else {
		   
			pozicija[x2][y2] = pozicija[x1][y1];		
			pozicija[x1][y1] = Dame.PRAZNO;	

		 	int figura = pozicija[x2][y2];
			if ((figura == Dame.BELI) && (y2 == 7)){										
				pozicija[x2][y2] = Dame.BDAMA;
			}
			if ((figura == Dame.CRNI) && (y2 == 0)){										
				pozicija[x2][y2] = Dame.CDAMA;
			}
                    
			if (Math.abs(x2-x1) == 2){																	
				
				int xp = (x1+x2)/2; 
				int yp = (y1+y2)/2;				
				
				pozicija[xp][yp] = Dame.PRAZNO;															
				
				if (status == NEDOVRSEN){
					if (mozeDaSkoci(pozicija, x2, y2)){
						return NEDOVRSEN;
					} else {
						return DOVRSEN;
					}
				}
			}	
		
			return DOVRSEN;
                }			
		
        }	 
	
	
	// metod mozeDaSeKrece(pozicija, x1, y1) 
        // proverava da li data figura moze da hoda ili skoci sa polja na kojem se nalazi

	protected boolean mozeDaSeKrece(int[][] pozicija, int x1, int y1)
	{
		return (mozeDaHoda(pozicija, x1, y1) || mozeDaSkoci(pozicija, x1, y1));
	}

	
	// metod mozeDaHoda(pozicija, x1, y1) 
        // proverava da li data figura moze da izvrsi hod u bilo kojem smeru sa svoje pozicije				
	
	boolean mozeDaHoda(int[][] pozicija, int x1, int y1){

		return ((dozvoljenHod(pozicija, x1, y1, x1+1, y1+1)) || 
                        (dozvoljenHod(pozicija, x1, y1, x1-1, y1+1)) ||
			(dozvoljenHod(pozicija, x1, y1, x1+1, y1-1)) || 
                        (dozvoljenHod(pozicija, x1, y1, x1-1, y1-1)));		
	}
	
		
	// metod mozeDaSkoci(pozicija, x1, y1) 
        // proverava da li data figura moze da izvrsi skok u bilo kojem smeru sa svoje pozicije	
	
	boolean mozeDaSkoci(int[][] pozicija, int x1, int y1) { 
				
		return ((dozvoljenSkok(pozicija, x1, y1, x1+2, y1+2)) || 
                        (dozvoljenSkok(pozicija, x1, y1, x1+2, y1-2)) ||
                        (dozvoljenSkok(pozicija, x1, y1, x1-2, y1+2)) || 
                        (dozvoljenSkok(pozicija, x1, y1, x1-2, y1-2)));
	} 


	// metod generisiPoteze(pozicija, igrac) pravi spisak svih mogucih poteza datog igraca u datoj poziciji
	// spisak se belezi kao vektor ciji su elementi uredjene cetvorke (potezi)
	
	abstract Vector generisiPoteze(int[][] pozicija, int igrac); 
		
		
	// metod odigraj(tabla, potez) sprovodi potez na tabli;
	// ako je potez sacinjen iz vise skokova
	// krecemo se kroz while petlju i sprovodimo jedan po jedan skok
	
	public void odigraj(int[][] tabla, int[] potez){
		int x1 = potez[0];
		int y1 = potez[1];
		int x2 = potez[2];
		int y2 = potez[3];
		
		while(x2>0 || y2>0){
                    primeniPotez(tabla, x1, y1, x2%10, y2%10);
                    x1 = x2%10;
                    y1 = y2%10;
                    x2 /= 10;
                    y2 /= 10;
                }
        }
	
	// pomocni metodi 
	
	int boja(int figura){		
		if((figura == Dame.BELI) || (figura == Dame.BDAMA)){
			return Dame.BELI;
		} else if ((figura == Dame.CRNI) || (figura == Dame.CDAMA)){
			return Dame.CRNI;
		}
		return Dame.PRAZNO;
	}
	
	
	protected int[][] kopirajTablu(int[][] tabla){		
		int[][] kopija = new int[8][8];
		for (int i=0; i<8; i++){
			System.arraycopy(tabla[i], 0, kopija[i], 0, 8);
		}
		return kopija;
	}
	

}