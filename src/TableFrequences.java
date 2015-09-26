import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class TableFrequences {
	private ArrayList<Caractere> tf = new ArrayList();
	
	public TableFrequences(Path path){
		
		try {
			
			// Lecture du fichier a compresser
			InputStream in = Files.newInputStream(path);
			BufferedReader reader = new BufferedReader(new InputStreamReader(in, Charset.forName("ISO-8859-1")));
			
			int ch = 0;
			while((ch = reader.read()) != -1){
			
				// Recherche du caractere dans la table de frequences
				boolean found = false;
				int i = 0;
		        while (!found && getTf().size() !=0){
		        	if(getTf().get(i).getNom() == ch)	        	
		        		found = true;
		        	i++;
		        	if(i >= getTf().size())
		        		break;
		        }
		        
		        // Si trouve, on augmente de 1 la frequence
		        if (found){
		        	getTf().get(i-1).incrementerNbr();
		        	
		        }
		        // Sinon, on ajoute le nouveau character
		        else {
		        	getTf().add(new Caractere(ch));
		        }
				
			}
			reader.close();
			in.close();
			
		} catch (IOException e) {
			
		}
	}
	
	public void sortTable() {
		Collections.sort(getTf(), new Comparator<Caractere>() {
			@Override
			public int compare(Caractere element1, Caractere element2) {
				return Integer.compare(element1.getNbr(), element2.getNbr());
			}
		});
	}

	public ArrayList<Caractere> getTf() {
		return tf;
	}
	
	public int getTotalSize() {
		int totalSize = 0;
		for(Caractere caractere : tf) {
			totalSize += caractere.getNbr();
		}
		
		return totalSize;
	}
}
