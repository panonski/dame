
/**************************************************************************/
//                                                                         /	
//    Klasa koja implementira prozor sa dijalogom za pomoc korisniku       /
//                                                                         /	
/**************************************************************************/
package dame;

import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.awt.event.*;
import java.util.*;


public class Pomoc extends JDialog {
	private BorderLayout borderLayout1 = new BorderLayout();
	private BorderLayout borderLayout2 = new BorderLayout();

	private JPanel panel1 = new JPanel();
	private JPanel jPanel1 = new JPanel();
	private JPanel jPanel2 = new JPanel();	

	private JScrollPane jScrollPane1 = new JScrollPane();
	private JLabel jLabel1 = new JLabel();
	private JLabel jLabel2 = new JLabel();
	private JLabel jLabel3 = new JLabel();
	private JLabel jLabel4 = new JLabel();
	private JTextPane txtF = new JTextPane();
	private JButton Izadji = new JButton();
	
	//konstruktor
	//inicijalizuje prozor za pomoc korisniku 
	//inicijalizuje direktno velicinu i poziciju prozora
	//i pozivom metode jbInit() ostale elemnte
	public Pomoc(Frame frame, String title, boolean modal) {
            super(frame, title, modal);
            try {
                jbInit();
                pack();
                setSize(700,500);
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                Dimension dialogSize = this.getSize();
                setLocation((screenSize.width - dialogSize.width) / 2, 
                            (screenSize.height - dialogSize.height) / 2);
            }
            catch(Exception ex) {
                ex.printStackTrace();
            }
	}


//alternativni konstruktor
//poziva predhodni gonstruktor sa odgovarajucim parametrima
	public Pomoc() {
   	this(null, "", false);
	}
	
	
	void jbInit() throws Exception {
	
	//rasporedjivaci na panelima
		panel1.setLayout(borderLayout1);
		jPanel1.setLayout(borderLayout2);
		
		//naslov u 3 reda
		jLabel1.setFont(new java.awt.Font("Lucida Handwriting", Font.BOLD, 24));
		jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
		jLabel1.setText("DAME");
		
		jLabel2.setFont(new java.awt.Font("Lucida Handwriting", Font.BOLD, 20));
		jLabel2.setHorizontalAlignment(SwingConstants.CENTER);
		jLabel2.setText("Osnovne napomene i kratka pomoc korisniku");
		
		jLabel3.setFont(new java.awt.Font("Lucida Handwriting", Font.BOLD, 16));
		jLabel3.setHorizontalAlignment(SwingConstants.CENTER);
		jLabel3.setText("AUTORI  :  Dejan Acanski  i  Gordana Rakic");

        //deo prozora u kome se nalazi sam tekst za pomoc
	//dimenzija prostora za tekst
		txtF.setPreferredSize(new Dimension(650, 350));
	//tekst se ne moze menjati (editovati)
		txtF.setEditable(false);
	//font
		txtF.setFont(new java.awt.Font("Dialog", 1, 14));
	//ucitavamo tekst za pomoc pozivom metode ulaz()
	//ova metoda ce ucitati tekst iz ulaznog tekstualnog fajla
		txtF.setText(ulaz());
	
        //dugme za izlaz iz prozora za pomoc i povratak u predhodni prozor
		Izadji.setMaximumSize(new Dimension(150, 25));
		Izadji.setText("Vrati se");
		Izadji.addActionListener(this::Izadji_actionPerformed);
		getContentPane().add(panel1);
	//slozimo naslov na panel
	//ovaj panel ce biti smesten u veliki panel (panel1)
		jPanel1.add(jLabel1, BorderLayout.CENTER);
		jPanel1.add(jLabel2, BorderLayout.NORTH);
		jPanel1.add(jLabel3, BorderLayout.SOUTH);
	//sada slozimo komponente u glavni panel
		panel1.add(Izadji, BorderLayout.SOUTH);
		panel1.add(jPanel1, BorderLayout.NORTH);
	//u sredini je panel sa Scroll-om u kome ce se nalaziti tekst
	//Scroll je u slucaju da tekst zauzima vise od jedne "strane", tj. povrsine
		panel1.add(jScrollPane1,  BorderLayout.CENTER);
		jScrollPane1.getViewport().add(txtF);
	}

	void Izadji_actionPerformed(ActionEvent e) {
	 dispose();
	}
	
	//metoda ucitava tekst iz ulaznog tekstualnog fajla 
	public String ulaz() throws Exception{
		String pom = new String();
		String s = new String();
		BufferedReader buf = new BufferedReader(new FileReader("pomoc.txt"));
		while ( (pom = buf.readLine()) != null)
			s += pom + "\n";
		return s;
	}
	
	
}
