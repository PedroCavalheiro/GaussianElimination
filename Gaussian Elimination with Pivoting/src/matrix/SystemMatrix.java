package matrix;

import java.math.RoundingMode;
import java.util.stream.IntStream;

/**
 * Behaves exactly like {@link Matrix Matrix.class} but has a different
 * String representation.
 */
public class SystemMatrix extends Matrix {
	
	/**
	 * @param matrix - Matrix as string representation. Scientific Notation is supported
	 * @param precision - Mantissa size
	 * @param rounding - Rounding mode
	 * @see Matrix#Matrix(String[][], int, RoundingMode)
	 */
	public SystemMatrix(String[][] matrix, int precision, RoundingMode rounding) {
		super(matrix, precision, rounding);
	} 
	
	/**
	 * @param matrix - Matrix as string representation. Scientific Notation is supported
	 * @param precision - Mantissa size
	 * @see Matrix#Matrix(String[][], int)
	 */
	public SystemMatrix(String[][] matrix, int precision) {super(matrix, precision);}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		int[] lengths = colLength();
		int max = IntStream.of(lengths).max().getAsInt();
		int charLength = max*(lengths.length)+lengths.length + 2;
		result.append(" _").append(" ".repeat(charLength-1)).append("_ ").append(NEWLINE);
		result.append("|").append(" ".repeat(charLength-max-2));
		result.append("|").append(" ".repeat(max+2)).append("|").append(NEWLINE);
		for (int i = 0; i < data.length; i++) {
			result.append("|");
			for (int j = 0; j < data[i].length-1; j++) {
				String leftpad = " ".repeat(1+(max-data[i][j].toString().length())/2);
				result.append(leftpad).append(data[i][j]);
				int length = max-data[i][j].toString().length();
				if (length != 0) {
					int odd = max-data[i][j].toString().length() % 2 == 0 ? 0 : 1;
					String rightpad = " ".repeat(odd + (max-data[i][j].toString().length())/2);
					result.append(rightpad);
				}
			}
			result.append(" |");
			String leftpad = " ".repeat(1+(max-data[i][data[i].length-1].toString().length())/2);
			result.append(leftpad).append(data[i][data[i].length-1]);
			int length = max-data[i][data[i].length-1].toString().length();
			if (length != 0) {
				int odd = max-data[i][data[i].length-1].toString().length() % 2 == 0 ? 0 : 1;
				String rightpad = " ".repeat(odd + (max-data[i][data[i].length-1].toString().length())/2);
				result.append(rightpad);
			}
			result.append(" |").append(NEWLINE);
		}
		result.append("|_").append(" ".repeat(charLength-max-3));
		result.append("|").append(" ".repeat(max+1)).append("_|");
		return result.toString();
	}
}
