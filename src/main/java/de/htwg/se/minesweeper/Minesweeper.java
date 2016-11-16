package de.htwg.se.minesweeper;

import java.util.Scanner;
import com.google.inject.Guice;
import com.google.inject.Injector;
import de.htwg.se.minesweeper.aview.gui.GUI;
import de.htwg.se.minesweeper.aview.tui.TUI;
import de.htwg.se.minesweeper.controller.impl.OldController;
import de.htwg.se.minesweeper.controller.OldIController;

public final class Minesweeper {
	
	private static Scanner scanner;
	private TUI tui;
	protected OldIController controller;
	private GUI gui;
	private static Minesweeper instance = null;

	private Minesweeper(){
		Injector inject = Guice.createInjector();
		controller = inject.getInstance(OldController.class);
		tui = new TUI(controller);
		gui = new GUI(controller);
		controller.setStatusCode(0);
		tui.printTui();
	}
	
	public static Minesweeper getInstance(){
		if(instance == null){
			instance = new Minesweeper();
		}
		return instance;
	}
	
	public TUI getTUI(){
		return tui;
	}
	
	public GUI getGUI(){
		return gui;
	}
	
	public OldIController getController(){
		return controller;
	}
	
	public static void main(final String[] args)
	{

		Minesweeper game = Minesweeper.getInstance();
		
		boolean continu = true;
		scanner = new Scanner(System.in);
		
		while(continu){
			continu = game.getTUI().answerOptions(scanner.next());
		}
	}

}