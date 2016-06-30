package de.htwg.minesweeper.aview.tui;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.htwg.minesweeper.controller.IController;
import util.observer.Event;
import util.observer.IObserver;


public class TUI implements IObserver {
	private static final Logger LOGGER = LogManager.getLogger();
	
	
	private IController controller;
	
	
	public TUI(IController controller){
		this.controller = controller;
		controller.addObserver(this);
	}
	

	public boolean answerOptions(String answer){
		boolean continu = true;
		switch(answer){
			case "q":
				continu = false;
				controller.exitGame();
			break;
			
			case "n":
				controller.newGame();
			break;
			
			case "h":
				controller.hilfe();
			break;
			
			
			default:
				controller.startGame(answer);
		}
		return continu;
	}

	@Override
	public void update(Event e) {
		printTui();
	}

	
	public StringBuilder printField(String[][] field){
		StringBuilder result = new StringBuilder();
		for(int j = 0; j < controller.getRow(); j++){
			for(int i= 0; i < controller.getColumn(); i++){
				result.append(field[i][j]).append(" ");
			}
			result.append("\n");
		}
		return result;
	}


	public void printTui() {
		int status = controller.getStatusCode();
		
		StringBuilder stringBuilder;
		
		stringBuilder = printField(controller.getFeldText());
		
		if(!"".equals(controller.getFieldPosition()))
			LOGGER.info("You typed: " + controller.getFieldPosition() + "\n");
		
		LOGGER.info(stringBuilder.toString());
		
		if(status == 1 || status == 0)
			LOGGER.info("Type: x,x | x is a number between 0 and 9(row, column):\n");
		if(status == 2)
			LOGGER.info("You Lost!");
		if(status == 3){
			LOGGER.info("You Won! " + controller.getTimeWon() + " Points!");
		}
		if(status == 4){
			LOGGER.info(controller.getHelpText());
		}
		if(status == 2 || status == 3)
			LOGGER.info("New Game? Type: n");
	}
}	
