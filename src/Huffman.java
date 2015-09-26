import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class Huffman {

	private static TableFrequences tf;

	public static void main(String[] args) {
		if(createFile() > -1) {
			decodeFile();
		}
	}

	private static int createFile() {
		tf = new TableFrequences(
				Paths.get("C:/Users/AK79880/testing/encode.txt"));
		if(tf.getTf().size() == 0) return - 1;
		
		Map<Integer, String> map = new HashMap<Integer, String>();
		if(tf.getTf().size() == 1) {
			map.put(tf.getTf().get(0).getNom(), "0");
		} else {
			map = BinaryTree.creerCorespondance(
					creerArbre(tf), "");
		}
		
		String encodage = encoderFichier(map);

		if (encodage.length() % 8 != 0) {
			char[] chars = new char[8 - (encodage.length() % 8)];
			Arrays.fill(chars, '0');
			encodage += new String(chars);
		}

		createFile(encodage, map, tf.getTotalSize());
		return 1;
	}                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           

	private static BinaryTree creerArbre(TableFrequences tf) {
		BinaryTree root = new BinaryTree();
		Map<Caractere, BinaryTree> binaryTrees = new HashMap<Caractere, BinaryTree>();
		if(tf.getTf().size() == 1) {
			root.setNode(tf.getTf().get(0));
			return root;
		}
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
					new InputStreamReader(in, "ISO-8859-1"));
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
			writer = new PrintWriter("C:/Users/AK79880/testing/encoded.txt", "ISO-8859-1");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		StringBuilder textToWrite = new StringBuilder();
		for (Entry<Integer, String> entry : map.entrySet()) {
			int integer = entry.getKey();
			String binaryTreeValue = entry.getValue();
			int characterAdded = 0;
			
			if (binaryTreeValue.length() % 8 != 0) {
				char[] chars = new char[8 - (binaryTreeValue.length() % 8)];
				characterAdded = 8 - (binaryTreeValue.length() % 8);
				Arrays.fill(chars, '0');
				binaryTreeValue += new String(chars);
			}
			
			textToWrite.append(binaryTreeValue.length() / 8);
			textToWrite.append(characterAdded);
			for(int i = 0; i < binaryTreeValue.length(); i+=8) {
				textToWrite.append(new String(new byte[]{(byte)Integer.parseInt(binaryTreeValue.substring(i, i+8), 2)}, Charset.forName("ISO-8859-1")));
			}
			textToWrite.append(new String(new byte[]{(byte)integer}, Charset.forName("ISO-8859-1")));
		}
		textToWrite.append("|");
		writer.write(textToWrite.toString());
		
		StringBuilder encodedText = new StringBuilder();
		for (int i = 0; i < encoding.length(); i += 8) {
			String tempEncoding = encoding.substring(i, i + 8);
			byte myByte = (byte)Integer.parseInt(tempEncoding, 2);
			encodedText.append(new String(new byte[]{myByte}, Charset.forName("ISO-8859-1")));
		}
		
		encodedText.insert(0, size + "|");
		writer.print(encodedText.toString());
		writer.close();
	}
	

	private static void decodeFile() {
		try {
			Reader bytestream = null;
				bytestream = new BufferedReader(new InputStreamReader(
				        new FileInputStream("C:/Users/AK79880/testing/encoded.txt"), "ISO-8859-1"));
			

			Map<String, Character> associativeMap = new HashMap<String, Character>();
			
			int ch = 0;
			do {
				StringBuilder binaryTreeCode = new StringBuilder();
				char char1 = (char)bytestream.read();
				if(char1 == '|') break;
				int numberOfEight = Integer.parseInt(String.valueOf(char1));
				char char2 = (char)bytestream.read();
				int numberCharactersToRemove = Integer.parseInt(String.valueOf(char2));
				for(int i = 0; i < numberOfEight; i++) {
					byte character = (byte)bytestream.read();
					int integerBase10 = character & 0xFF;
					StringBuilder binaryCode = new StringBuilder(Integer.toBinaryString(integerBase10));
					if(binaryCode.length() % 8 != 0) {
						char[] chars = new char[8 - (binaryCode.length() % 8)];
						Arrays.fill(chars, '0');
						binaryCode.insert(0, chars);
					}
					
					binaryTreeCode.append(binaryCode.toString());
				}
				
				if(numberCharactersToRemove > 0) {
					binaryTreeCode.replace(binaryTreeCode.length() - numberCharactersToRemove, binaryTreeCode.length(), "");
				}
				
				associativeMap.put(binaryTreeCode.toString(), new String(new byte[]{(byte)(int)bytestream.read()}, Charset.forName("ISO-8859-1")).charAt(0));
				
			} while(true);
			
			int numberOfCharacters = Integer.parseInt(readUntil(0, '|', bytestream));
			
			// need to get everything here			
			StringBuilder encodedBinaryString = new StringBuilder();
			while((ch = bytestream.read()) != -1) {
				byte number = (byte)ch;
				int integerBase10 = number & 0xFF;
				StringBuilder binaryNumber = new StringBuilder(Integer.toBinaryString(integerBase10));
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
				
			
			bytestream.close();
			createNewFile(decodedString.toString());
		} catch (IOException e) {

		}
	}
	
	private static void createNewFile(String output) {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter("C:/Users/AK79880/testing/decoded.txt", "ISO-8859-1");
		} catch (FileNotFoundException e) {
			e.printStackTrace();																																								
		} catch (UnsupportedEncodingException e) {
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
	
	private static String readUntil(int charToAdd, char character, Reader reader) {
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
