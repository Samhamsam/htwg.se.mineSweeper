package de.htwg.minesweeper.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ModelTest {
	Model model;
	Model model1;
	
	int row = 10;
	int column = 10;
	int mines = 10;
	
	@Before
	public void setUp() throws Exception {
		model = new Model(row, column, mines);
		model1 = new Model(row, column, 0);
	}

	@Test
	public void testModel() {
		assertEquals(row, model.getRow());
		assertEquals(column, model.getColumn());
		assertEquals(mines, model.getNumberOfMines());
	}

	@Test
	public void testUserField() {
		assertEquals(model.getUserHiddenField(),model.getUserField()[0][0]);
	}

	@Test
	public void testCountIfGameIsWon() {
		model.countIfGameIsWon();
		boolean test = model.getsizeOfxAndfWithBomb() == model.getsizeOfxAndfWithoutBomb();
		assertFalse(test);
	}

	@Test
	public void testSetUserField() {
		
		model1.setUserField(0, 0);
		
		String teString = model1.getUserFieldSimple(0, 0);
		
		assertEquals("0", teString);
		
	}
	@Test
	public void testopenAllBlanksI9() {
		
		model1.openAllBlanksI9(0, 0, "0");
		
		String teString = model1.getUserFieldSimple(9, 9);
		
		assertEquals("0", teString);
	}

	@Test
	public void testSetFlag() {
		model.setFlag(0, 0);
		String teString = model.getUserFieldSimple(0, 0);
		assertEquals("f", teString);
	}

	@Test
	public void testSetUserFieldSimple() {
		model.setUserFieldSimple(0, 0, "b");
		assertEquals("b", model.getUserFieldSimple(0, 0));
		
	}
	
	@Test
	public void setUserHiddenField() {
		model.setUserHiddenField("a");
		assertEquals("a", model.getUserHiddenField());
	}


	@Test
	public void testResetSizeOFBoMB() {
		model.countIfGameIsWon();
		model.resetSizeOFBoMB();
		assertEquals(0, model.getsizeOfxAndfWithBomb());
		assertEquals(0, model.getsizeOfxAndfWithoutBomb());
	}

}