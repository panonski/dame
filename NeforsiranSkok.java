/*
// 	Ova klasa nasledjuje klasu Potez i implementira metode za verziju igre Dame
// 	u kojoj su skokovi nisu forsirani, a pioni smeju da preskacu dame.
//
// 	@author Dejan Acanski
//
*/

package dame;


import java.util.Vector;


class NeforsiranSkok extends Potez{

    // metod dozvoljenHod(pozicija, x1, y1, x2, ) proverava da li je uneti hod (pomeranje figure za jedno mesto) 
    // u skladu sa pravilima igre
    // ova verzija ne obraca paznju na to da li je moguc skok nekom drugom figurom

    boolean dozvoljenHod(int[][] pozicija, int x1, int y1, int x2, int y2){

        if (naTabli(x1, y1) && naTabli(x2, y2) && (Math.abs(x1-x2) == 1) && (pozicija[x2][y2] == Dame.PRAZNO)){

            int figura = pozicija[x1][y1];			

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
        return false;

    }	// dozvoljenHod




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
                        // pocetne koordinate poteza su one na kojima se data figura trenutno nalazi
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
                                    for (int ls=0; ls<lista_skokova.size(); ls++){				
                                            lista_poteza.insertElementAt(lista_skokova.elementAt(ls),ls); 
                                    }
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
    // argument iter predstavlja broj iteracije, odnosno redni broj jednog datog skoka 
    // u jednom visestrukom skoku

    void nadjiVezaneSkokove(int[][] tabla, Vector lista_skokova, int x1, int y1, int x2, int y2, int iter){

        int[] npot = new int[4];		
        npot[0] = x1; 			
        npot[1] = y1; 
        npot[2] = x2; 
        npot[3] = y2;							
        lista_skokova.addElement(npot);

        int[][] pom_tabla = kopirajTablu(tabla);
        primeniPotez(pom_tabla, x1, y1, x2, y2);

        if (mozeDaSkoci(pom_tabla, x2, y2)){

            int novix = x2;
            int noviy = y2;

            while (novix > 7){
                novix/=10;					
                noviy/=10;							
            }		

            for(int a=novix-2; a<=novix+2; a+=4){				
                for(int b=noviy-2; b<=noviy+2; b+=4){

                    if(dozvoljenSkok(pom_tabla, novix, noviy, a, b)){
                        x2 = x2+a*iter; 
                        y2 = y2+b*iter;							
                        nadjiVezaneSkokove(pom_tabla, lista_skokova, x1, y1, x2, y2, iter*10);	                                                                                                                                                                                                                                                                                                               // a zavrsne se menjaju
                                                                                                                                                                                                                                                                                                                               // i povecava se broj iteracije	
                    } 

                }
            } 

        }

    }

	
}

