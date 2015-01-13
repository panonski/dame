/*
//
// 	Ova klasa nasledjuje klasu ForsiranSkok i redefinise metod dozvoljenSkok 
//	tako da pioni ne smeju da preskacu dame.
//
// 	@author Dejan Acanski
*/



package dame;


class ForsiranSkokBezDama extends ForsiranSkok{


    protected boolean dozvoljenSkok(int[][] pozicija, int x1, int y1, int x2, int y2){

        if (naTabli(x1, y1) && naTabli(x2, y2) && (Math.abs(x1-x2) == 2) && (pozicija[x2][y2] == Dame.PRAZNO)){		

            int figura = pozicija[x1][y1];				
            int protivnik = pozicija[(x1+x2)/2][(y1+y2)/2];

            switch(figura) {

                case Dame.BELI :		
                        if ((y2-y1 == 2) && (protivnik == Dame.CRNI)) return true;
                        break;

                case Dame.CRNI :
                        if ((y2-y1 == -2) && (protivnik == Dame.BELI)) return true;
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

} // class ForsiranSkokBezDama

