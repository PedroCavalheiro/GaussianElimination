package matrix;

import java.math.BigDecimal;

/**
 * Gaussian Elimination with pivoting and finite precision. <br>
 * Uses the supplied matrix's precision and rounding mode. <br>
 * Throws {@link ArithmeticException ArithmeticException.class} if the matrix is linearly dependent.
 */
public class Gauss {
	public final SystemMatrix matrix;
	private int line = 0;
	
	public Gauss(SystemMatrix matrix) {
		this.matrix = matrix;
	}
	
	/**
	 * The pivot line is, at a certain step N, the line
	 * with the largest non zero number in the column N. <br>
	 * Pivoting reduces finite precision errors.
	 * 
	 * @return The pivot line of a matrix.
	 */
	public int pivot() {
		int result = getLine();
		BigDecimal max = null;
		for (int i = getLine(); i < matrix.lines(); i++) {
			if (max == null || (
				matrix.data[i][getLine()].compareTo(max) >= 1 && 
				matrix.data[i][getLine()].compareTo(BigDecimal.ZERO) != 0)) {
				max = matrix.data[i][getLine()];
				result = i;
			}
		}
		if (max.compareTo(BigDecimal.ZERO) == 0)
			throw new ArithmeticException("Pivot could not be found");
		return result;
	}
	
	/**
	 * Swaps provided lines
	 * @param lineA
	 * @param lineB
	 * @return String representation of the swap (LA <-> LB)
	 */
	public String swap(int lineA, int lineB) {
		StringBuilder sb = new StringBuilder();
		sb.append("L").append(lineA+1).append(" <-> L").append(lineB+1);
		BigDecimal[] m = matrix.data[lineA];
		matrix.data[lineA] = matrix.data[lineB];
		matrix.data[lineB] = m;
		return sb.toString();
	}
	
	/**
	 * @param targetLine
	 * @return Target Line's pivot column divided by the pivot line's pivot column.
	 */
	public BigDecimal multiplier(int targetLine) {
		BigDecimal pivot = matrix.data[getLine()][getLine()];
		return matrix.data[targetLine][getLine()].divide(pivot, matrix.rule);
	}
	
	/**
	 * @param lineA
	 * @param lineB
	 * @param m - Multiplier
	 * @return String representation of LineA - m*LineB
	 */
	public String subtract(int lineA, int lineB, BigDecimal m) {
		StringBuilder sb = new StringBuilder();
		sb.append("m").append(lineA+1).append(lineB+1).append(" = ").append(m).append(NEWLINE);
		sb.append("L").append(lineA+1).append(" -> ")
		  .append("L").append(lineA+1).append(" - (").append(m).append(")*L").append(lineB+1);
		for (int i = 0; i < matrix.data[lineA].length; i++) {
			matrix.data[lineA][i] = matrix.data[lineA][i].subtract(
				matrix.data[lineB][i].multiply(m, matrix.rule), matrix.rule);
			if (matrix.data[lineA][i].compareTo(BigDecimal.ZERO) == 0)
				matrix.data[lineA][i] = new BigDecimal("0."+"0".repeat(matrix.rule.getPrecision()-1), matrix.rule);
		}
		matrix.data[lineA][getLine()] = new BigDecimal("0."+"0".repeat(matrix.rule.getPrecision()-1), matrix.rule);
		return sb.toString();
	}
	
	protected static final String NEWLINE = System.getProperty("line.separator");
	
	/**
	 * @return String representation of an entire step (Pivoting, swapping, subtracting)
	 */
	public String step() {
		StringBuilder sb = new StringBuilder();
		int pivot = pivot();
		sb.append("Pivot: L").append(pivot+1).append(NEWLINE);
		if (pivot != getLine()) { // Swap lines
			sb.append(swap(getLine(), pivot)).append(NEWLINE);
			sb.append(this).append(NEWLINE).append(NEWLINE);
		}
		for (int i = getLine()+1; i < this.matrix.lines(); i++) {
			BigDecimal m = multiplier(i);
			sb.append(subtract(i, getLine(), m)).append(NEWLINE);
		}
		line++;
		sb.append(this).append(NEWLINE);
		return sb.toString();
	}
	
	@Override
	public String toString() {
		return this.matrix.toString();
	}

	/**
	 * @return Next line to perform Gaussian Elimination on
	 */
	public int getLine() {
		return line;
	}
	
	/**
	 * @return true if the matrix isn't in row echelon form
	 */
	public boolean hasNext() {
		return line != matrix.lines()-1;
	}
}
