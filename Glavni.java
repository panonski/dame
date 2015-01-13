/**************************************************************************/
//                                                                         /	
//    Klasa koja implementira glavnu klasu koja sadrzi main metod          /
//                                                                         /	
/**************************************************************************/


package dame;

import javax.swing.*;
import java.awt.*;

public class Glavni
{

  //konstruktor aplikacije
  public Glavni() {
	
	//inicijalizujemo ulaz u igricu
    Ulaz frame = new Ulaz();
    frame.validate();				
    frame.setVisible(true);

  }
	
	
  public static void main(String[] args) {   
    new Glavni();	
  }
}


