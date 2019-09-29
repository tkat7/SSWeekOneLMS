package com.ss.weekone;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AuthorDAO {

	public List<Author> readAuthorsFile() throws IOException{
		String line = "";
		BufferedReader br = new BufferedReader(new FileReader("Authors.csv"));
		List<Author> authList = new ArrayList<>();
		while((line = br.readLine()) != null){
			String[] value = line.split(",");
			Author auth = new Author(Integer.parseInt(value[0]), value[1]);
			authList.add(auth);
		}
		br.close();
		return authList;
	}
	
	public void writeAuthorsFile(List<Author> authors) throws IOException{
		
			FileWriter csvWriter = new FileWriter("Authors.csv");
			for(Author a : authors) {
				csvWriter.append(a.getId() + "," + a.getName());
				csvWriter.append("\n");
			}
			csvWriter.close();
	}
	
}
