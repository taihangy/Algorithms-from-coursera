import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {

	public static void main(String[] args) {
		// unit testing
		// test constructor
		Deque<Integer> d = new Deque<Integer>();
		StdOut.println("Test constructor:" + d.toString());

		// test isEmpty()
		StdOut.println("\nTest isEmpty() it should be true: " + d.isEmpty());
		// test size()
		StdOut.println("\nTest size() it should be 0: " + d.size());
		// test addFirst
		d.addFirst(1);
		StdOut.println("\nTest addFirst(): " + d.toString()
				+ "size should be 1: " + d.size());
		d.addFirst(2);
		d.addFirst(3);
		d.addFirst(4);
		d.addFirst(5);
		d.addFirst(6);
		d.addFirst(7);
		d.addFirst(8);
		StdOut.println("\nTest addFirst(): " + d.toString()
				+ "size should be 8: " + d.size());

		// test addLast
		d.addLast(1);
		StdOut.println("\nTest addLst(): " + d.toString()
				+ "size should be 9: " + d.size());
		d.addLast(2);
		d.addLast(3);
		d.addLast(4);
		d.addLast(5);
		d.addLast(6);
		d.addLast(7);
		d.addLast(8);
		StdOut.println("\nTest addLast(): " + d.toString()
				+ "size should be 16: " + d.size());

		// test removeFisrt
		Deque<Integer> d1 = new Deque<Integer>();
		// d1.removeFirst();
		d.removeFirst();
		StdOut.println("\nTest removeFirst(), it should be[ 7 6 5 4 3 2 1 1 2 3 4 5 6 7 8 ]:\n\t\t"
				+ d.toString() + "size should be 15: " + d.size());

		// test removeLast
		Deque<Integer> d2 = new Deque<Integer>();
		// d2.removeFirst();
		d.removeLast();
		StdOut.println("\nTest removeLast(), it should be[ 7 6 5 4 3 2 1 1 2 3 4 5 6 7 ]:\n\t\t"
				+ d.toString() + "size should be 14: " + d.size());

		// test iterator()
		for (Integer i : d) {
			StdOut.println(i);
		}
		// d2.iterator().remove();
		// d2.iterator().next();
	}

	private int size;
	private Node sentinel;

	private class Node {
		Item item;
		Node next;
		Node prev;
	}

	public Deque() {
		// construct an empty deque
		this.size = 0;
		this.sentinel = new Node();
		this.sentinel.item = null;
		this.sentinel.next = sentinel;
		this.sentinel.prev = sentinel;
	}

	public boolean isEmpty() {
		// is the deque empty?
		return size == 0;
	}

	public int size() {
		// return the number of items on the deque
		return this.size;
	}

	public void addFirst(Item item) throws NullPointerException {
		// insert the item at the front
		if (item == null)
			throw new NullPointerException();
		if (isEmpty()) {
			Node first = new Node();
			first.item = item;
			first.next = sentinel;
			first.prev = sentinel;
			sentinel.next = first;
			sentinel.prev = first;
			size++;
		} else {
			Node oldFirst = sentinel.next;
			Node first = new Node();
			first.item = item;
			first.next = oldFirst;
			first.prev = sentinel;
			sentinel.next = first;
			oldFirst.prev = first;
			size++;
		}
	}

	public void addLast(Item item) throws NullPointerException {
		// insert the item at the end
		if (item == null)
			throw new NullPointerException();
		if (isEmpty()) {
			Node first = new Node();
			first.item = item;
			first.next = sentinel;
			first.prev = sentinel;
			sentinel.next = first;
			sentinel.prev = first;
			size++;
		} else {
			Node oldLast = sentinel.prev;
			Node last = new Node();
			last.item = item;
			last.next = sentinel;
			last.prev = oldLast;
			sentinel.prev = last;
			oldLast.next = last;
			size++;
		}
	}

	public Item removeFirst() throws java.util.NoSuchElementException {
		// delete and return the item at the front
		if (isEmpty())
			throw new java.util.NoSuchElementException();
		if (this.size == 1) {
			Node first = sentinel.next;
			sentinel.next = sentinel;
			sentinel.prev = sentinel;
			size--;
			return first.item;
		} else {
			Node first = sentinel.next;
			sentinel.next = first.next;
			first.next.prev = sentinel;
			first.next = null;
			first.prev = null;
			size--;
			return first.item;
		}
	}

	public Item removeLast() throws java.util.NoSuchElementException {
		// delete and return the item at the end
		if (isEmpty())
			throw new java.util.NoSuchElementException();
		if (this.size == 1) {
			Node last = sentinel.prev;
			sentinel.next = sentinel;
			sentinel.prev = sentinel;
			size--;
			return last.item;
		} else {
			Node last = sentinel.prev;
			sentinel.prev = last.prev;
			last.prev.next = sentinel;
			last.prev = null;
			last.next = null;
			size--;
			return last.item;
		}
	}

	public Iterator<Item> iterator() {
		// return an iterator over items in order from front to end
		return new front2EndIterator();
	}

	private class front2EndIterator implements Iterator<Item> {
		private Node current = sentinel;

		@Override
		public boolean hasNext() {
			return current.next != sentinel;
		}

		@Override
		public Item next() throws java.util.NoSuchElementException {
			if (!hasNext())
				throw new java.util.NoSuchElementException();
			Item item = current.next.item;
			current = current.next;
			return item;
		}

		@Override
		public void remove() throws UnsupportedOperationException {
			throw new UnsupportedOperationException("unsupported!");
		}

	}

	/*public String toString() {
		String result = "[ ";
		Node cur = sentinel;
		Item item;
		while (cur.next != sentinel) {
			item = cur.next.item;
			result = result + item.toString() + " ";
			cur = cur.next;
		}
		result = result + "]";
		return result;
	}*/

}