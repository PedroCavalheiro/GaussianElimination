package matrix;

import static org.junit.Assert.*;

import org.junit.Test;

public class GaussTest {

	@Test
	public void testPivot() {
		String[][] a = new String[][] {
			{"1.09", "2.25", "2.50"},
			{"4.00", "1.00", "-2.01"},
			{"2.00", "6.50", "1.05"}
		};
		String[][] b = new String[][] {
			{"1.09", "2.25", "2.50"},
			{"2.00", "6.50", "1.05"},
			{"4.00", "1.00", "-2.01"}
		};
		String[][] c = new String[][] {
			{"4.00", "1.00", "-2.01"},
			{"1.09", "2.25", "2.50"},
			{"2.00", "6.50", "1.05"}
		};
		assertEquals(1, new Gauss(new SystemMatrix(a, 3)).pivot());
		assertEquals(2, new Gauss(new SystemMatrix(b, 3)).pivot());
		assertEquals(0, new Gauss(new SystemMatrix(c, 3)).pivot());
	}
	
	@Test
	public void testSwap() {
		Gauss a = new Gauss(new SystemMatrix(new String[][] {
			{"1.09", "2.25", "2.50"},
			{"4.00", "1.00", "-2.01"},
			{"2.00", "6.50", "1.05"}
		}, 3));
		Gauss b = new Gauss(new SystemMatrix(new String[][] {
			{"1.09", "2.25", "2.50"},
			{"2.00", "6.50", "1.05"},
			{"4.00", "1.00", "-2.01"}
		}, 3));
		assertEquals("L2 <-> L3", b.swap(1, 2));
		assertEquals(a.matrix.toString(), b.matrix.toString());
	}
	
	@Test
	public void testMultiplier() {
		Gauss a = new Gauss(new SystemMatrix(new String[][] {
			{"4.00", "1.00", "-2.01"},
			{"1.09", "2.25", "2.50"},
			{"2.00", "6.50", "1.05"}
		}, 3));
		assertEquals("0.273", a.multiplier(1).toString());
		assertEquals("0.5", a.multiplier(2).toString());
	}
	
	@Test
	public void testSubtract1() {
		Gauss a = new Gauss(new SystemMatrix(new String[][] {
			{"4.00", "1.00", "-2.01", "2.99"},
			{"1.09", "2.25", "2.50", "5.75"},
			{"2.00", "6.50", "1.05", "9.55"}
		}, 3));
		SystemMatrix result = new SystemMatrix(new String[][] {
			{"4.00", "1.00", "-2.01", "2.99"},
			{"0.00", "1.98", "3.05", "4.93"},
			{"2.00", "6.50", "1.05", "9.55"}
		}, 3);
		String s = "m21 = 0.273\r\n"
				+ "L2 -> L2 - (0.273)*L1";
		assertEquals(s, a.subtract(1, 0, a.multiplier(1)));
		assertEquals(result.toString(), a.matrix.toString());
	}
	
	@Test
	public void testSubtract2() {
		Gauss a = new Gauss(new SystemMatrix(new String[][] {
			{"3.00", "1.00"},
			{"1.00", "1.00"},
		}, 3));
		SystemMatrix result = new SystemMatrix(new String[][] {
			{"3.00", "1.00"},
			{"0.00", "0.667"}
		}, 3);
		String s = "m21 = 0.333\r\n"
				+ "L2 -> L2 - (0.333)*L1";
		assertEquals(s, a.subtract(1, 0, a.multiplier(1)));
		assertEquals(result.toString(), a.matrix.toString());
	}
	
	@Test
	public void testStep() {
		Gauss a = new Gauss(new SystemMatrix(new String[][] {
			{"1.09", "2.25", "2.50", "5.75"},
			{"4.00", "1.00", "-2.01", "2.99"},
			{"2.00", "6.50", "1.05", "9.55"}
		}, 3));
		String step1 = 
				  "Pivot: L2\r\n"
				+ "L1 <-> L2\r\n"
				+ " _                         _ \r\n"
				+ "|                   |       |\r\n"
				+ "| 4.00  1.00  -2.01 | 2.99  |\r\n"
				+ "| 1.09  2.25  2.50  | 5.75  |\r\n"
				+ "| 2.00  6.50  1.05  | 9.55  |\r\n"
				+ "|_                  |      _|\r\n"
				+ "\r\n"
				+ "m21 = 0.273\r\n"
				+ "L2 -> L2 - (0.273)*L1\r\n"
				+ "m31 = 0.5\r\n"
				+ "L3 -> L3 - (0.5)*L1\r\n"
				+ " _                         _ \r\n"
				+ "|                   |       |\r\n"
				+ "| 4.00  1.00  -2.01 | 2.99  |\r\n"
				+ "| 0.00  1.98  3.05  | 4.93  |\r\n"
				+ "| 0.00  6.00  2.06  | 8.05  |\r\n"
				+ "|_                  |      _|\r\n";
		assertEquals(step1, a.step());
		String step2 = 
				  "Pivot: L3\r\n"
				+ "L2 <-> L3\r\n"
				+ " _                         _ \r\n"
				+ "|                   |       |\r\n"
				+ "| 4.00  1.00  -2.01 | 2.99  |\r\n"
				+ "| 0.00  6.00  2.06  | 8.05  |\r\n"
				+ "| 0.00  1.98  3.05  | 4.93  |\r\n"
				+ "|_                  |      _|\r\n"
				+ "\r\n"
				+ "m32 = 0.33\r\n"
				+ "L3 -> L3 - (0.33)*L2\r\n"
				+ " _                         _ \r\n"
				+ "|                   |       |\r\n"
				+ "| 4.00  1.00  -2.01 | 2.99  |\r\n"
				+ "| 0.00  6.00  2.06  | 8.05  |\r\n"
				+ "| 0.00  0.00  2.37  | 2.27  |\r\n"
				+ "|_                  |      _|\r\n";
		assertEquals(step2, a.step());
	}

}
