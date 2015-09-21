import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Huffman {

	private static TableFrequences tf;

	public static void main(String[] args) {

		// Appel au constructeur de la classe TableFrequences
		tf = new TableFrequences(Paths.get("C:\\Users\\xavie\\Desktop\\Montreal\\ÉTS\\LOG320_SDA\\TP1\\Huffman.txt"));
		tf.printTable();
		// tf.sortTable();
		tf.printTable();
		tf.getTf().add(new Caractere(-1));
		/*
		 * tf = new TableFrequences(
		 * Paths.get("C:\\Users\\xavie\\Pictures\\VSCOCAM\\tibi.jpg") );
		 */

		Map<Integer, String> map = BinaryTree.creerCorespondance(creerArbre(tf), "");
		String encodage = encoderFichier(map);
	}

	private static BinaryTree creerArbre(TableFrequences tf) {
		BinaryTree root = new BinaryTree();
		Map<Caractere, BinaryTree> binaryTrees = new HashMap<Caractere, BinaryTree>();
		while (tf.getTf().size() > 1) {
			tf.sortTable();
			Caractere newCaractere = new Caractere(-1);
			newCaractere.setNbr(tf.getTf().get(0).getNbr() + tf.getTf().get(1).getNbr());
			root = new BinaryTree();
			if (binaryTrees.containsKey(tf.getTf().get(0))) {
				root.setLeft(binaryTrees.get(tf.getTf().get(0)));
			} else {
				root.setLeft(new BinaryTree(tf.getTf().get(0)));
			}

			if (binaryTrees.containsKey(tf.getTf().get(1))) {
				root.setRight(binaryTrees.get(tf.getTf().get(1)));
			} else {
				root.setRight(new BinaryTree(tf.getTf().get(1)));
			}

			tf.getTf().remove(0);
			tf.getTf().remove(0);
			tf.getTf().add(newCaractere);
			binaryTrees.put(newCaractere, root);
		}
		return binaryTrees.get(tf.getTf().get(0));
	}

	private static String encoderFichier(Map<Integer, String> map) {
		InputStream in = null;
		String encoding = "";
		try {
			in = Files.newInputStream(
					Paths.get("C:\\Users\\xavie\\Desktop\\Montreal\\ÉTS\\LOG320_SDA\\TP1\\Huffman.txt"));

			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			int ch = 0;

			while ((ch = reader.read()) != -1) {
				encoding += map.get(ch);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return encoding;
	}
}
