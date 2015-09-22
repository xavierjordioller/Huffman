import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Huffman {

	private static TableFrequences tf;

	public static void main(String[] args) {

		// Appel au constructeur de la classe TableFrequences
		/*tf = new TableFrequences(
				Paths.get("C:/Users/AK79880/testing/encode.txt"));
		tf.printTable();
		// tf.sortTable();
		tf.printTable();
	//	tf.getTf().add(new Caractere(-1));
	

		Map<Integer, String> map = BinaryTree.creerCorespondance(
				creerArbre(tf), "");
		String encodage = encoderFichier(map);

		// on le normalize a un multiple de 8 (rajoute de 0 a la fin )

		if (encodage.length() % 8 != 0) {
			char[] chars = new char[8 - (encodage.length() % 8)];
			Arrays.fill(chars, '0');
			encodage += new String(chars);
		}

		createFile(encodage, map);*/

		 decodeFile();
	}

	private static BinaryTree creerArbre(TableFrequences tf) {
		BinaryTree root = new BinaryTree();
		Map<Caractere, BinaryTree> binaryTrees = new HashMap<Caractere, BinaryTree>();
		while (tf.getTf().size() > 1) {
			tf.sortTable();
			Caractere newCaractere = new Caractere(-1);
			newCaractere.setNbr(tf.getTf().get(0).getNbr()
					+ tf.getTf().get(1).getNbr());
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
		StringBuilder stringBuilder = new StringBuilder();
		try {
			in = Files.newInputStream(Paths
					.get("C:/Users/AK79880/testing/encode.txt"));

			BufferedReader reader = new BufferedReader(
					new InputStreamReader(in));
			int ch = 0;

			while ((ch = reader.read()) != -1) {
				stringBuilder.append(map.get(ch));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stringBuilder.toString();
	}

	private static void createFile(String encoding, Map<Integer, String> map) {
		PrintWriter writer = null;

		try {
			writer = new PrintWriter("C:/Users/AK79880/testing/encoded.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		for (Entry<Integer, String> entry : map.entrySet()) {
			writer.print(".");

			int i = 0;

			if (!entry.getValue().matches("0+")) {
				while (true) {
					if (entry.getValue().charAt(i) == '0') {
						i++;
					} else {
						break;
					}
				}
			}

			int integer = entry.getKey();
			if (i > 0) {
				writer.print(i
						+ ">"
						+ (char) Integer.parseInt(
								entry.getValue().substring(i,
										entry.getValue().length()), 2) + "="
						+ ((char) integer));
			} else {
				writer.print(">" + (char) Integer.parseInt(entry.getValue(), 2)
						+ "=" + ((char) integer));
			}
		}
		writer.print("|");

		for (int i = 0; i < encoding.length(); i += 8) {
			String tempEncoding = encoding.substring(i, i + 8);

			int value = Integer.parseInt(tempEncoding, 2);
			String character = String.valueOf(Character.toChars(value));
			writer.print(character);
		}
		writer.close();
	}

	private static void decodeFile() {
		Map<String, Integer> map = new HashMap<String, Integer>();
		try {
			InputStream in = Files.newInputStream(Paths
					.get("C:/Users/AK79880/testing/encoded.txt"));
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(in));

			int ch = 0;
			ch = reader.read();
			while(true) {
				StringBuilder tempString  = new StringBuilder();
				
				if(ch != '>') {
					char number = (char) ch;
					char[] chars = new char[Integer.parseInt(number + "")];
					Arrays.fill(chars, '0');
					tempString.append(new String(chars));
					reader.read();
				}
				
				ch = reader.read();
				tempString.append(Integer.toBinaryString(ch));
				reader.read();
				ch = reader.read();
				map.put(tempString.toString(), ch);
				ch = reader.read();
				if(ch == '|') {
					break;
				}
				ch = reader.read();
			}
			
			reader.close();
			in.close();

		} catch (IOException e) {

		}
	}

	public static <K, V> HashMap<V, K> reverse(Map<K, V> map) {
		HashMap<V, K> rev = new HashMap<V, K>();
		for (Map.Entry<K, V> entry : map.entrySet())
			rev.put(entry.getValue(), entry.getKey());
		return rev;
	}
}
