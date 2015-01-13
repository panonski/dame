/**************************************************************************/
//                                                                         /	
//    Klasa koja implementira prozor sa dijalogom za izbor pravila         /
//                                                                         /	
/**************************************************************************/


package dame;


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Ulaz extends JDialog {

	static Potez potez;	

	Color tamnoZeleno = new Color(0,90,45);
	Color svetloZeleno = new Color(120,200,120);

	private JLabel naslov = new JLabel();

	private JPanel contentPane;
	private JButton pomoc = new JButton("POMOĆ");
        private JButton	resetuj = new JButton("RESETUJ");
        private JButton	izlaz = new JButton("IZAĐI");

	private JButton moramoze = new JButton();
	private JButton nemoramoze = new JButton();
	private JButton moranemoze = new JButton(); 
	private JButton nemoranemoze = new JButton();

	private BorderLayout borderLayout1 = new BorderLayout();
	
	//konstruktor 
	//inicijalizuje izgled i ponasanje pozivom metode Init()
	public Ulaz() {
            enableEvents(AWTEvent.WINDOW_EVENT_MASK);
            try {
                Init();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
  
	//inicujalizuje izgled i ponasanje u prozoru
	public void Init() {
	
	//postavljamo rasporedjivac na panel
            contentPane = (JPanel) this.getContentPane();
            contentPane.setLayout(borderLayout1);
            Box boxSouth = Box.createHorizontalBox();
            Box boxCenter= Box.createVerticalBox();
            
        //dimenzije, pozadina i naslov u naslovnoj liniji
            this.setSize(new Dimension(500, 400));
            contentPane.setBackground(tamnoZeleno);
            this.setTitle("DAME");

        //naslov: font,pozadina, poravnanje i tekst			
            naslov.setFont(new Font("Lucida Handwriting", Font.BOLD, 20));
            naslov.setBackground(tamnoZeleno);
            naslov.setForeground(svetloZeleno);	
            naslov.setHorizontalAlignment(SwingConstants.CENTER);
            naslov.setHorizontalTextPosition(SwingConstants.CENTER);
            naslov.setText("DOBRODOŠLI! OVO SU DAME!");
        //dugme za pomoc
            pomoc.setBackground(svetloZeleno);
            pomoc.setForeground(Color.red);
            pomoc.setPreferredSize(new Dimension(120, 60));
            pomoc.addActionListener(new ActionListener() {
                public void actionPerformed (ActionEvent e){
                    Pomoc pomoc = new Pomoc();
                    pomoc.show();
		};
            });
        //dugme za izlaz		
            izlaz.setBackground(svetloZeleno);
            izlaz.setForeground(Color.red);
            izlaz.setPreferredSize(new Dimension(120, 60));
            izlaz.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    izlaz_actionPerformed(e);
                }
            });
			
				
//4 dugmeta za 4 opcije
//opcije predstavljaju 4 razlicite kombinacije pravila igre
//detaljniji opis ocija moze se dobiti pritiskom na dugme za pomoc
            moramoze.setPreferredSize(new Dimension(400, 70));
            moramoze.setBackground(tamnoZeleno);		
            moramoze.setForeground(svetloZeleno);
            moramoze.setFont(new Font("Lucida Sans Unicode",Font.PLAIN, 14));
            moramoze.setText("  ako figura moze da jede, mora da jede; pion moze da jede damu   ");
            moramoze.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    moramoze_actionPerformed(e);
                }
            });
		
            moranemoze.setPreferredSize(new Dimension(400, 70));
            moranemoze.setBackground(tamnoZeleno);		
            moranemoze.setForeground(svetloZeleno);
            moranemoze.setFont(new Font("Lucida Sans Unicode",Font.PLAIN, 14));
            moranemoze.setText("    ako figura moze da jede, mora da jede; pion ne moze da jede damu      ");
            moranemoze.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    moranemoze_actionPerformed(e);
                }
            });
	
            nemoramoze.setPreferredSize(new Dimension(400, 70));
            nemoramoze.setBackground(tamnoZeleno );		
            nemoramoze.setForeground(svetloZeleno);
            nemoramoze.setFont(new Font("Lucida Sans Unicode",Font.PLAIN, 14));
            nemoramoze.setText("             figura ne mora da jede; pion moze da jede damu                ");
            nemoramoze.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    nemoramoze_actionPerformed(e);
                }
            });
		
            nemoranemoze.setPreferredSize(new Dimension(400, 70));
            nemoranemoze.setBackground(tamnoZeleno );		
            nemoranemoze.setForeground(svetloZeleno);
            nemoranemoze.setFont(new Font("Lucida Sans Unicode",Font.PLAIN, 14));
            nemoranemoze.setText("              figura ne mora da jede; pion ne moze da jede damu         ");
            nemoranemoze.addActionListener(this::nemoranemoze_actionPerformed);
            
	//smestanje komponenti na panel u donji deo prozora...
            boxSouth.add(Box.createHorizontalStrut(100));
            boxSouth.add(pomoc);
            boxSouth.add(Box.createHorizontalStrut(100));
            boxSouth.add(izlaz);
            boxSouth.add(Box.createHorizontalStrut(100));
            
        //... i u centralni deo prozora		
            boxCenter.add(Box.createVerticalStrut(30));				
            boxCenter.add(moramoze);
            boxCenter.add(Box.createVerticalStrut(15));
            boxCenter.add(moranemoze);				
            boxCenter.add(Box.createVerticalStrut(15));
            boxCenter.add(nemoramoze);
            boxCenter.add(Box.createVerticalStrut(15));
            boxCenter.add(nemoranemoze);
            boxCenter.add(Box.createVerticalStrut(30));
				
            Container cp = getContentPane();
            cp.add(BorderLayout.SOUTH, boxSouth);
            cp.add(BorderLayout.CENTER,boxCenter);
            cp.add(BorderLayout.NORTH, naslov);
	
	}
	
	//izborom svake od opcija inicijalizuje se igrica sa odgovarajucim parametrima
	//ovde se koristi kasno vezivanje obejkata
	void moramoze_actionPerformed(ActionEvent e) {
		potez = new ForsiranSkok();
		Mashina mashina = new Mashina(potez);
		Dame igra = new Dame(potez,mashina);		
                igra.show();
		this.hide();
 	}
	
	void moranemoze_actionPerformed(ActionEvent e) {
		potez = new ForsiranSkokBezDama();
		Mashina mashina = new Mashina(potez);
		Dame igra = new Dame(potez,mashina);			
                igra.show();
		this.hide();
  	}

        void nemoramoze_actionPerformed(ActionEvent e) {
                potez = new NeforsiranSkok();	
		Mashina mashina = new Mashina(potez);
		Dame igra = new Dame(potez,mashina);			
                igra.show();
		this.hide();
  	}
	
 	void nemoranemoze_actionPerformed(ActionEvent e) {
   		potez = new NeforsiranSkokBezDama();
		Mashina mashina = new Mashina(potez);
		Dame igra = new Dame(potez,mashina);
		igra.show();
		this.hide();
 	}
		
		
	void izlaz_actionPerformed(ActionEvent e) {
                System.exit(0);
        }
	
}

