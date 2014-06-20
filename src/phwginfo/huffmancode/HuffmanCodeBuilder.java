package phwginfo.huffmancode;

import javax.swing.*;
import java.util.*;

/**
 * Huffman compressor from http://rosettacode.org/wiki/Huffman_coding#Java
 */


class HuffmanTree implements Comparable<HuffmanTree> {
    public final int frequency; // the frequency of this tree
    public HuffmanTree(int freq) { frequency = freq; }

    // compares on the frequency
    public int compareTo(HuffmanTree tree) {
        return frequency - tree.frequency;
    }
}

class HuffmanLeaf extends HuffmanTree {
    public final char value; // the character this leaf represents

    public HuffmanLeaf(int freq, char val) {
        super(freq);
        value = val;
    }
}

class HuffmanNode extends HuffmanTree {
    public final HuffmanTree left, right; // subtrees

    public HuffmanNode(HuffmanTree l, HuffmanTree r) {
        super(l.frequency + r.frequency);
        left = l;
        right = r;
    }
}


public class HuffmanCodeBuilder {
    // input is an array of frequencies, indexed by character code
    public HuffmanTree buildTree(Map<Character,Integer> charFreqs) {

        // PriorityQueue ist eine sortie Liste
        PriorityQueue<HuffmanTree> trees = new PriorityQueue<HuffmanTree>();

        // Am Anfang: nur Bäume die keine Kinder haben, und nicht verbunden sind
        // one for each non-empty character
        for (char c: charFreqs.keySet())
            trees.offer(new HuffmanLeaf(charFreqs.get(c), c));

        assert trees.size() > 0;
        // loop until there is only one tree left
        while (trees.size() > 1) {
            // two trees with least frequency
            HuffmanTree a = trees.poll();
            HuffmanTree b = trees.poll();

            // put into new node and re-insert into queue
            trees.offer(new HuffmanNode(a, b));
        }
        return trees.poll();
    }

    public void printCodes(HuffmanTree tree, StringBuffer prefix) {
        assert tree != null;
        if (tree instanceof HuffmanLeaf) {
            HuffmanLeaf leaf = (HuffmanLeaf)tree;

            // print out character, frequency, and code for this leaf (which is just the prefix)
            System.out.println(leaf.value + "       " + leaf.frequency + "       " + prefix);

        } else if (tree instanceof HuffmanNode) {
            HuffmanNode node = (HuffmanNode)tree;

            // traverse left
            prefix.append('0');
            printCodes(node.left, prefix);
            prefix.deleteCharAt(prefix.length()-1);

            // traverse right
            prefix.append('1');
            printCodes(node.right, prefix);
            prefix.deleteCharAt(prefix.length()-1);
        }
    }

    public void run() {
        String test = JOptionPane.showInputDialog("Geben Sie die Eingabe:");

        // Abbildung Buchstabe => Frequenzen
        Map<Character, Integer> charFreqs = new HashMap<Character, Integer>();


        // Buchstaben zählen
        for (char c : test.toCharArray()) {
            if(charFreqs.containsKey(c))
                charFreqs.put(c, charFreqs.get(c)+1);
            else
                charFreqs.put(c,1);
        }

        // build tree
        HuffmanTree tree = buildTree(charFreqs);

        // print out results
        System.out.println("SYMBOL\tWEIGHT\tHUFFMAN CODE");
        printCodes(tree, new StringBuffer());
    }

    public static void main(String[] args) {
        // neues Coder, Laufen
        new HuffmanCodeBuilder().run();
    }
}