package matrix;

import java.math.BigDecimal;

public class Solve {
	Matrix m;
	int step;
	boolean mulPhase = false;
	boolean subPhase = true;
	final int rr; // result row
	protected static final String NEWLINE = System.getProperty("line.separator");
	
	public Solve(SystemMatrix m) {
		for (int i = 0; i < m.lines(); i++)
			for (int j = 0; j < i; j++)
				if (m.data[i][j].compareTo(BigDecimal.ZERO) != 0)
					throw new IllegalArgumentException();
		this.m = m;
		this.step = m.lines()-1;
		this.rr = m.columns()-1;
	}
	
	/**
	 * @return String representation of an entire step
	 */
	public String step() {
		StringBuilder sb = new StringBuilder();
		m.data[step][rr] = m.data[step][rr].divide(m.data[step][step], m.rule);
		m.data[step][step] = new BigDecimal("1."+"0".repeat(m.rule.getPrecision()-1), m.rule);
		sb.append(this).append(NEWLINE).append(NEWLINE);
		subPhase = false;
		step--;
		if (step >= 0) {
			mulPhase = true;
			sb.append(this).append(NEWLINE).append(NEWLINE);
			for (int i = step; i >= 0; i--) {
				m.data[i][step+1] = m.data[i][step+1].multiply(m.data[step+1][rr], m.rule);
			}
			mulPhase = false;
			sb.append(this).append(NEWLINE).append(NEWLINE);
			subPhase = true;
			for (int i = step; i >= 0; i--) {
				m.data[i][rr] = m.data[i][rr].subtract(m.data[i][step+1], m.rule);
				m.data[i][step+1] = new BigDecimal("0."+"0".repeat(m.rule.getPrecision()-1), m.rule);
			}
			sb.append(this).append(NEWLINE);
		}
		return sb.toString();
	}
	
	private static final char VAR = 'x';
	
	@Override
	public String toString() { // TODO: Simplify and revamp (remove dependence from subPhase and mulPhase, contemplate that in other functions)
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < m.lines(); i++) {
			for (int j = 0; j < m.columns()-1; j++) {
				if (m.data[i][j].compareTo(BigDecimal.ZERO) < 0)
					sb.append("- ");
				else {
					if (j == 0 || j <= i || j > step+1 || (j > step && subPhase))
						sb.append("  ");
					else
						sb.append("+ ");
				}
				if (m.data[i][j].compareTo(BigDecimal.ZERO) == 0)
					sb.append(" ".repeat((new StringBuilder().append(m.data[i][j].abs()).append(VAR).append(j+1).append(" ")).toString().length()));
				else {
					if (m.data[i][j].compareTo(BigDecimal.ONE) == 0 && !(mulPhase && j == step+1 && i != step+1))
						sb.append(" ".repeat(m.data[i][j].abs().toString().length())).append(VAR).append(j+1).append(" ");
					else if (mulPhase && j == step+1)
						sb.append(m.data[i][j].abs()).append("*(").append(m.data[j][rr]).append(") ");
					else if (j == step+1)
						sb.append(m.data[i][j].abs()).append("   ");
					else
						sb.append(m.data[i][j].abs()).append(VAR).append(j+1).append(" ");
				}
			}
			sb.append("= ").append(m.data[i][rr]).append(NEWLINE);
		}
		sb.append("___________________________________________");
		return sb.toString();
	}
	
	/**
	 * @return true if the system isn't solved
	 */
	public boolean hasNext() {
		return step >= 0;
	}
}
