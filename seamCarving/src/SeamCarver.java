import java.awt.Color;

public class SeamCarver {
	private Picture p;
	private Picture newPic;
	private int width;
	private int height;
	private int[][] energyMatrix;
	private int[][] edgeTo;
	private double[] distTo;
	private int[] seam;
	private boolean isEnergyMatrixTranspose;

	public SeamCarver(Picture picture) {
		// create a seam carver object based on the given picture
		this.p = new Picture(picture);
		this.width = p.width();
		this.height = p.height();
		this.seam = new int[height];
		this.energyMatrix = new int[width][height];
		this.edgeTo = new int[width][height];
		this.distTo = new double[width];
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++)
				energyMatrix[i][j] = (int) energy(i, j);
	}

	public Picture picture() {
		// current picture
		return p;
	}

	public int width() {
		// width of current picture
		return this.width;
	}

	public int height() {
		// height of current picture
		return this.height;
	}

	public double energy(int x, int y) {
		if (x < 0 || y < 0 || x > this.width - 1 || y > this.height - 1)
			throw new IndexOutOfBoundsException();
		// energy of pixel at column x and row y
		if (x == 0 || y == 0 || x == this.width - 1 || y == this.height - 1)
			return 195075.0;
		Color c1 = p.get(x + 1, y);
		Color c2 = p.get(x - 1, y);
		int dxsquare = (c1.getRed() - c2.getRed())
				* (c1.getRed() - c2.getRed()) + (c1.getBlue() - c2.getBlue())
				* (c1.getBlue() - c2.getBlue())
				+ (c1.getGreen() - c2.getGreen())
				* (c1.getGreen() - c2.getGreen());
		Color c3 = p.get(x, y + 1);
		Color c4 = p.get(x, y - 1);
		int dysquare = (c3.getRed() - c4.getRed())
				* (c3.getRed() - c4.getRed()) + (c3.getBlue() - c4.getBlue())
				* (c3.getBlue() - c4.getBlue())
				+ (c3.getGreen() - c4.getGreen())
				* (c3.getGreen() - c4.getGreen());
		return dxsquare + dysquare;
	}

	public int[] findHorizontalSeam() {
		// sequence of indices for horizontal seam
		if (height <= 2) return new int[this.height];
		if (!this.isEnergyMatrixTranspose) {
			this.transposeEnergymatrix();
			this.isEnergyMatrixTranspose = true;
		}
		int[] retSeam = this.findVerticalSeam();
		return retSeam;
	}

	public int[] findVerticalSeam() {
		// sequence of indices for vertical seam
		if (width <= 2) return new int[this.height];
		for (int i = 1; i < this.width - 1; i++) {
			distTo[i] = 195075.0 + this.energyMatrix[i][1];
			edgeTo[i][1] = i;
		}
		computeSeam();
		int[] retSeam = seam.clone();

		if (isEnergyMatrixTranspose) {
			transposeEnergymatrix();
			isEnergyMatrixTranspose = false;
		}

		return retSeam;
	}

	private void computeSeam() {
		for (int i = 1; i < this.height - 2; i++) {
			double[] temp = new double[this.width];
			for (int k = 0; k < this.width; k++)
				temp[k] = Double.POSITIVE_INFINITY;
			for (int j = 1; j < this.width - 1; j++) {
				relax(j, i, temp);
			}
			distTo = temp;
		}

		double minDistance = Double.POSITIVE_INFINITY;
		int col = -1;
		for (int i = 1; i < this.width - 1; i++) {
			if (distTo[i] < minDistance) {
				minDistance = distTo[i];
				col = i;
			}
		}

		int next = col;
		seam[height - 1] = next;
		for (int i = height - 2; i > 0; i--) {
			seam[i] = next;
			next = edgeTo[next][i];
		}
		seam[0] = next;
	}

	private void relax(int x, int y, double[] temp) {
		for (int w = x - 1; w <= x + 1; w++) {
			if (w > 0 && w < this.width - 1) {
				double distance = distTo[x] + energyMatrix[w][y + 1];
				if (temp[w] > distance) {
					temp[w] = distance;
					edgeTo[w][y + 1] = x;
				}
			}
		}

	}

	public void removeHorizontalSeam(int[] seam) {
		// remove horizontal seam from current picture
		if (seam == null)
			throw new NullPointerException();
		if (seam.length != this.width || this.height <= 1)
			throw new IllegalArgumentException();
		for (int i = 1; i < seam.length; i++)
			if (Math.abs(seam[i] - seam[i - 1]) > 1 || seam[i] < 0
					|| seam[i] > this.height - 1)
				throw new IllegalArgumentException();
		newPic = new Picture(this.width, this.height - 1);
		for (int w = 0; w < this.width; w++) {
			for (int h = seam[w]; h < this.height - 1; h++) {
				p.set(w, h, p.get(w, h + 1));
			}
		}
		for (int w = 0; w < this.width; w++) {
			for (int h = 0; h < this.height - 1; h++) {
				newPic.set(w, h, p.get(w, h));
			}
		}

		reinitialize();
	}

	public void removeVerticalSeam(int[] seam) {
		// remove vertical seam from current picture
		if (seam == null)
			throw new NullPointerException();
		if (seam.length != this.height || this.width <= 1)
			throw new IllegalArgumentException();
		for (int i = 1; i < seam.length; i++)
			if (Math.abs(seam[i] - seam[i - 1]) > 1 || seam[i] < 0
					|| seam[i] > this.width - 1)
				throw new IllegalArgumentException();
		newPic = new Picture(this.width - 1, this.height);
		for (int h = 0; h < this.height; h++) {
			for (int w = seam[h]; w < this.width - 1; w++) {
				p.set(w, h, p.get(w + 1, h));
			}
		}
		for (int h = 0; h < this.height; h++) {
			for (int w = 0; w < this.width - 1; w++) {
				newPic.set(w, h, p.get(w, h));
			}
		}

		reinitialize();
	}

	private void reinitialize() {
		this.p = newPic;
		this.width = newPic.width();
		this.height = newPic.height();
		this.energyMatrix = new int[width][height];
		for (int w = 0; w < width; w++)
			for (int h = 0; h < height; h++)
				energyMatrix[w][h] = (int) this.energy(w, h);
		this.distTo = new double[width];
		this.edgeTo = new int[width][height];
		this.seam = new int[height];
	}

	private void transposeEnergymatrix() {
		// TODO Auto-generated method stub
		int newHeight = this.width;
		int newWidth = this.height;
		double[] newDistTo = new double[newWidth];
		int[][] newEdgeTo = new int[newWidth][newHeight];
		int[][] newEnergy = new int[newWidth][newHeight];
		for (int i = 0; i < newHeight; i++)
			for (int j = 0; j < newWidth; j++)
				newEnergy[j][i] = energyMatrix[i][j];
		this.energyMatrix = newEnergy;
		this.distTo = newDistTo;
		this.edgeTo = newEdgeTo;
		this.seam = new int[newHeight];
		this.width = newWidth;
		this.height = newHeight;
	}

	public static void main(String args[]) {
		Picture p = new Picture(args[0]);
		SeamCarver sc = new SeamCarver(p);
		sc.removeVerticalSeam(sc.findVerticalSeam());
		sc.findVerticalSeam();
	}
}