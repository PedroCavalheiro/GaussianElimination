import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import matrix.Gauss;
import matrix.Solve;
import matrix.SystemMatrix;

public class Client {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		List<String[]> tempMatrix = new ArrayList<>();
		while (sc.hasNextLine()) {
			String line = sc.nextLine();
			if (line.equals(""))
				break;
			tempMatrix.add(line.split(" "));
		}
		String[][] in = tempMatrix.toArray(String[][]::new);
		SystemMatrix matrix = new SystemMatrix(in, 3);
		System.out.println(matrix);
		Gauss g = new Gauss(matrix);
		while (g.hasNext())
			System.out.println(g.step());
		Solve s = new Solve(g.matrix);
		System.out.println(s);
		System.out.println("");
		try {
			while (s.hasNext())
				System.out.println(s.step());
		} catch (ArithmeticException e) {/* Cannot be Solved*/}
		sc.close();
	}
}
