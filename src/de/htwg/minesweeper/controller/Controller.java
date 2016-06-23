package de.htwg.minesweeper.controller;

import java.util.*;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.htwg.minesweeper.model.FieldTest;
import de.htwg.minesweeper.model.Model;
import util.observer.Observable;

public class Controller extends Observable{
	
	private static final Logger log = LogManager.getLogger();

	Model field;
	
	private int firstNumber;
	private int secondNumber;
	
	private int statusCode;
	private String FeldText[][]; 
	
	private long timestart;
	
	private int row = 10, column = 10, numberOfMines = 3;
	
	public Controller(){
		field = new Model(row, column, numberOfMines);
	}

	
	public void spielFeld() {
		setFeldText(field.getUserField());
	}
	
	
	
	public boolean startgame(String answer) {
		long timestart = System.nanoTime();
		int[] AnswerList = {};
		boolean gameNotlost = true;
		
		statusCode = 1;

		List<String> list = Arrays.asList(answer.split(","));
		
		if(list.size() == 2){
			AnswerList = stringToNumber(list);
			boolean ItsABomb = IsItaBomb(AnswerList[0],AnswerList[1]);
			
			if(ItsABomb){
				//iview.lostGame();
				statusCode = 2;
				gameNotlost = false;
			}
			
			else if(checkIfGameIsWon()){
				/*
				long timeEnd = System.nanoTime();
				long elapsedTime = timeEnd-timestart;
				long time = TimeUnit.SECONDS.convert(elapsedTime, TimeUnit.NANOSECONDS);
				System.out.println(time);
				*/
				statusCode = 3;
			}
			

		}
		else if(list.size() == 3){
			setFlag(list);
		}

		spielFeld();
		
		notifyObservers();

		return gameNotlost;
	}
	
	boolean checkIfGameIsWon(){
		boolean gameWon = false;
		System.out.println(field.getsizeOfxAndfWithBomb());
		System.out.println(field.getsizeOfxAndfWithoutBomb());
		if (field.getsizeOfxAndfWithBomb() == field.getsizeOfxAndfWithoutBomb() ){
			gameWon = true;
		}
		field.resetSizeOFBoMB();
		return gameWon;
	}
	
	int[] stringToNumber(List<String> list){
		int[] i;
		i = new int[2];
		try{
			firstNumber = Integer.parseInt(list.get(0));
			secondNumber = Integer.parseInt(list.get(1));
		}
		catch (ArrayIndexOutOfBoundsException ah ){
			log.error("You forgot to input the second coordinate!" + ah.getMessage());
		}
		i[0] = firstNumber;
		i[1] = secondNumber;
		return i;
	}
	
	void setFlag(List<String> flag){
		int numberi = Integer.parseInt(flag.get(0));
		int numberj = Integer.parseInt(flag.get(1));
		String testField = field.getUserFieldSimple(numberi, numberj);
		if(testField.equals("x"))
			field.setFlag(numberi, numberj);
	}
	
	boolean IsItaBomb(int i, int j){
		String[][] fi = field.getfilledField();
		if(fi[firstNumber][secondNumber].equals("b")){
			field.setUserFieldSimple(firstNumber, secondNumber, "b");
			return true;
		}
		field.setUserField(firstNumber,secondNumber);	
		return false;
	}
	
	
	
	
	
	
	public int getStatusText()
	{
		return statusCode;
	}

	
	
	public void exitGame(){
		Runtime.getRuntime().halt(0);
	}
	
	public int getRow(){
		return row;
	}
	
	public void setRow(int i){
		row = i;
	}

	public String[][] getFeldText(){
		return field.getUserField();
	}
	
	public void setFeldText(String feldText[][]) {
		FeldText = feldText;
	}
}
