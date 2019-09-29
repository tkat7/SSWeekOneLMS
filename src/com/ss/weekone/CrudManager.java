package com.ss.weekone;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CrudManager {
	
	public void createAuthor(Scanner scan) {
		
		List<Author> authors = readAuthorFile();
		List<Book> books = readBookFile();
		List<Publisher> publishers = readPublisherFile();
		List<List<String>> changes = new ArrayList<>();
		
		boolean newAuth = true;
		System.out.println("What is the new author's name?");
		String authName = scan.nextLine();
		System.out.println();
		
		//look through authors for existing one with the same name
		int authId = 0;
search:	for(Author auth : authors) {
			if(auth.getId() > authId) {
				authId = auth.getId();
			}
			//list all books that author wrote
			if(authName.equalsIgnoreCase(auth.getName())){
				System.out.println("There is already an author named " + authName + " who has written:");
				for(Book book : books) {
					if(book.getAuthId() == auth.getId()) {
						System.out.println(book.getTitle());
					}
				}
				System.out.println();
				if(validateYesNo("Is this the same author?", scan)) {
					newAuth = false;
					authId = auth.getId();
					break search;
				}else {
					break;
				}
			}
		}	
				
		//add the new author
		if(newAuth) {
			authId++;
			Author auth = new Author(authId, authName);
			authors.add(auth);
			update(changes, "Add", "Author", auth.getName());
		}
		
		//check to add any books the author wrote and to add who published them
		int bookNum;
		do {
			System.out.println("How many " + (newAuth ? "" : "new ") + "books has " + authName + " written: ");	
			System.out.print("-> ");
			while (!scan.hasNextInt()) {
		        System.out.println("Please only enter a number!");
		        scan.nextLine();
		    }
		    bookNum = scan.nextInt();
		    scan.nextLine();
		} while (bookNum < 0);
		
		System.out.println();
		for(int i = 0; i < bookNum; i++) {
			System.out.println("What is the title of book " + (i+1) + ": ");
			System.out.print("-> ");
			String bookTitle = scan.nextLine();
			System.out.println();
			System.out.println("Who published " + bookTitle + ": ");
			System.out.print("-> ");
			String bookPub = scan.nextLine();
			System.out.println();
			int pubId = 1;
			boolean exists = false;
			boolean multiplePubs = false;
			List<Publisher> matchingPublishers = new ArrayList<>();
			for(Publisher pub : publishers) {
				if(pub.getName().equalsIgnoreCase(bookPub)) {
					if(exists) {
						multiplePubs = true;
					}
					exists = true;
					pubId = pub.getId();
					matchingPublishers.add(pub);
				}
				if(pub.getId() > pubId) {
					pubId = pub.getId();
				}
			}
			
			//if there are multiple publishers with that name list them all with their published books
			if(multiplePubs) {
				System.out.println("There are multiple publishers with that name.");
				for(Publisher pub : matchingPublishers) {
					System.out.println(pub.getId() + (pub.getId() < 10 ? ")  " : ") ") + pub.getName());
					for(Book book : books) {
						if(book.getPubId() == pub.getId()) {
							System.out.println("    " + book.getTitle());
						}
					}
					System.out.println();
				}
				
				int selectedPubId;
				boolean validId = false;
				System.out.println("Please enter the ID of the Publisher who published this book: ");
				System.out.print("-> ");
				do {
					while (!scan.hasNextInt()) {
						System.out.println();
				        System.out.println("Please only enter a number: ");
				        System.out.print("-> ");
				        scan.nextLine();
				    }
				    selectedPubId = scan.nextInt();
				    scan.nextLine();
				    for(Publisher pub : matchingPublishers) {
				    	if(selectedPubId == pub.getId()) {
				    		validId = true;
				    		authId = selectedPubId;
				    		break;
				    	}
				    }
				    if(!validId) {
				    	System.out.println();
				    	System.out.println("Please enter a valid Publisher ID: ");
				    	System.out.print("-> ");
				    }
				} while (!validId);
			}

			System.out.println();
			if(!exists) {
				pubId++;
				System.out.println("What is the address for "+ bookPub +": ");
				System.out.print("-> ");
				String pubAdd = scan.nextLine();
				Publisher pub = new Publisher(pubId, bookPub, pubAdd);
				publishers.add(pub);
				update(changes, "Add", "Publisher", pub.getName());
			}

			int bookId=0;
			for(Book book : books)
			{
				if(book.getId() > bookId) {
					bookId = book.getId();
				}
			}
			//add book
			Book book = new Book(bookId+1, bookTitle, authId, pubId);
			books.add(book);
			update(changes, "Add", "Book", book.getTitle());
		}
		System.out.println();

		writeAuthorsFile(authors);
		writeBooksFile(books);
		writePublishersFile(publishers);
		
		reviewChanges(changes);
		//scan.close();
	}
	
	public void readAuthor() {
		
		List<Author> authors = readAuthorFile();
		int count = 1;
		for(final Author auth : authors) {
			System.out.print(count + (count < 10 ? ")  " : ") "));
			System.out.println(auth.getName());
			System.out.println();
			count++;
		}
	}
	
	public void updateAuthor(Scanner scan) {
		
		List<Author> authors = readAuthorFile();
		List<List<String>> changes = new ArrayList<>();
		
		for(final Author auth : authors) {
			System.out.print(auth.getId() + ") ");
			System.out.println(auth.getName());
			
		}
		System.out.println();
		int authId;
		boolean validId = false;
		Author selectedAuth = new Author();
		System.out.println("Please enter the ID of the Author you would like to edit: ");
		System.out.print("-> ");
		int index = -1;
		do {
			while (!scan.hasNextInt()) {
				System.out.println();
		        System.out.println("Please only enter a number: ");
		        System.out.print("-> ");
		        scan.nextLine();
		    }
		    authId = scan.nextInt();
		    scan.nextLine();
		    int count = 0;
		    for(Author auth : authors) {
		    	if(authId == auth.getId()) {
		    		validId = true;
		    		selectedAuth = new Author(auth.getId(), auth.getName());
		    		break;
		    	}
		    	count++;
		    }
		    if(!validId) {
		    	System.out.println();
		    	System.out.println("Please enter a valid Author ID: ");
		    	System.out.print("-> ");
		    }else {
		    	index = count;
		    }
		} while (!validId);
		
		String authName = selectedAuth.getName();
		System.out.println();
		System.out.println("You have selected " + authName + "!");
edit:	while(true) {
	
			System.out.println("What would you like their new name to be: ");
			System.out.print("-> ");
			String newAuthName = scan.nextLine();
			System.out.println();
			for(Author author: authors) {
				
				if(newAuthName.equalsIgnoreCase(author.getName())) {
					System.out.println("An author with this name already exists.");

					if(validateYesNo("Would you like to change it anyways?", scan)) {
						authName = newAuthName;
						break edit;
					}else {
						if(validateYesNo("Would you still like to edit the author's name?", scan)) {
							continue edit;
						}else {
							break edit;
						}
					}
				}
			}
		}	
		System.out.println();
		authors.get(index).setName(authName);
		update(changes, "Edit", "Author", authName);
		reviewChanges(changes);
		writeAuthorsFile(authors);
	}
	
	public void deleteAuthor(Scanner scan) {
		
		List<Author> authors = readAuthorFile();
		List<Book> books = readBookFile();
		List<Publisher> publishers = readPublisherFile();
		List<List<String>> changes = new ArrayList<>();
		
		for(final Author auth : authors) {
			System.out.print(auth.getId() + ") ");
			System.out.println(auth.getName());
			
		}
		int authId;
		boolean validId = false;
		Author selectedAuth = new Author();
		System.out.println();
		System.out.println("Please enter the ID of the Author you would like to delete: ");
		System.out.print("-> ");
		int index = -1;
		do {
			while (!scan.hasNextInt()) {
				System.out.println();
		        System.out.println("Please only enter a number: ");
		        System.out.print("-> ");
		        scan.nextLine();
		    }
		    authId = scan.nextInt();
		    scan.nextLine();
		    int count = 0;
		    for(Author auth : authors) {
		    	if(authId == auth.getId()) {
		    		validId = true;
		    		selectedAuth = new Author(auth.getId(), auth.getName());
		    		break;
		    	}
		    	count++;
		    }
		    if(!validId) {
		    	System.out.println();
		    	System.out.println("Please enter a valid Author ID: ");
		    	System.out.print("-> ");
		    }else {
		    	index = count;
		    }
		} while (!validId);
		System.out.println();
		System.out.println("You have selected " + selectedAuth.getName() + "!");
		System.out.println(selectedAuth.getName() + " wrote the following books:");
		
		//loop through books
		List<Book> selectedBooks = new ArrayList<>();
		List<Integer> bookIndex = new ArrayList<>();
		int count = 0;
		for(Book book : books) {
			if(book.getAuthId() == selectedAuth.getId()) {
				selectedBooks.add(book);
				System.out.println(book.getTitle());
				bookIndex.add(count);
			}
			count ++;
		}
		System.out.println();
		if(validateYesNo("Are you sure you want to delete " + selectedAuth.getName() + ":", scan)) {
		
			update(changes, "Delete", "Author", authors.get(index).getName());
			authors.remove(index);
			

			//remove books by author
			int counter = 0;
			for(int bIndex : bookIndex) {
				int pubId = books.get(bIndex-counter).getPubId();
				update(changes, "Delete", "Book", books.get(index).getTitle());
				books.remove(bIndex-counter);
				
				//remove publisher if there are no more books published by them
				boolean deletePub = true;
				for(Book book : books) {
					if (pubId == book.getPubId()){
						deletePub = false;
						break;
					}
				}
				if(deletePub) {
					int pubIndex = 0;
					for(Publisher pub : publishers) {
						if(pubId == pub.getId()) {
							break;
						}
						pubIndex++;
					}
					update(changes, "Delete", "Publisher", publishers.get(index).getName());
					publishers.remove(pubIndex);
				
				}
				counter++;
			}
		}
		
		System.out.println();
		reviewChanges(changes);
		writeAuthorsFile(authors);
		writeBooksFile(books);
		writePublishersFile(publishers);
		//scan.close();
	}
	
	public void createBook(Scanner scan) {

		List<Author> authors = readAuthorFile();
		List<Book> books = readBookFile();
		List<Publisher> publishers = readPublisherFile();
		List<List<String>> changes = new ArrayList<>();
				
		int bookId = 0;
		for(Book book : books) {
			if(book.getId() > bookId) {
				bookId = book.getId();
			}
		}
		bookId++;
		
		System.out.println("What is the title of the book: ");
		System.out.print("-> ");
		String bookTitle = scan.nextLine();
		System.out.println();		
					
		System.out.println("Who wrote " + bookTitle + ": ");
		System.out.print("-> ");
		String bookAuth = scan.nextLine();
		
		//see if author exists, if not add one
		int authId = 1;
		boolean authExists = false;
		boolean multipleAuths = false;
		List<Author> matchingAuthors = new ArrayList<>();
		for(Author auth : authors) {
			if(auth.getName().equalsIgnoreCase(bookAuth)) {
				if(authExists) {
					multipleAuths = true;
				}
				authExists = true;
				authId = auth.getId();
				matchingAuthors.add(auth);
			}
			if(auth.getId() > authId) {
				authId = auth.getId();
			}
		}
		
		//if multiple authors ask which one
		if(multipleAuths) {
			System.out.println("There are multiple authors with that name.");
			for(Author auth : matchingAuthors) {
				System.out.println(auth.getId() + (auth.getId() < 10 ? ")  " : ") ") + auth.getName());
				for(Book book : books) {
					if(book.getAuthId() == auth.getId()) {
						System.out.println("    " + book.getTitle());
					}
				}
				System.out.println();
			}
			
			int selectedAuthId;
			boolean validId = false;
			System.out.println("Please enter the ID of the Author who wrote this book: ");
			System.out.print("-> ");
			do {
				while (!scan.hasNextInt()) {
					System.out.println();
			        System.out.println("Please only enter a number: ");
			        System.out.print("-> ");
			        scan.nextLine();
			    }
			    selectedAuthId = scan.nextInt();
			    scan.nextLine();
			    for(Author auth : matchingAuthors) {
			    	if(selectedAuthId == auth.getId()) {
			    		validId = true;
			    		authId = selectedAuthId;
			    		break;
			    	}
			    }
			    if(!validId) {
			    	System.out.println();
			    	System.out.println("Please enter a valid Author ID: ");
			    	System.out.print("-> ");
			    }
			} while (!validId);
		}
		
		//if no author add an author
		if(!authExists) {
			authId++;
			Author auth = new Author(authId, bookAuth);
			authors.add(auth);
			update(changes, "Add", "Author", auth.getName());
		}
		
		//see if Publisher exists, if not add one
		System.out.println();
		System.out.println("Who published " + bookTitle + ": ");
		System.out.print("-> ");
		String bookPub = scan.nextLine();
		System.out.println();
		int pubId = 1;
		boolean pubExists = false;
		boolean multiplePubs = false;
		List<Publisher> matchingPublishers = new ArrayList<>();
		for(Publisher pub : publishers) {		
			if(pub.getName().equalsIgnoreCase(bookPub)) {
				if(pubExists) {
					multiplePubs = true;
				}
				pubExists = true;
				pubId = pub.getId();
				matchingPublishers.add(pub);
			}
			if(pub.getId() > pubId) {
				pubId = pub.getId();
			}
		}
		
		//if multiple publishers ask which one
		if(multiplePubs) {
			System.out.println("There are multiple publishers with that name.");
			for(Publisher pub : matchingPublishers) {
				System.out.println(pub.getId() + (pub.getId() < 10 ? ")  " : ") ") + pub.getName());
				for(Book book : books) {
					if(book.getPubId() == pub.getId()) {
						System.out.println("    " + book.getTitle());
					}
				}
				System.out.println();
			}
			
			int selectedPubId;
			boolean validId = false;
			System.out.println("Please enter the ID of the Publisher who published this book: ");
			System.out.print("-> ");
			do {
				while (!scan.hasNextInt()) {
					System.out.println();
					System.out.println("Please only enter a number: ");
			        System.out.print("-> ");
			        scan.nextLine();
			    }
			    selectedPubId = scan.nextInt();
			    scan.nextLine();
			    for(Publisher pub : matchingPublishers) {
			    	if(selectedPubId == pub.getId()) {
			    		validId = true;
			    		authId = selectedPubId;
			    		break;
			    	}
			    }
			    if(!validId) {
			    	System.out.println();
			    	System.out.println("Please enter a valid Publisher ID: ");
			    	System.out.print("-> ");
			    }
			} while (!validId);
		}
		System.out.println();
		if(!pubExists) {
			pubId++;
			System.out.println("What is the address for "+ bookPub +": ");
			System.out.print("-> ");
			String pubAdd = scan.nextLine();
			Publisher pub = new Publisher(pubId, bookPub, pubAdd);
			publishers.add(pub);
			update(changes, "Add", "Publisher", pub.getName());
		}
		
		//create new book
		Book book = new Book(bookId, bookTitle, authId, pubId);
		books.add(book);
		update(changes, "Add", "Book" ,book.getTitle());
		
		System.out.println();
		reviewChanges(changes);
		writeAuthorsFile(authors);
		writeBooksFile(books);
		writePublishersFile(publishers);
	}
	
	public void readBook() {
		
		
		List<Author> authors = readAuthorFile();
		List<Book> books = readBookFile();
		List<Publisher> publishers = readPublisherFile();
		int count = 1;
		for(final Book book : books) {
			System.out.print(count + ") ");
			System.out.println((count < 10 ? " Title: " : "Title: ") + book.getTitle());
			for(Author auth : authors) {
				if(auth.getId() == book.getAuthId()) {
					System.out.println("   Author: " + auth.getName());
				}
			}
			for(Publisher pub : publishers) {
				if(pub.getId() == book.getPubId()) {
					System.out.println("Publisher: " + pub.getName());
				}
			}
			System.out.println();
			count++;
		}
	}
	
	public void updateBook(Scanner scan) {
		
		List<Author> authors = readAuthorFile();
		List<Book> books = readBookFile();
		List<Publisher> publishers = readPublisherFile();
		List<List<String>> changes = new ArrayList<>();
		
		//loop through books and show them all
		for(Book book : books) {
			System.out.print(book.getId() + ") ");
			System.out.println((book.getId() < 10 ? " Title: " : "Title: ") + book.getTitle());
			for(Author auth : authors) {
				if(auth.getId() == book.getAuthId()) {
					System.out.println("   Author: " + auth.getName());
				}
			}
			for(Publisher pub : publishers) {
				if(pub.getId() == book.getPubId()) {
					System.out.println("Publisher: " + pub.getName());
				}
			}
		}
		int bookId;
		boolean validId = false;
		Book selectedBook = new Book();
		System.out.println("Please enter the ID of the Book you would like to edit: ");
		System.out.print("-> ");
		int index = -1;
		do {
			while (!scan.hasNextInt()) {
				System.out.println();
				System.out.println("Please only enter a number: ");
		        System.out.print("-> ");
		        scan.nextLine();
		    }
		    bookId = scan.nextInt();
		    scan.nextLine();
		    int count = 0;
		    for(Book book : books) {
		    	if(bookId == book.getId()) {
		    		validId = true;
		    		selectedBook = new Book(book.getId(), book.getTitle(), book.getAuthId(), book.getPubId());
		    		
		    		break;
		    	}
		    	count++;
		    }
		    if(!validId) {
		    	System.out.println();
		    	System.out.println("Please enter a valid Book ID: ");
		    	System.out.print("-> ");
		    }else {
		    	index = count;
		    }
		} while (!validId);
		
		System.out.println("You have selected " + selectedBook.getTitle() + "!");
		
		String bookTitle = selectedBook.getTitle();
		String bookAuth = "";
		String bookPub = "";
		
		//find the author and publisher of the selected book
		for(Author auth : authors) {
			if(auth.getId() == selectedBook.getAuthId())
			bookAuth = auth.getName();
		}
		
		for(Publisher pub : publishers) {
			if(pub.getId() == selectedBook.getPubId())
			bookPub = pub.getName();
		}
		
edit:	while(true) {
			//ask about editing the title
			System.out.println("Would you like to edit the title?");
			System.out.println("1)Yes");
			System.out.println("2)No");
			System.out.print("-> ");
			String responce = scan.next();
			scan.nextLine();
			if(responce.equals("1")){
				System.out.println();
				System.out.println("What would you like its new title to be: ");
				System.out.print("-> ");
				String newBookTitle = scan.nextLine();
				
				//check if book with that title already exists
				for(Book book : books) {
					
					if(newBookTitle.equalsIgnoreCase(book.getTitle())) {
						
						System.out.println("A book with this title already exists.");
						if(validateYesNo("Would you like to change it anyways?", scan)) {
							bookTitle = newBookTitle;
							break;
						}else {
							continue edit;
						}
					}	
				}
				break;
			}else if(responce.equals("2")) {
				break;
			}else {
				System.out.println("Please only enter 1 or 2!");
			}
		}
		//ask about editing the author
		System.out.println("The current author is " + bookAuth + ".");
		if(validateYesNo("Would you like to edit the author?", scan)) {
			System.out.println("Who would you like the new author to be: ");
			System.out.print("-> ");
			bookAuth = scan.nextLine();
			System.out.println();
		}
		//ask about editing the publisher
		System.out.println("The current publisher is " + bookPub + ".");
		if(validateYesNo("Would you like to edit the publisher?", scan)) {
			System.out.print("Who would you like the new publisher to be: ");
			System.out.print("-> ");
			bookPub = scan.nextLine();
			System.out.println();
		}
		
		int oldAuth = selectedBook.getAuthId();
		int authId = 1;
		
		//add author if new author doens't exist
		boolean authExists = false;
		boolean multipleAuths = false;
		List<Author> matchingAuthors = new ArrayList<>();
		for(Author auth : authors) {
			if(auth.getName().equalsIgnoreCase(bookAuth)) {
				if(authExists) {
					multipleAuths = true;
				}
				authExists = true;
				authId = auth.getId();
				matchingAuthors.add(auth);
			}
			if(auth.getId() > authId) {
				authId = auth.getId();
			}
		}
		//if there are multiple authors with the same name, display books by each and ask which one
		if(multipleAuths) {
			System.out.println("There are multiple authors with that name.");
			for(Author auth : matchingAuthors) {
				System.out.println(auth.getId() + (auth.getId() < 10 ? ")  " : ") ") + auth.getName());
				for(Book book : books) {
					if(book.getAuthId() == auth.getId()) {
						System.out.println("    " + book.getTitle());
					}
				}
			}
			
			int selectedAuthId;
			boolean validAuthId = false;
			System.out.println("Please enter the ID of the Author who wrote this book: ");
			System.out.print("-> ");
			do {
				while (!scan.hasNextInt()) {
					System.out.println();
			        System.out.println("Please only enter a number: ");
			        System.out.print("-> ");
			        scan.nextLine();
			    }
			    selectedAuthId = scan.nextInt();
			    scan.nextLine();
			    for(Author auth : matchingAuthors) {
			    	if(selectedAuthId == auth.getId()) {
			    		validAuthId = true;
			    		authId = selectedAuthId;
			    		break;
			    	}
			    }
			    if(!validAuthId) {
			    	System.out.println();
			    	System.out.println("Please enter a valid Author ID: ");
			    	System.out.print("-> ");
			    }
			} while (!validAuthId);
		}
		System.out.println();
		if(!authExists) {
			authId++;
			Author auth = new Author(authId, bookAuth);
			authors.add(auth);
			update(changes, "Add", "Author", auth.getName());
		}
		
		books.get(index).setAuthId(authId);
		
		//remove old author if no longer has any books
		boolean deleteAuth = true;
		for(Book book : books) {
			if (oldAuth == book.getAuthId()){
				deleteAuth = false;
				break;
			}
		}
		
		if(deleteAuth) {
			int authIndex = 0;
			for(Author auth : authors) {
				if(oldAuth == auth.getId()) {
					break;
				}
				authIndex++;
			}
			update(changes, "Delete", "Author", authors.get(authIndex).getName());
			authors.remove(authIndex);
		}
		
		int oldPub = selectedBook.getPubId();								
		int pubId = 1;

		//add publisher if new publisher doesn't exist
		boolean pubExists = false;
		boolean multiplePubs = false;
		List<Publisher> matchingPublishers = new ArrayList<>();
		for(Publisher pub : publishers) {
			if(pub.getName().equalsIgnoreCase(bookPub)) {
				if(pubExists) {
					multiplePubs = true;
				}
				pubExists = true;
				pubId = pub.getId();
				matchingPublishers.add(pub);
			}
			if(pub.getId() > pubId) {
				pubId = pub.getId();
			}
		}
		//if there are multiple publishers with the same name, display books by each and ask which one
		if(multiplePubs) {
			System.out.println("There are multiple publishers with that name.");
			for(Publisher pub : matchingPublishers) {
				System.out.println(pub.getId() + (pub.getId() < 10 ? ")  " : ") ") + pub.getName());
				for(Book book : books) {
					if(book.getPubId() == pub.getId()) {
						System.out.println("    " + book.getTitle());
					}
				}
			}
			
			int selectedPubId;
			boolean validPubId = false;
			System.out.println("Please enter the ID of the Publisher who published this book: ");
			System.out.print("-> ");
			do {
				while (!scan.hasNextInt()) {
					System.out.println();
			        System.out.println("Please only enter a number: ");
			        System.out.print("-> ");
			        scan.nextLine();
			    }
			    selectedPubId = scan.nextInt();
			    scan.nextLine();
			    for(Publisher pub : matchingPublishers) {
			    	if(selectedPubId == pub.getId()) {
			    		validPubId = true;
			    		authId = selectedPubId;
			    		break;
			    	}
			    }
			    if(!validPubId) {
			    	System.out.println();
			    	System.out.println("Please enter a valid Publisher ID: ");
			    	System.out.print("-> ");
			    }
			} while (!validPubId);
		}
		//add publisher if it doesn't exist
		if(!pubExists) {
			pubId++;
			System.out.println("What is the address for "+ bookPub +": ");
			System.out.print("-> ");
			String pubAdd = scan.nextLine();
			Publisher pub = new Publisher(pubId, bookPub, pubAdd);
			publishers.add(pub);
			update(changes, "Add", "Publisher", pub.getName());
			System.out.println();
		}
		books.get(index).setPubId(pubId);
		
		//remove old publisher if no longer has any books
		boolean deletePub = true;
		for(Book book : books) {
			if (oldPub == book.getPubId()){
				deletePub = false;
				break;
			}
		}
		if(deletePub) {
			int pubIndex = 0;
			for(Publisher pub : publishers) {
				if(oldPub == pub.getId()) {
					break;
				}
				pubIndex++;
			}
			update(changes, "Delete", "Publisher", publishers.get(pubIndex).getName());
			publishers.remove(pubIndex);
		}
										
		//edit book
		books.get(index).setTitle(bookTitle);
		update(changes, "Edit", "Book", books.get(index).getTitle());
		
		System.out.print("");
		reviewChanges(changes);
		writeAuthorsFile(authors);
		writeBooksFile(books);
		writePublishersFile(publishers);
		
	}
	
	public void deleteBook(Scanner scan) {
	
		List<Author> authors = readAuthorFile();
		List<Book> books = readBookFile();
		List<Publisher> publishers = readPublisherFile();
		List<List<String>> changes = new ArrayList<>();
		//display all books
		for(Book book : books) {
			System.out.print(book.getId() + ") ");
			System.out.println((book.getId() < 10 ? " Title: " : "Title: ") + book.getTitle());
			for(Author auth : authors) {
				if(auth.getId() == book.getAuthId()) {
					System.out.println("   Author: " + auth.getName());
				}
			}
			for(Publisher pub : publishers) {
				if(pub.getId() == book.getPubId()) {
					System.out.println("Publisher: " + pub.getName());
				}
			}
			System.out.println();
		}
		int bookId;
		boolean validId = false;
		Book selectedBook = new Book();
		System.out.println("Please enter the ID of the Book you would like to delete: ");
		System.out.print("-> ");
		int index = -1;
		do {
			while (!scan.hasNextInt()) {
				System.out.println();
		        System.out.println("Please only enter a number: ");
		        System.out.print("-> ");
		        scan.nextLine();
		    }
		    bookId = scan.nextInt();
		    scan.nextLine();
		    int count = 0;
		    for(Book book : books) {
		    	if(bookId == book.getId()) {
		    		validId = true;
		    		selectedBook = new Book(book.getId(), book.getTitle(), book.getAuthId(), book.getPubId());
		    
		    		break;
		    	}
		    	count++;
		    }
		    if(!validId) {
		    	System.out.println();
		    	System.out.println("Please enter a valid Book ID: ");
		    	System.out.print("-> ");
		    }else {
		    	index = count;
		    }
		} while (!validId);
		System.out.println();
		System.out.println("You have selected " + selectedBook.getTitle() + "!");
		
		if(validateYesNo("Are you sure you want to delete " + selectedBook.getTitle() + ":", scan)) {
			update(changes, "Delete", "Book", books.get(index).getTitle());
			books.remove(index);
			
			//remove author if there are no more books written by them
			int authId = selectedBook.getAuthId();
			boolean deleteAuth = true;
			for(Book book : books) {
				if (authId == book.getAuthId()){
					deleteAuth = false;
					break;
				}
			}
			if(deleteAuth) {
				int authIndex = 0;
				for(Author auth : authors) {
					if(authId == auth.getId()) {
						break;
					}
					authIndex++;
				}
				update(changes, "Delete", "Author", authors.get(authIndex).getName());
				authors.remove(authIndex);
			}
			
			//remove publisher if there are no more books published by them
			int pubId = selectedBook.getPubId();
			boolean deletePub = true;
			for(Book book : books) {
				if (pubId == book.getPubId()){
					deletePub = false;
					break;
				}
			}
			if(deletePub) {
				int pubIndex = 0;
				for(Publisher pub : publishers) {
					if(pubId == pub.getId()) {
						break;
					}
					pubIndex++;
				}
				update(changes, "Delete", "Publisher", publishers.get(pubIndex).getName());
				publishers.remove(pubIndex);
			}
		}
		
		System.out.println();
		reviewChanges(changes);
		writeAuthorsFile(authors);
		writeBooksFile(books);
		writePublishersFile(publishers);
	}
	
	public void createPublisher(Scanner scan) {
		
		List<Author> authors = readAuthorFile();
		List<Book> books = readBookFile();
		List<Publisher> publishers = readPublisherFile();
		List<List<String>> changes = new ArrayList<>();
		
		boolean newPub = true;
		System.out.println("What is the new publisher's name: ");
		System.out.print("-> ");
		String pubName = scan.nextLine();
		System.out.println();	
		
		//check if the publisher already exists
		int pubId = 0;
search:	for(Publisher pub : publishers) {
			if(pub.getId() > pubId) {
				pubId = pub.getId();
			}
			if(pubName.equalsIgnoreCase(pub.getName())){
				System.out.println("There is already a publisher named " + pubName + " who has published:");
				for(Book book : books) {
					if(book.getPubId() == pub.getId()) {
						System.out.println(book.getTitle());
					}
				}
				System.out.println();

				if(validateYesNo("Is this the same publisher?", scan)) {
					newPub = false;
					pubId = pub.getId();
					break search;
				}
			}
		}	
		
		if(newPub) {
			pubId++;
			System.out.println("What is " + pubName + "'s address: ");
			System.out.print("-> ");
			String pubAdd = scan.nextLine();
			System.out.println();
			Publisher pub = new Publisher(pubId, pubName, pubAdd);
			publishers.add(pub);
			update(changes, "Add", "Publisher", pub.getName());
		}
		
		//check to add any books the publisher published and to add who wrote them
		
		int bookNum;
		do {
			System.out.println("How many " + (newPub ? "" : "new") + "books has " + pubName + " published: ");	
			System.out.print("-> ");
			while (!scan.hasNextInt()) {
		    	System.out.println();
		        System.out.println("Please only enter a number!");
		        System.out.print("-> ");
		        scan.nextLine();
		    }
		    bookNum = scan.nextInt();
		    scan.nextLine();
		} while (bookNum < 0);
		
		
		System.out.println();
		for(int i = 0; i < bookNum; i++) {
			System.out.println("What is the title of book " + (i+1) + ": ");
			System.out.print("-> ");
			String bookTitle = scan.nextLine();
			System.out.println();
			System.out.println("Who wrote " + bookTitle + ": ");
			System.out.print("-> ");
			String bookAuth = scan.nextLine();
			System.out.println();
			int authId = 1;
			boolean exists = false;
			boolean multipleAuths = false;
			List<Author> matchingAuthors = new ArrayList<>();
			for(Author auth : authors) {
				if(auth.getName().equalsIgnoreCase(bookAuth)) {
					if(exists) {
						multipleAuths = true;
					}
					exists = true;
					authId = auth.getId();
					matchingAuthors.add(auth);
				}
				if(auth.getId() > authId) {
					authId = auth.getId();
				}
			}
			//if there are multiple authors with that name, display all books by them and ask which one
			if(multipleAuths) {
				System.out.println("There are multiple authors with that name.");
				for(Author auth : matchingAuthors) {
					System.out.println(auth.getId() + (auth.getId() < 10 ? ")  " : ") ") + auth.getName());
					for(Book book : books) {
						if(book.getAuthId() == auth.getId()) {
							System.out.println("    " + book.getTitle());
						}
					}
					System.out.println();
				}
				
				int selectedAuthId;
				boolean validId = false;
				System.out.println("Please enter the ID of the Author who wrote this book: ");
				System.out.print("-> ");
				do {
					while (!scan.hasNextInt()) {
						System.out.println();
				        System.out.println("Please only enter a number: ");
				        System.out.print("-> ");
				        scan.nextLine();
				    }
				    selectedAuthId = scan.nextInt();
				    scan.nextLine();
				    for(Author auth : matchingAuthors) {
				    	if(selectedAuthId == auth.getId()) {
				    		validId = true;
				    		authId = selectedAuthId;
				    		break;
				    	}
				    }
				    if(!validId) {
				    	System.out.println("-> ");
				    	System.out.println("Please enter a valid Author ID: ");
				    	System.out.print("-> ");
				    }
				} while (!validId);
			}
			System.out.println();
			if(!exists) {
				authId++;
				Author auth = new Author(authId, bookAuth);
				authors.add(auth);
				update(changes, "Add", "Author", auth.getName());
			}

			int bookId=0;
			for(Book book : books)
			{
				if(book.getId() > bookId) {
					bookId = book.getId();
				}
			}
			//add book
			Book book = new Book(bookId+1, bookTitle, authId, pubId);
			books.add(book);
			update(changes, "Add", "Book", book.getTitle());
		}
		writeAuthorsFile(authors);
		writeBooksFile(books);
		writePublishersFile(publishers);
	}
	
	public void readPublisher() {
	
		List<Publisher> publishers = readPublisherFile();
		int count = 1;
		for(final Publisher pub : publishers) {
			System.out.print(count + ") ");
			System.out.println((count < 10 ? " Name: " : "Name: ") + pub.getName());
			System.out.println(" Address: " + pub.getAddress());
			System.out.println();
			count++;
		}
	}
	
	public void updatePublisher(Scanner scan) {
		
		List<Publisher> publishers = readPublisherFile();
		List<List<String>> changes = new ArrayList<>();
	
		//loop through the publishers
		for(final Publisher pub : publishers) {
			System.out.print(pub.getId() + ") ");
			System.out.println((pub.getId() < 10 ? " Name: " : "Name: ") + pub.getName());
			System.out.println(" Address: " + pub.getAddress());
			System.out.println();
			
		}
		int pubId;
		boolean validId = false;
		Publisher selectedPub = new Publisher();
		System.out.println("Please enter the ID of the Publisher you would like to edit: ");
		System.out.print("->");
		int index = -1;
		do {
			while (!scan.hasNextInt()) {
				System.out.println();
		        System.out.println("Please only enter a number: ");
		        System.out.print("->");
		        scan.nextLine();
		    }
		    pubId = scan.nextInt();
		    scan.nextLine();
		    int count = 0;
		    for(Publisher pub : publishers) {
		    	if(pubId == pub.getId()) {
		    		validId = true;
		    		selectedPub = new Publisher(pub.getId(), pub.getName(), pub.getAddress());
		    		break;
		    	}
		    	count++;
		    }
		    if(!validId) {
		    	System.out.println();
		    	System.out.println("Please enter a valid Publisher ID: ");
		    	System.out.print("->");
		    }else {
		    	index = count;
		    }
		} while (!validId);
		
		System.out.println();
		System.out.println("You have selected " + selectedPub.getName() + "!");
		
		//check what they want to edit
		String pubName = selectedPub.getName();
		String pubAdd = selectedPub.getAddress();
edit:		while(true) {
			System.out.println("Would you like to edit the publisher's name?");
			System.out.println("1)Yes");
			System.out.println("2)No");
			System.out.print("->");
			String z = scan.next();
			scan.nextLine();
			System.out.println();
			if(z.equals("1")){
				System.out.println("What would you like their new name to be: ");
				System.out.print("->");
				String newPubName = scan.nextLine();
				System.out.println();
				
				for(Publisher publisher : publishers) {
					
					if(newPubName.equalsIgnoreCase(publisher.getName())) {
						System.out.println("A publisher with this name already exists.");
						if(validateYesNo("Would you like to change it anyways?", scan)) {
							pubName = newPubName;
							break;
						}else {
							continue edit;
						}
					}
				}
				
				break;
			}else if(z.equals("2")) {
				break;
			}else {
				System.out.println("Please only enter 1 or 2!");
			}
		}
		
		System.out.println("The current address is " + pubAdd + ".");		
		if(validateYesNo("Would you like to edit the address?", scan)){
			System.out.println("What would you like " + pubName + "'s new address to be: ");
			System.out.print("->");
			pubAdd = scan.nextLine();
		}
			
		publishers.get(index).setName(pubName);
		publishers.get(index).setAddress(pubAdd);
		update(changes, "Edit", "Publisher", publishers.get(index).getName());

		writePublishersFile(publishers);
	}

	public void deletePublisher(Scanner scan) {
	
		List<Author> authors = readAuthorFile();
		List<Book> books = readBookFile();
		List<Publisher> publishers = readPublisherFile();
		List<List<String>> changes = new ArrayList<>();
			
		//loop through all the publishers
		for(final Publisher pub : publishers) {
			System.out.print(pub.getId() + ") ");
			System.out.println((pub.getId() < 10 ? " Name: " : "Name: ") + pub.getName());
			System.out.println(" Address: " + pub.getAddress());
			System.out.println();
		}
		int pubId;
		boolean validId = false;
		Publisher selectedPub = new Publisher();
		System.out.println("Please enter the ID of the Publisher you would like to delete: ");
		System.out.print("->");
		int index = -1;
		do {
			while (!scan.hasNextInt()) {
				System.out.println();
		        System.out.println("Please only enter a number: ");
		        System.out.print("->");
		        scan.nextLine();
		    }
		    pubId = scan.nextInt();
		    scan.nextLine();
		    int count = 0;
		    for(Publisher pub : publishers) {
		    	if(pubId == pub.getId()) {
		    		validId = true;
		    		selectedPub = new Publisher(pub.getId(), pub.getName(), pub.getAddress());
		    		break;
		    	}
		    	count++;
		    }
		    if(!validId) {
		    	System.out.println();
		    	System.out.println("Please enter a valid Publisher ID: ");
		    	System.out.print("->");
		    }else {
		    	index = count;
		    }
		} while (!validId);
		
		System.out.println("You have selected " + selectedPub.getName() + "!");
		System.out.println(selectedPub.getName() + " published the following books:");
		
		//loop through books
		List<Book> selectedBooks = new ArrayList<>();
		List<Integer> bookIndex = new ArrayList<>();
		int count = 0;
		for(Book book : books) {
			if(book.getPubId() == selectedPub.getId()) {
				selectedBooks.add(book);
				System.out.println(book.getTitle());
				bookIndex.add(count);
			}
			count ++;
		}
		
		if(validateYesNo("Are you sure you want to delete " + selectedPub.getName() + ":", scan)){
			update(changes, "Delete", "Publisher", publishers.get(index).getName());
			publishers.remove(index);
			
			//remove books by publisher
			int counter = 0;
			for(int bIndex : bookIndex) {
				int authId = books.get(bIndex-counter).getAuthId();
				update(changes, "Delete", "Book", books.get(index).getTitle());
				books.remove(bIndex-counter);
				
				//remove author if there are no more books written by them
				boolean deleteAuth = true;
				for(Book book : books) {
					if (authId == book.getAuthId()){
						deleteAuth = false;
						break;
					}
				}
				if(deleteAuth) {
					int authIndex = 0;
					for(Author auth : authors) {
						if(authId == auth.getId()) {
							break;
						}
						authIndex++;
					}
					update(changes, "Delete", "Author", authors.get(index).getName());
					authors.remove(authIndex);
				}
				counter++;
			}
		}
		
		System.out.println();
		reviewChanges(changes);
		writeAuthorsFile(authors);
		writeBooksFile(books);
		writePublishersFile(publishers);
	}

	public void reviewChanges(List<List<String>> changes) {
		
		List<List<String>> add = new ArrayList<>();
		List<List<String>> edit = new ArrayList<>();
		List<List<String>> delete = new ArrayList<>();
		//add the change to the list of changes
		for(List<String> change : changes) {
			
			if(change.get(0).equals("Add")) {
				add.add(change);
			}else if(change.get(0).equals("Edit")) {
				edit.add(change);
			}else if(change.get(0).equals("Delete")) {
				delete.add(change);
			}
		}
		
		//display the changes sorted by the type of change
		System.out.println("You have made the following changes:");
		boolean first = true;
		for(List<String> a : add) {
			if(first) {
				first = false;
				System.out.println("  Additions:");
			}
			System.out.println("    " + a.get(1) + ": " + a.get(2));
		}
		first = true;
		for(List<String> e : edit) {
			if(first) {
				first = false;
				System.out.println("  Edits:");
			}
			System.out.println("    " + e.get(1) + ": " + e.get(2));
		}
		
		first = true;
		for(List<String> d : delete) {
			if(first) {
				first = false;
				System.out.println("  Deletions:");
			}
			System.out.println("    " + d.get(1) + ": " + d.get(2));
		}		
	}

	
	
	//Read Authors.csv and convert to a list of Author Objects
	
	public List<Author> readAuthorFile(){
		AuthorDAO auth = new AuthorDAO();
		List<Author> authList = new ArrayList<>();
		try {
			
			authList = auth.readAuthorsFile();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return authList;
	}
	
	//Read Book.csv and convert to a list of Book Objects
	public List<Book> readBookFile(){
		BookDAO book = new BookDAO();
		List<Book> bookList = new ArrayList<>();
		try {
			
			bookList = book.readBooksFile();
						
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bookList;
	}
	
	//Read Publishers.csv and convert to a list of Publisher Objects
	public List<Publisher> readPublisherFile(){
		PublisherDAO pub = new PublisherDAO();
		List<Publisher> pubList = new ArrayList<>();
		try {
			
			pubList = pub.readPublishersFile();
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pubList;
	}
	
	//Write to Authors.csv
	public void writeAuthorsFile(List<Author> authors) {
		AuthorDAO auth = new AuthorDAO();
		try {
			auth.writeAuthorsFile(authors);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//Write to Book.csv
	public void writeBooksFile(List<Book> books) {
		BookDAO book = new BookDAO();
		try {
			book.writeBooksFile(books);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//Write to Publishers.csv
	public void writePublishersFile(List<Publisher> publishers) {
		PublisherDAO pub = new PublisherDAO();
		try {
			pub.writePublishersFile(publishers);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public boolean validateYesNo(String question, Scanner scan) {
		
		//validate a yes no question
		while(true) {
			System.out.println(question);
			System.out.println("1)Yes");
			System.out.println("2)No");
			System.out.print("-> ");
			String responce = scan.next();
			scan.nextLine();
			System.out.println();
			if(responce.equals("1")){
				return true;
			}else if(responce.equals("2")) {
				return false;
			}else {
				System.out.println("Please only enter 1 or 2!");
			}
		}
	}
	
	public void update(List<List<String>> changes, String action, String obj, String name) {
		
		//create an update
		List<String> update = new ArrayList<>();
		update.add(action);
		update.add(obj);
		update.add(name);
		changes.add(update);
		
	}
	
}
