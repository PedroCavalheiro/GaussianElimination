package matrix;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.stream.IntStream;

/**
 * Matrix composed of an array of numbers with limited precision <br>
 * Normalized String representation and automatic rounding.
 */
public class Matrix {
	BigDecimal[][] data;
	protected final MathContext rule;
	
	protected static final String NEWLINE = System.getProperty("line.separator");
	
	/**
	 * @param matrix - Matrix as string representation. Scientific Notation is supported
	 * @param precision - Mantissa size
	 * @param rounding - Rounding mode
	 */
	public Matrix(String[][] matrix, int precision, RoundingMode rounding) {
		this.rule = new MathContext(precision, rounding);
		this.data = new BigDecimal[matrix.length][matrix[0].length];
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				data[i][j] = new BigDecimal(matrix[i][j], this.rule);
			}
		}
	} 
	
	/**
	 * @param matrix - Matrix as string representation. Scientific Notation is supported
	 * @param precision - Mantissa size
	 */
	public Matrix(String[][] matrix, int precision) {this(matrix, precision, RoundingMode.HALF_UP);}
	
	/**
	 * @return Number of lines of this matrix
	 */
	public int lines() {
		return data.length;
	}
	
	/**
	 * @return Number of columns of this matrix
	 */
	public int columns() {
		return data[0].length;
	}
	
	/**
	 * @return Max string length of each column
	 */
	protected int[] colLength() {
		int[] result = new int[data[0].length];
		for (int i = 0; i < data.length; i++)
			for (int j = 0; j < data[i].length; j++)
				if (result[j] < data[i][j].toString().length())
					result[j] = data[i][j].toString().length();
		return result;
	}
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		int[] lengths = colLength();
		int max = IntStream.of(lengths).max().getAsInt(); // biggest number(as string)
		int charLength = max*(lengths.length)+lengths.length; // uses the max number for padding of smaller numbers
		result.append(" _").append(" ".repeat(charLength-1)).append("_ ").append(NEWLINE);
		result.append("|").append(" ".repeat(charLength+1)).append("|").append(NEWLINE);
		
		for (int i = 0; i < data.length; i++) {
			result.append("|");
			for (int j = 0; j < data[i].length; j++) {
				String leftpad = " ".repeat(1+(max-data[i][j].toString().length())/2);
				result.append(leftpad).append(data[i][j]);
				int length = max-data[i][j].toString().length();
				if (length != 0) {
					int odd = max-data[i][j].toString().length() % 2 == 0 ? 0 : 1;
					String rightpad = " ".repeat(odd + (max-data[i][j].toString().length())/2);
					result.append(rightpad);
				}
			}
			result.append(" |").append(NEWLINE);
		}
		result.append("|_").append(" ".repeat(charLength-1)).append("_|");
		return result.toString();
	}
}
