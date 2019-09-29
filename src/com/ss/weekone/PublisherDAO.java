package com.ss.weekone;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PublisherDAO {

	public List<Publisher> readPublishersFile() throws IOException{
		String line = "";
		BufferedReader br = new BufferedReader(new FileReader("Publishers.csv"));
		List<Publisher> pubList = new ArrayList<>();
		while((line = br.readLine()) != null){
			String[] value = line.split(",");
			Publisher pub = new Publisher(Integer.parseInt(value[0]), value[1], value[2]);
			pubList.add(pub);
		}
		br.close();
		return pubList;
	}
	
	public void writePublishersFile(List<Publisher> publishers) throws IOException{
		
		FileWriter csvWriter = new FileWriter("Publishers.csv");
		for(Publisher p : publishers) {
			csvWriter.append(p.getId() + "," + p.getName() + "," + p.getAddress());
			csvWriter.append("\n");
		}
		csvWriter.close();
	}
	
}
