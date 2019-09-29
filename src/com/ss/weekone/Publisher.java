package com.ss.weekone;

public class Publisher {

		private int id;
		private String name;
		private String address;
		
		public Publisher() {
			id = 0;
			name = "";
			address = "";
		}
		
		public Publisher(int pubId, String pubName, String pubAdd) {
			id = pubId;
			name = pubName;
			address = pubAdd;
		}
		
		
		public int getId() {
			return id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getAddress() {
			return address;
		}
		public void setAddress(String address) {
			this.address = address;
		}
}
