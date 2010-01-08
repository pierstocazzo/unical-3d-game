package utils;

import org.jdom.*;
import org.jdom.input.SAXBuilder;
import org.jdom.output.*;
import java.io.*;
import java.util.Iterator;

/** Classe di test per scrivere un file xml<br>
 * Giusto per cultura personale...
 * 
 * @author slash17
 */
public class XmlWriter {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		try {
			// creo l'elemento radice
			Element root = new Element( "TODO_LIST" );
			
			// creo il documento
			Document document = new Document(root);

			// aggiungo elementi al mio documento
			Element item1 = new Element( "item" );
			item1.setText( "Importazione parametri e opzioni di gioco da file xml" );
			item1.setAttribute( "importanza", "3");
			item1.setAttribute( "stato", "da iniziare" );
			item1.setAttribute( "assegnatario", "joe" );
			root.addContent(item1);

			Element item2 = new Element( "item" );
			item2.setText( "Importazione scena da file xml" );
			item2.setAttribute( "importanza", "2" );
			item2.setAttribute( "stato", "iniziato" );
			item2.setAttribute( "assegnatario", "slash" );
			root.addContent(item2);
			
			Element item3 = new Element( "item" );
			item3.setText( "Sound Manager" );
			item3.setAttribute( "importanza", "1" );
			item3.setAttribute( "stato", "iniziato" );
			item3.setAttribute( "assegnatario", "andrea" );
			root.addContent(item3);

			// creo il mio outputter
			XMLOutputter outputter = new XMLOutputter();
			// imposto il formato dell'outputter come "bel formato"
			outputter.setFormat( Format.getPrettyFormat() );
			// stampo l'output sul file todo.xml nel package utils
			outputter.output( document, new FileOutputStream( "src/utils/todo.xml" ) );

			System.out.println( "Stampa a schermo del file xml\n" );
			outputter.output( document, System.out );
			
			System.out.println( "\nLettura del documento appena creato e stampa senza tag\n" );
			SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build( new File("src/utils/todo.xml") );
			
			Element docRoot = doc.getRootElement();
			
			for( Iterator it = docRoot.getChildren().iterator(); it.hasNext();  ) {
				Element el = (Element) it.next();
				System.out.println( "* " + el.getText() );
				for( Iterator it1 = el.getAttributes().iterator(); it1.hasNext(); ) {
					Attribute a = (Attribute) it1.next();
					System.out.println( "\t" + a.getName() + ": " + a.getValue() );
				}
			}
			
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}