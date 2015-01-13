/*
//      Engine za igru dama (checkers);
//      Dame su igra koja se igra na šahovskoj tabli, a koriste se samo pioni i dame.
//      Cilj igre je ukloniti sve figure protivnika ili dovesti protivnika u situaciju
//      da nema šta da odigra.
//      Figure mogu da se kreću samo po belim poljima, po jedno polje ukoso napred. 
//      Ako se ispred figure nalazi protivnička figura, a iza te protivničke figure slobodno polje,
//      možemo "pojesti" protivničku figuru. Pion koji stigne do protivničkog kraja table,
//      postaje dama i dobija mogućnost da se kreće i unazad.
//      Igru mogu igrati dva ljudska igrača, ili čovek protiv računara.
//
//	@author Dejan Ačanski & Gordana Rakić
*/
package dame;

import java.util.Vector;
 
class Mashina 
							 
								
{
	final static int BESKONACNO = Integer.MAX_VALUE;
	final static int PION = 200;
	final static int POLJE = 1;             // jedno polje po y-osi;
	final static int DAMA = 400;
	final static int IVICA = 10; 		// kraljica koja stoji na ivici je manje pokretljiva;
	final static int SLUCAJAN = 5;          // za izbor izmedju dve situacije sa istom ocenom 
											  					// - da ne bi svaki put izabrao istu
	
	final static int KRAJNJA_DUBINA = 8;	// dubina do koje se ispituju moguci potezi
	
	Potez potez;
	Vector listaPoteza = new Vector(); 	
	// vektor sastavljen od jednodimenzionalnih nizova kojima se beleze potezi;
	
	Mashina(Potez potez){
		this.potez = potez;
	}
	
	
	int ocenjivanje(int[][] pozicija){
	
        int ocena = 0;

        for (int i=0; i<8; i++){
            for (int j=0; j<8; j++){
                if(((i+j) % 2) == 1){

                    int figura = pozicija[i][j];	

                    switch (figura){

                        case Dame.BELI : 
                            ocena-=PION;
                            ocena-=POLJE*j*j;
                            break;

                        case Dame.CRNI : 
                            ocena+=PION;
                            ocena+=POLJE*(7-j)*(7-j);
                            break;

                        case Dame.BDAMA : 
                            ocena-=DAMA;	
                            if ((i==0) || (i==7))
                                    ocena+=IVICA;
                            if ((j==0) || (j==7)) 
                                    ocena+=IVICA; 
                            break;

                        case Dame.CDAMA : 
                            ocena+=DAMA; 
                            if ((i==0) || (i==7))
                                    ocena-=IVICA; 																				
                            if ((j==0) || (j==7)) 
                                    ocena-=IVICA; 												
                            break;
                    }

                }
            } 
        }
		
		ocena+= (int)(Math.random() * SLUCAJAN);
		return ocena;
	}
	
	int protivnik(int igrac){
		return igrac==Dame.BELI ? Dame.CRNI : Dame.BELI;
	}
	
	int pocetnaVrednost(int igrac){
		return igrac==Dame.BELI ? BESKONACNO : -BESKONACNO;
	}
	
	// metod MiniMax je implementacija standardnog istoimenog algoritma sa alfa-beta odsecanjem
	// beli minimizira ocenu, crni maksimizira	
	
	int MiniMax(int[][] pozicija, int dubina, int[] potez, int naPotezu, int alfa, int beta) {

            int ocena = 0;
            int[] naj_potez = new int[4];
            int naj_ocena;
		
            Vector lista_poteza = new  Vector();
		
            if (dubina == KRAJNJA_DUBINA) {
		naj_ocena = ocenjivanje(pozicija);		
				
            } else {
		naj_ocena = pocetnaVrednost(naPotezu);			
                // pravimo spisak svih mogucih poteza u datoj poziciji
		lista_poteza = this.potez.generisiPoteze(pozicija, naPotezu);	
				
		if (lista_poteza.isEmpty()){
                    return naj_ocena;				
		    		
		} else {
                    for (int i=0; i<lista_poteza.size(); i++){								
                        int[][] pom_tabla = this.potez.kopirajTablu(pozicija);	
                        this.potez.odigraj(pom_tabla, (int[]) lista_poteza.elementAt(i));

                        ocena = MiniMax(pom_tabla, dubina+1, potez, protivnik(naPotezu), alfa, beta);

                        if ((naPotezu==Dame.CRNI) && (ocena >= naj_ocena)) {		
                            naj_potez = (int[]) lista_poteza.elementAt(i);
                            naj_ocena = ocena;
                            if (naj_ocena >= beta) break;													// ako nam ova grana u stablu pretrazivanja ne nudi bolji rezultat
                                                                                                                                                                                                                                                                    // odustajemo od nje
                            if (naj_ocena > alfa) alfa = naj_ocena;

                            } else if ((naPotezu==Dame.BELI) && (ocena <= naj_ocena)) {
                                    naj_potez = (int[]) lista_poteza.elementAt(i);
                                    naj_ocena = ocena;
                                    if (naj_ocena <= alfa) break;							
                                        if (naj_ocena < beta) beta = naj_ocena;

                            }
                        }

			potez[0] = naj_potez[0];	// pamtimo najbolji potez
			potez[1] = naj_potez[1];
			potez[2] = naj_potez[2];
			potez[3] = naj_potez[3];					
		}
            }
            return naj_ocena;
	} //end minimax
	
}

