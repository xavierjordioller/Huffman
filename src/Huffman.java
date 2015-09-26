import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Huffman {

	private static TableFrequences tf;

	public static void main(String[] args) {

		createFile();

		decodeFile();
	}

	private static void createFile() {
		for(int i = 0; i < 256; i++) {
			System.out.println(i + " = " + (char)i);
		}
		
		
		// Appel au constructeur de la classe TableFrequences
		tf = new TableFrequences(
				Paths.get("C:/Users/AK79880/testing/encode.txt"));
		tf.printTable();
		// tf.sortTable();
		tf.printTable();

		Map<Integer, String> map = BinaryTree.creerCorespondance(
				creerArbre(tf), "");
		String encodage = encoderFichier(map);
		// on le normalize a un multiple de 8 (rajoute de 0 a la fin )

		if (encodage.length() % 8 != 0) {
			char[] chars = new char[8 - (encodage.length() % 8)];
			Arrays.fill(chars, '0');
			encodage += new String(chars);
		}

		createFile(encodage, map, tf.getTotalSize());
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

	private static void createFile(String encoding, Map<Integer, String> map, int size) {
		PrintWriter writer = null;

		try {
			writer = new PrintWriter("C:/Users/AK79880/testing/encoded.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		StringBuilder textToWrite = new StringBuilder();
		for (Entry<Integer, String> entry : map.entrySet()) {
			int integer = entry.getKey();
			textToWrite.append(entry.getValue() + "=" + (char)integer);
		}
		textToWrite.append("|");
		writer.write(textToWrite.toString());
		
		StringBuilder encodedText = new StringBuilder();
		for (int i = 0; i < encoding.length(); i += 8) {
			String tempEncoding = encoding.substring(i, i + 8);
			encodedText.append(Integer.parseInt(tempEncoding, 2));
			encodedText.append(".");
		}
		
		encodedText.insert(0, size + "|");
		writer.print(encodedText.toString());
		writer.close();
	}
	

	private static void decodeFile() {
		try {
			InputStream in = Files.newInputStream(Paths
					.get("C:/Users/AK79880/testing/encoded.txt"));
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(in));

			Map<String, Character> associativeMap = new HashMap<String, Character>();
			
			int ch = 0;
			do {
				StringBuilder key = new StringBuilder(readUntil(ch, '=', reader));
				associativeMap.put(key.toString(), (char)reader.read());
				ch = reader.read();
			} while(ch != '|');
			
			int numberOfCharacters = Integer.parseInt(readUntil(0, '|', reader));
			
			// need to get everything here
			StringBuilder textEncoded = new StringBuilder();
			/*while ((ch = reader.read()) != -1) {
				StringBuilder binaryString = new StringBuilder(Integer.toBinaryString(ch));
				
				textEncoded.append(binaryString);
			}*/
			
			String line = reader.readLine();
			line = line.substring(0, line.length() - 1);
			StringBuilder encodedBinaryString = new StringBuilder();
			for(String number : line.split("\\.")) {
				StringBuilder binaryNumber = new StringBuilder(Integer.toBinaryString(Integer.parseInt(number)));
				if (binaryNumber.length() % 8 != 0) {
					char[] chars = new char[8 - (binaryNumber.length() % 8)];
					Arrays.fill(chars, '0');
					binaryNumber.insert(0, new String(chars));
				}
				encodedBinaryString.append(binaryNumber.toString());
			}
				
			
			StringBuilder currentEncodedString = new StringBuilder();
			StringBuilder decodedString = new StringBuilder();
			
			for(char character : encodedBinaryString.toString().toCharArray()) {
				currentEncodedString.append(character);
				if(associativeMap.containsKey(currentEncodedString.toString())) {
					decodedString.append(associativeMap.get(currentEncodedString.toString()));
					currentEncodedString = new StringBuilder();
				}
				if(decodedString.length() == numberOfCharacters) break;
			}
				
			
			reader.close();
			in.close();
			createNewFile(decodedString.toString());
		} catch (IOException e) {

		}
	}
	
	private static void createNewFile(String output) {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter("C:/Users/AK79880/testing/decoded.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		writer.print(output);
		writer.close();
	}

	public static <K, V> HashMap<V, K> reverse(Map<K, V> map) {
		HashMap<V, K> rev = new HashMap<V, K>();
		for (Map.Entry<K, V> entry : map.entrySet())
			rev.put(entry.getValue(), entry.getKey());
		return rev;
	}
	
	private static String readUntil(int charToAdd, char character, BufferedReader reader) {
		StringBuilder builder = new StringBuilder();
		if(charToAdd != 0)
			builder.append((char)charToAdd);
		char currentCharacter;
		try {
			currentCharacter = (char)reader.read();
		
		while(currentCharacter != character) {
			builder.append(currentCharacter);
			currentCharacter = (char)reader.read();
		}
		return builder.toString();
		} catch (IOException e) {
			return builder.toString();
		}
	}
}
