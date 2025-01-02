
public class FCCNode {
	int i, j, k, l;

	// constructor
	FCCNode(int i, int j, int k, int l) {
		this.i = i;
		this.j = j;
		this.k = k;
		this.l = l;
	}

	FCCNode(FCCNode q) {
		this(q.i, q.j, q.k, q.l);
	}

	public boolean equals(FCCNode q) {
		if (i == q.i && j == q.j && k == q.k && l == q.l)
			return true;
		else
			return false;
	}

	boolean isValid() {
		return (i + j + k + l) == 0;
	}

	FCCNode diff(FCCNode q) {
		return new FCCNode(this.i - q.i, this.j - q.j, this.k - q.k, this.l - q.l);
	}

	boolean isNeighbor(FCCNode q) {
		FCCNode d = this.diff(q);
		for (int i = 0; i < 12; i++) {
			if (d.equals(FCC.neighbors[i]))
				return true;
		}
		return false;
	}

	boolean coPlane(int type, FCCNode q) {
		FCCNode d = this.diff(q);
		switch (type) {
		case 1:
			return d.i == 0;
		case 2:
			return d.i + d.k == 0;
		case 3:
			return d.k == 0;
		case 4:
			return d.i + d.j == 0;
		default:
			// throw NoSuchTypeException();
		}
		return false;
	}

	double[] EuCoordinate() {
		FCCNode p = new FCCNode(this); // cron myself
		double[] a = new double[3];
		// i offset
		a[0] += p.i * FCC.offset[0][0];
		a[1] += p.i * FCC.offset[0][1];
		a[2] += p.i * FCC.offset[0][2];
		p = p.diff(FCC.multiply(p.i, FCC.neighbors[0]));
		// j offset
		a[0] += p.j * FCC.offset[1][0];
		a[1] += p.j * FCC.offset[1][1];
		a[2] += p.j * FCC.offset[1][2];
		p = p.diff(FCC.multiply(p.j, FCC.neighbors[1]));
		// k offset
		a[0] += p.k * FCC.offset[2][0];
		a[1] += p.k * FCC.offset[2][1];
		a[2] += p.k * FCC.offset[2][2];
		return a;
	}

}

class FCC {
	static final FCCNode[] neighbors = {
			// u, v, w
			new FCCNode(1, -1, 0, 0), new FCCNode(0, 1, -1, 0), new FCCNode(0, 0, 1, -1),
			// -u, -v, -w
			new FCCNode(-1, 1, 0, 0), new FCCNode(0, -1, 1, 0), new FCCNode(0, 0, -1, 1),
			// u+v, v+w, u-w
			new FCCNode(1, 0, -1, 0), new FCCNode(0, 1, 0, -1), new FCCNode(1, -1, -1, 1),
			// -u-v, -v-w, -u+w
			new FCCNode(-1, 0, 1, 0), new FCCNode(0, -1, 0, 1), new FCCNode(-1, 1, 1, -1) };

	static final double[][] offset = { { 2 * Math.sqrt(3) / 3, 0, 2 * Math.sqrt(6) / 3 }, { -Math.sqrt(3), 1, 0 },
			{ Math.sqrt(3), 1, 0 }, { -2 * Math.sqrt(3) / 3, 0, -2 * Math.sqrt(6) / 3 }, { Math.sqrt(3), -1, 0 },
			{ -Math.sqrt(3), -1, 0 }, { -Math.sqrt(3) / 3, 1, 2 * Math.sqrt(6) / 3 }, { 0, 2, 0 },
			{ -Math.sqrt(3) / 3, -1, 2 * Math.sqrt(6) / 3 }, { Math.sqrt(3) / 3, -1, -2 * Math.sqrt(6) / 3 },
			{ 0, -2, 0 }, { Math.sqrt(3) / 3, 1, -2 * Math.sqrt(6) / 3 } };

	static FCCNode multiply(int scalar, FCCNode p) {
		return new FCCNode(scalar * p.i, scalar * p.j, scalar * p.k, scalar * p.l);

	}
}
