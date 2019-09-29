package com.ss.weekone;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {

	public List<Book> readBooksFile() throws IOException{
		String line = "";
		BufferedReader br = new BufferedReader(new FileReader("Books.csv"));
		List<Book> bookList = new ArrayList<>();
		while((line = br.readLine()) != null){
			String[] value = line.split(",");
			Book book = new Book(Integer.parseInt(value[0]), value[1], Integer.parseInt(value[2]), Integer.parseInt(value[3]));
			bookList.add(book);
		}
		br.close();
		return bookList;
	}
	
	public void writeBooksFile(List<Book> books) throws IOException{
		
		FileWriter csvWriter = new FileWriter("Books.csv");
		for(Book b : books) {
			csvWriter.append(b.getId() + "," + b.getTitle() + "," + b.getAuthId() + "," + b.getPubId());
			csvWriter.append("\n");
		}
		csvWriter.close();
	}

}
