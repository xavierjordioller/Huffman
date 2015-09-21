import java.util.HashMap;
import java.util.Map;

public class BinaryTree {
	private BinaryTree left;
	private BinaryTree right;
	private Caractere node;

	public BinaryTree() {

	}

	public BinaryTree(Caractere node) {
		this.node = node;
	}

	public BinaryTree getLeft() {
		return left;
	}

	public void setLeft(BinaryTree left) {
		this.left = left;
	}

	public BinaryTree getRight() {
		return right;
	}

	public void setRight(BinaryTree right) {
		this.right = right;
	}

	public Caractere getNode() {
		return node;
	}

	public void setNode(Caractere node) {
		this.node = node;
	}

	public static Map<Integer, String> creerCorespondance(BinaryTree tree, String nombre) {
		Map<Integer, String> map = new HashMap<Integer, String>();
		if (nombre.equals("")) {
			map.putAll(creerCorespondance(tree.getLeft(), "0"));
			map.putAll(creerCorespondance(tree.getRight(), "1"));
		} else if (!nombre.isEmpty()) {
			if (tree.getLeft() == null) {
				map.put(tree.getNode().getNom(), nombre);
			} else {
				map.putAll(creerCorespondance(tree.getLeft(), nombre + "0"));
				map.putAll(creerCorespondance(tree.getRight(), nombre + "1"));
			}
		}
		return map;
	}

}
