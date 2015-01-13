/*
// 	Klasa ForsiranSkok nasledjuje klasu Potez i implementira metode za verziju igre Dame
// 	u kojoj su skokovi forsirani (tj. ako postoji mogucnost za skok, on se mora odigrati), a pioni smeju da preskacu dame.
//
// 	@author Dejan Acanski
//
*/



package dame;


import java.util.Vector;


public class ForsiranSkok extends Potez{

	// metod dozvoljenHod(pozicija, x1, y1, x2, y2) proverava da li je uneti hod 
        // (pomeranje figure za jedno mesto) u skladu sa pravilima igre
	// ova verzija proverava i da li je moguc skok nekom drugom figurom, jer tada skok ima prioritet
		
    boolean dozvoljenHod(int[][] pozicija, int x1, int y1, int x2, int y2){
		
        if (naTabli(x1, y1) && naTabli(x2, y2) && (Math.abs(x1-x2) == 1) && (pozicija[x2][y2] == Dame.PRAZNO)){
            int figura = pozicija[x1][y1];
            if (biloKoMozeDaSkoci(pozicija, figura)){		// skok ima prioritet!
                    return false;
            } else {
                   switch (figura){
                        case Dame.BELI:					
                                if (y2-y1 == 1) return true;
                                break;

                        case Dame.CRNI:	
                                if (y2-y1 == -1) return true;						
                                break;
                            
                        case Dame.BDAMA:
                        case Dame.CDAMA:
                                if (Math.abs(y2-y1) == 1) return true;													
                    }	// switch				
            }
        }
        return false;						
    }
		
    // metod dozvoljenSkok(pozicija, x1, y1, x2, y2) proverava da li je dati skok u skladu sa pravilima igre
	
    protected boolean dozvoljenSkok(int[][] pozicija, int x1, int y1, int x2, int y2){

    if (naTabli(x1, y1) && naTabli(x2, y2) && (Math.abs(x1-x2) == 2) && (pozicija[x2][y2] == Dame.PRAZNO)){		

        int figura = pozicija[x1][y1];				
        int protivnik = pozicija[(x1+x2)/2][(y1+y2)/2];

        switch(figura) {

            case Dame.BELI :		
                    if ((y2-y1 == 2) && (boja(protivnik) == Dame.CRNI)) return true;
                    break;

            case Dame.CRNI :
                    if ((y2-y1 == -2) && (boja(protivnik) == Dame.BELI)) return true;
                    break;

            case Dame.BDAMA :
                    if ((Math.abs(y1-y2) == 2) && (boja(protivnik) == Dame.CRNI)) return true;
                    break;

            case Dame.CDAMA :
                    if ((Math.abs(y1-y2) == 2) && (boja(protivnik) == Dame.BELI)) return true;				
                    break;
        }
    }	
    return false;
    }
		
		
	// metod biloKoMozeDaSkoci(pozicija, figura) proverava 
        // da li postoji figura iste boje koja moze da skoci
		
	boolean biloKoMozeDaSkoci(int[][] pozicija, int figura) {		
	
            boolean nasao = false;
            int i = 0;

            while((i<8) && !nasao){
                    int j = 0;
                    while((j<8) && !nasao){						
                            if(((i+j) % 2) == 1){ 													// proveravamo samo neparna polja							
                                    if(boja(pozicija[i][j]) == boja(figura)) 
                                            nasao = mozeDaSkoci(pozicija, i, j);							
                            }
                            j++;
                    } // while j
                    i++;
            } // while i
            return nasao;
	}
	
				
	
	// metod generisiPoteze(pozicija, igrac) pravi spisak svih mogucih poteza datog igraca u datoj poziciji
	// spisak se belezi kao vektor ciji su elementi uredjene cetvorke (potezi)
			
    Vector generisiPoteze(int[][] tabla, int igrac)	{

        int[][] pom_tabla;
        Vector lista_poteza = new Vector();			

        for (int i=0; i<8; i++){
            for (int j=0; j<8; j++){
                if (((i+j) % 2) == 1){										
                    int figura = tabla[i][j];
                    if (boja(figura) == boja(igrac)){
                        int[] potez = new int[4];
                        potez[0] = i; potez[1] = j;	

                        for(int p=i-1; p<=i+1; p+=2){		
                            for(int q=j-1; q<=j+1; q+=2){
                                if (dozvoljenHod(tabla, i, j, p, q)) {
                                    potez[2] = p; potez[3] = q;
                                    lista_poteza.addElement(potez);									
                                }
                            }
                        }		

                        for(int p=i-2; p<=i+2; p+=4){																
                            for(int q=j-2; q<=j+2; q+=4){
                                if (dozvoljenSkok(tabla, i, j, p, q)) {																						
                                    Vector lista_skokova = new Vector();									
                                    int iter = 10;
                                    nadjiVezaneSkokove(tabla, lista_skokova, i, j, p, q, iter);
                                    for (int ls=0; ls<lista_skokova.size(); ls++){				// kopiraj vektor sa vezanim skokovima u listu poteza
                                        lista_poteza.insertElementAt(lista_skokova.elementAt(ls),ls); // skokove stavljamo na pocetak liste
                                    }																																// jer su mozda najbolji											
                                }
                            }
                        }	

                    }
                }
            } 
        }	

        return lista_poteza;

    }  // generisiPoteze


	
	// metod nadjiVezaneSkokove(tabla, lista_skokova, x1, y1, x2, y2, iter) je rekurzivni metod 
	// koji pronalazi sve visestruke skokove koje data figura moze izvesti
	// argumenti x1, y1, x2 i y2 predstavljaju pocetne i krajnje koordinate skoka
	// argument iter predstavlja broj iteracije, odnosno redni broj jednog datog skoka u jednom visestrukom skoku
	
    void nadjiVezaneSkokove(int[][] tabla, Vector lista_skokova, int x1, int y1, int x2, int y2, int iter){
		
        int[][] pom_tabla = kopirajTablu(tabla);
        primeniPotez(pom_tabla, x1, y1, x2, y2);

        if (mozeDaSkoci(pom_tabla, x2, y2)){
        
            // pocetne koordinate za novi skok su zapravo krajnje koordinate prethodnog
            int novix = x2;	
            int noviy = y2;
            // zbog zapisa visestrukih skokova, moraju se prvo svesti na "normalne" koordinate
            while (novix > 7){
                novix/=10;								
                noviy/=10;
            }		
            // proveri sva cetiri smera za dalje skokove
            for(int a=novix-2; a<=novix+2; a+=4){
                for(int b=noviy-2; b<=noviy+2; b+=4){

                    if(dozvoljenSkok(pom_tabla, novix, noviy, a, b)){
                        x2 = x2+a*iter; 
                        y2 = y2+b*iter;					
            // prvobitne pocetne koordinate skoka ostaju iste     
            // a zavrsne se menjaju;                    
                        nadjiVezaneSkokove(pom_tabla, lista_skokova, x1, y1, x2, y2, iter*10);	                                                                                                                                                                                                                                                                                                                  
                    } 

                }
            } 

        } else {													
            int[] npot = new int[4];			
            npot[0] = x1; 
            npot[1] = y1; 
            npot[2] = x2; 
            npot[3] = y2;							
            lista_skokova.addElement(npot);
        }

    }
		
}

