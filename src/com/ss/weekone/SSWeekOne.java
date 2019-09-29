/**
 * 
 */
package com.ss.weekone;

import java.util.Scanner;
/**
 * @author Tkat
 *
 */
public class SSWeekOne {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		CrudManager crud = new CrudManager();
		System.out.println("Welcome to my library!");
back:	while(true) {
			Scanner scan = new Scanner(System.in);			
			
			String responceOne;
				while(true) {
				System.out.println("Would you like to look at options for:");
				System.out.println("1)Authors");
				System.out.println("2)Books");
				System.out.println("3)Publishers");
				System.out.print("-> ");
				responceOne = scan.next();
				scan.nextLine();
				System.out.println();
				if(responceOne.equals("1") || responceOne.equals("2") || responceOne.equals("3")) {
					break;
				}
				System.out.println("Please only enter 1, 2 or 3!");
				}
			String current = responceOne.equals("1") ? "Author" : responceOne.equals("2") ? "Book" : "Publisher";
			
			String responceTwo;
			while(true) {
				System.out.println("Would you like to:");
				System.out.println("1)Add");
				System.out.println("2)View");
				System.out.println("3)Edit");
				System.out.println("4)Delete");
				System.out.println("5)Back");
				System.out.print("-> ");
				responceTwo = scan.next();
				System.out.println();
				if(responceTwo.equals("1") || responceTwo.equals("2") || responceTwo.equals("3") || responceTwo.equals("4") || responceTwo.equals("5")){
					break;
				}
				System.out.println("Please only enter 1, 2, 3, 4 or 5!");
			}
			scan.nextLine();
			String action = responceTwo.equals("1") ? "Create" : responceTwo.equals("2") ? "Read" : responceTwo.equals("3") ? "Update" : responceTwo.equals("4") ? "Delete" : "Back";

			switch(current) {
			case "Author":
				
				switch(action) {
				case "Create":
					
					crud.createAuthor(scan);
				
					break;//Author Create
					
				case "Read":
					
					crud.readAuthor();
					
					break;//Author Read
					
				case "Update":

					crud.updateAuthor(scan);
										
					break;//Author Update
					
				case "Delete":
					
					crud.deleteAuthor(scan);
		
					break;//Author Delete
				case "Back":
					
					continue back;//Back
				}
				
				break;//Author
				
			case "Book":
				
				switch(action) {
				case "Create":

					crud.createBook(scan);
					
					break;//Book Create
					
				case "Read":
					
					crud.readBook();
				
					break;//Book Read

				case "Update":
					
					crud.updateBook(scan);
					
					break;//Book Update
					
				case "Delete":
					
					crud.deleteBook(scan);
				
					break;//Book Delete
				case "Back":
					continue back;//Back
				}
				break;//Book
				
			case "Publisher":
				switch(action) {
				case "Create":
					
					crud.createPublisher(scan);
					
					break;//Publisher Create
					
				case "Read":
					
					crud.readPublisher();
					
					break;//Publisher Read

				case "Update":
					
					crud.updatePublisher(scan);
					
					break;//Publisher Update

				case "Delete":
					
					crud.deletePublisher(scan);
					
					break;//Publisher Delete
				case "Back":
					continue back;//Back
				}
				break;//Publisher
				
			}
			if(!crud.validateYesNo("Would you like to do anything else?", scan)) {
				System.out.println("Have a nice day!");
				scan.close();
				break back;
			}
		}
	}
}