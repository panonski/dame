
/**************************************************************************/
//                                                                         /	
//    Klasa koja implementira samu tablu na kojoj se igra izvrasava        /
//                                                                         /	
/**************************************************************************/

package dame;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

class Tabla extends JApplet implements MouseListener
{
	boolean nedovrsen ;
	boolean oznaceno;											 
	private int pocetak_i,pocetak_j,kraj_i,kraj_j;
	Dame igra;
	Potez potez;
	Image slika;
	Graphics g;
	Color tamnoZeleno = new Color(0,90,45);
	Color svetloSivo  = new Color(150,150,150);
	boolean izvrsavaj;
	int i; 
	int j;
	
	//konstruktor
	//inicijalizuje igru sa odgovarajucim (inicijalizovanim) opcijama 
	Tabla (Dame dame, Potez p) {
		this.igra = dame;
		this.potez = p;
		this.addMouseListener(this);			
		this.start();
	}

	public void destroy() {
		removeMouseListener(this);
		this.stop();
	}
	
	public void mouseReleased(MouseEvent e){}

	public void mousePressed(MouseEvent e){}
	
	public void mouseEntered(MouseEvent e){}
	
	public void mouseExited(MouseEvent e){}
	
	//sta se desava kada negde u tabli kliknemo
	
	public void mouseClicked(MouseEvent e){
	
		int x = e.getX();
		int y = e.getY();
		
		int i = x / 40;
		int j = y / 40;

        //proverimo da li smo kliknuli na onoga ko je na redu da igra
		if ((this.igra.naPotezu == Dame.BELI && 
                    (this.igra.pozicija[i][j] == Dame.BELI || this.igra.pozicija[i][j] == Dame.BDAMA))
			 ||
                    (this.igra.naPotezu == Dame.CRNI &&
                    (this.igra.pozicija[i][j] == Dame.CRNI || this.igra.pozicija[i][j] == Dame.CDAMA))){
		
		//oznacavamo polazno polje za potez
		
		//oznacimo polje ako vec nije oznaceno neko drugo	
                    if ((!nedovrsen)&&(!oznaceno)) {
                        oznaceno = true;
                        nedovrsen = false;
                        pocetak_i = i;
                        pocetak_j = j;
                        repaint();
                    }
		//ili ako je neko polje vec oznaceno vrsimo novu selekciju			
                    else if ((oznaceno)&&(!nedovrsen)) {
                        pocetak_i = i;
                        pocetak_j = j;
                        repaint();
                    }
                }
		
		//oznacavamo ciljno polje poteza i odigravamo
		else if (oznaceno) {
                    kraj_i = i;
                    kraj_j = j;
                    int status = this.potez.primeniPotez(igra.pozicija,pocetak_i,pocetak_j,kraj_i,kraj_j);
				
		//vodimo racuna da li sa te pozicije potez moze jos da se nastavi
		//tj. ako smo "jeli" figuru da li mozemo jos da "jedemo"
				
                    switch (status) {
                        case Potez.DOVRSEN:
                            nedovrsen = false;
                            oznaceno = false;
                            this.igra.promeniNaPotezu(igra.naPotezu);
                        break;
				
                        case Potez.NEDOVRSEN:
                            nedovrsen = true;
                            oznaceno = true;
                            pocetak_i = i;
                            pocetak_j = j;
                        break;
                    }
			
                //iscrtavamo novo stanje na tabli
                    repaint();
		}
	} 

	public void start() {
            izvrsavaj = true;
	}

	public void stop() {
            izvrsavaj = false;
	}

        //metod reimplementira metod paint() nasledjene klase JApplet
	public void paint(Graphics g) {
	//pre iscrtavanja "postavimo" sliku
            offscreen_paint(g);
            try {
                g.drawImage(slika,0,0,this);
            } catch (Exception e){}
	}
		
        //metod "postavlja" elemente na sliku pre nego sto se pristupi iscrtavanju
	public void offscreen_paint(Graphics g) {
            
            for (int i=0; i<8; i++){
                for (int j=0; j<8; j++){
			
                    if ( ((i+j) % 2) == 0)
                        g.setColor(Color.darkGray);	
                    else
                        g.setColor(svetloSivo);
                    
                    g.fillRect(i*40,j*40,40,40);
		
                    if (oznaceno && i==pocetak_i && j==pocetak_j) {
                        if ((this.potez.mozeDaSeKrece(this.igra.pozicija,i,j))) 
                            g.setColor(Color.yellow);
                        else 					
                            g.setColor(Color.red);
					
                        g.fillRect(i*40,j*40,40,40);
                    }		
		
                    switch (igra.pozicija[i][j]) {
                        case Dame.BELI:
                            g.setColor(Color.white);
                            g.fillOval(i*40+5,j*40+5,30,30);
                        break;
                            
                        case Dame.CRNI:
                            g.setColor(Color.black);
                            g.fillOval(i*40+5,j*40+5,30,30);
                        break;
				
                        case Dame.BDAMA:
                            g.setColor(Color.white);
                            g.fillOval(i*40+5,j*40+5,30,30);
                            g.setColor(Color.red);
                            g.fillOval(i*40+10,j*40+10,20,20);
                        break;
                            
			case Dame.CDAMA:
                            g.setColor(Color.black);
                            g.fillOval(i*40+5,j*40+5,30,30);
                            g.setColor(Color.yellow);
                            g.fillOval(i*40+10,j*40+10,20,20);		
                    }
                } 
            }
	} 

} 