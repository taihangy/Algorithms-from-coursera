import java.util.*;

public class CircularSuffixArray {
	private Integer[] index;
	private int stringLen;
	private String input;

	public CircularSuffixArray(String s) {
		input = s;
		stringLen = s.length();
		index = new Integer[stringLen];

		for (int i = 0; i < index.length; i++) {
			index[i] = i;
		}

		sortSuffixes();
	}

	// returns length of string s
	public int length() {
		return stringLen;
	}

	// returns the index of the ith sorted suffix
	public int index(int i) {
		return index[i];
	}

	// method compares suffix characters for sorting suffixes, uses quicksort
	// and Comparator
	private void sortSuffixes() {
		Arrays.sort(index, new Comparator<Integer>() {
			public int compare(Integer i, Integer j) {
				if (i.equals(j)) {
					return 0;
				} else {
					for (int n = 0; n < stringLen; n++) {
						if (input.charAt((i + n) % stringLen) != input
								.charAt((j + n) % stringLen)) {
							int result = input.charAt((i + n) % stringLen)
									- input.charAt((j + n) % stringLen);
							return result;
						}
					}

					return 0;
				}
			}
		});
	}

	public static void main(String[] args) {
		// unit testing of the methods (optional)
		CircularSuffixArray csa = new CircularSuffixArray("ABRACADABRA!");
		for (int i = 0; i < 12; i++)
			StdOut.println(csa.index(i));
	}
}