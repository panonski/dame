/**************************************************************************/
//                                                                         /	
//	Klasa koja implementira prozor u koji se smesta tabla              /
//	Ovde se odvija igra, a tu su i propratne opcije                    /	
//                                                                         /	
/**************************************************************************/


package dame;

import java.applet.*;
import java.awt.*;
import java.util.Date;	
import java.awt.event.*;
import javax.swing.*;



public class Dame extends JFrame
{
	int[][] pozicija = new int[8][8];
	
	static final int PRAZNO = 0;
	static final int BELI 	= 1;
	static final int CRNI 	= 2;
	static final int BDAMA 	= 3;
	static final int CDAMA 	= 4;

	int naPotezu = BELI;
	int izgubio = PRAZNO;


	Choice beliJe = new Choice();
	Choice crniJe = new Choice();
	
	private Button 	izlaz = new Button("IZAĐI");
	private Button 	pomoc = new Button("POMOĆ");
        private Button	novaIgra = new Button("NOVA IGRA");

	Label cekaSe = new Label();
	Label pobednik = new Label("JOŠ NEMA POBEDNIKA");
	Label beli = new Label("BELI IGRAČ JE");
	Label crni = new Label("CRNI IGRAČ JE");
	Label naslov = new Label("DOBRODOŠLI! OVO SU DAME!",Label.CENTER);
								
	Mashina mashina;
	Tabla tabla;
	Potez potez;
	Color tamnoZeleno = new Color(0,90,45);
	Color svetloZeleno = new Color(120,200,120);

//konstruktor
//inicujalizujemo igru sa prenetim parametrima koji se prenose kroz igricu
//izgled i komponente se inicijalizuju pozivom metoda init()

	public Dame(Potez p, Mashina mashina){
		this.potez = p;
		this.mashina=mashina;
                init();
	}		
	
	public void init() {			
	//dodajemo tablu
		this.tabla = new Tabla(this, this.potez);
		this.tabla.addMouseListener(this.tabla);
		this.tabla.setLocation(20,20 );
		this.tabla.setBackground(tamnoZeleno);
		this.setSize(new Dimension(475, 415));
		this.setBackground(tamnoZeleno);
	//dodajemo lsitice za izbor ko je covek ko racunar
		this.beliJe.setBackground(tamnoZeleno);
		this.beliJe.setForeground(svetloZeleno);
		this.crniJe.setBackground(tamnoZeleno);
		this.crniJe.setForeground(svetloZeleno);
		this.beliJe.addItem("Covek");
		this.beliJe.addItem("Racunar");
		this.beliJe.select(1);
		this.crniJe.addItem("Covek");
		this.crniJe.addItem("Racunar");
		this.crniJe.select(0);
	//fontovi, pozadine, poravnanja...	
                this.cekaSe.setBackground(svetloZeleno);
                this.cekaSe.setAlignment(pobednik.CENTER);
                this.pobednik.setBackground(tamnoZeleno);
                this.pobednik.setForeground(Color.red);
                this.pobednik.setAlignment(pobednik.CENTER);
                this.naslov.setBackground(tamnoZeleno);
                this.naslov.setForeground(svetloZeleno);
                this.naslov.setFont(new Font("Lucida Handwriting", Font.BOLD, 22));
                this.izlaz.setBackground(svetloZeleno);
                this.izlaz.setForeground(Color.red);
                this.novaIgra.setBackground(svetloZeleno);
                this.novaIgra.setForeground(Color.yellow);
                this.pomoc.setBackground(svetloZeleno);
                this.pomoc.setForeground(tamnoZeleno);
                this.beli.setBackground(tamnoZeleno);
                this.beli.setForeground(svetloZeleno);
                this.beli.setAlignment(beli.CENTER);
                this.crni.setBackground(tamnoZeleno);
                this.crni.setForeground(svetloZeleno);
                this.crni.setAlignment(beli.CENTER);
	//osluskivaci dogadjaja na dugmadima 
	//za izlaz iz igrice, otvaranje prozora za pomoc i povratak na prozor sa opcijama		
                this.izlaz.addActionListener(alIzlaz);
                this.pomoc.addActionListener(alPomoc);
                this.novaIgra.addActionListener(alOpcije);

	//rasporedjujemo komponente
                Box boxSouth = Box.createHorizontalBox();
                Box boxEast  = Box.createVerticalBox();
                Box boxWest  = Box.createVerticalBox();
                Box boxCenter= Box.createHorizontalBox();

                boxSouth.add(Box.createHorizontalStrut(30));
                boxSouth.add(pomoc);
                boxSouth.add(Box.createHorizontalStrut(30));
                boxSouth.add(izlaz);
                boxSouth.add(Box.createHorizontalStrut(30));
    		boxSouth.add(novaIgra);
                boxSouth.add(Box.createHorizontalStrut(30));

                boxWest.add(Box.createVerticalStrut(60));			
                boxWest.add(beli);
                boxWest.add(beliJe);
                boxWest.add(Box.createVerticalStrut(20));
                boxWest.add(crni);
                boxWest.add(crniJe);
                boxWest.add(Box.createVerticalStrut(60));
                boxWest.add(pobednik);
                boxWest.add(Box.createVerticalStrut(20));
                boxWest.add(cekaSe);
                boxWest.add(Box.createVerticalStrut(60));

                Container cp = getContentPane();
                cp.add(BorderLayout.WEST, boxWest);
                cp.add(BorderLayout.SOUTH, boxSouth);
                cp.add(BorderLayout.CENTER,tabla);
                cp.add(BorderLayout.NORTH, naslov);

                resetujTablu();
	}
	
	ActionListener alIzlaz = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            };
        };


	ActionListener alPomoc = new ActionListener() {
            public void actionPerformed (ActionEvent e){
		Pomoc pomoc = new Pomoc();
  	  	pomoc.show();
            };
        };

	ActionListener alOpcije = new ActionListener() {
            public void actionPerformed (ActionEvent e){
		Ulaz ulaz = new Ulaz();
		ulaz.show();
  	  	dispose();
            };
        };

	public void paint(Graphics g){
	}

        //metod postavlja tablu za pocetak igrice
	protected void resetujTablu() {
            this.tabla.oznaceno = false;
            this.tabla.nedovrsen = false;
            this.izgubio = PRAZNO;

            //postavljamo figure na pocetne pozicije
            for (int i=0; i<8; i++) {
                for (int j=0; j<3; j++){
                    if (dozvoljenoPolje(i,j))
                        pozicija[i][j] = BELI;	
                    else if (dozvoljenoPolje(i,7-j))
                        pozicija[i][7-j] = CRNI;

                    else {
                        pozicija[i][j] = PRAZNO;
                        pozicija[i][7-j] = PRAZNO;
                    }
                }
                for (int j=3; j<5;j++)
                    pozicija[i][j] = PRAZNO;
                }

            //na pocetku je crni na potezu
            this.naPotezu = CRNI;
            promeniCekaSe();
            //pa i odigrava se potez ako je crni racunar
            if (crniJe.getSelectedIndex() == 1) {
                this.naPotezu = BELI;
                this.promeniNaPotezu(this.naPotezu);
            }
            prikaziTablu();
            //iscrtavamo ovako postavljenu tablu
	}

        //ovde nam se samo menja labelica koja ispisuje ko je na potezu
        //i da li ima pobednika kao i ko je pobedio ako ima pobednika
	void promeniCekaSe() {
            if (this.potez.generisiPoteze(pozicija, naPotezu).isEmpty()) {
                if (naPotezu == BELI)
                        izgubio = BELI;
                else
                        izgubio = CRNI;
            }
		
            if (this.naPotezu == BELI) {
                this.cekaSe.setForeground(Color.white);
                this.cekaSe.setText("IGRA BELI IGRAC");
            }
            else {
                this.cekaSe.setForeground(Color.black);
                this.cekaSe.setText("IGRA CRNI IGRAC");
            }

            if (this.izgubio == BELI) {
                this.pobednik.setForeground(Color.black);
                this.pobednik.setText("CRNI JE POBEDIO!");
		}
            else if (this.izgubio == CRNI) {
                this.pobednik.setForeground(Color.white);
                this.pobednik.setText("BELI JE POBEDIO!");
            }
	}

        //poziva metod iz klase Tabla koji iscrtava aktuelnu tablu
	private void prikaziTablu() {
            this.tabla.repaint();	
            
	}								   

        //ovde je odigravanje poteza za racunar
        public void promeniNaPotezu(int naPotezu) {
            int rezultat;
            int[] pot = new int[4];

            prikaziTablu();
           
            //ako je odigrao crni a beli je racunar...
            if (this.naPotezu == CRNI && beliJe.getSelectedIndex() == 1){
                this.naPotezu = BELI; 
                promeniCekaSe();

                rezultat = this.mashina.MiniMax(this.pozicija, 0, pot,this.naPotezu, -Mashina.BESKONACNO, Mashina.BESKONACNO);

                if ((rezultat == Mashina.BESKONACNO) && 
                    (this.potez.generisiPoteze(pozicija, naPotezu).isEmpty())){	
                        this.izgubio = BELI;
                } else {
                    this.potez.odigraj(pozicija, pot);
                    if (this.tabla.izvrsavaj && izgubio == PRAZNO && crniJe.getSelectedIndex() == 1)
                        promeniNaPotezu(this.naPotezu);	
                    else
                        this.naPotezu = CRNI;	
                }
             
            //ili je beli odigrao a racunar je crni
            } else {
                if (this.naPotezu == BELI && crniJe.getSelectedIndex() == 1){
                    this.naPotezu = CRNI; 
                    promeniCekaSe();

                    rezultat = this.mashina.MiniMax(pozicija,0,pot,this.naPotezu, -Mashina.BESKONACNO, Mashina.BESKONACNO);	

                    if ((rezultat == -Mashina.BESKONACNO) && 
                        (this.potez.generisiPoteze(pozicija, naPotezu).isEmpty())){	
                            this.izgubio = CRNI;

                    } else {
                        this.potez.odigraj(pozicija, pot);
                        if (this.tabla.izvrsavaj && izgubio == PRAZNO && beliJe.getSelectedIndex() == 1 )
                            promeniNaPotezu(this.naPotezu);
                        else
                            this.naPotezu = BELI;
                    }
                    
          //inace samo promeni ko je na potezu jer ne treba da igra kompjuter
                } else {
                    if (this.naPotezu == BELI)
                        this.naPotezu = CRNI;
                    else
                        this.naPotezu = BELI;
            }
            //iscrtavamo novo stanje
            promeniCekaSe();
            prikaziTablu();
	}
    }
	public void update(Graphics g)	{
		paint(g);
	} 

	boolean dozvoljenoPolje(int i, int j)	{
		return (i+j)%2 == 1;
	}
		
};	



