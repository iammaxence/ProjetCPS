package tests;

import java.util.ArrayList;

import annexes.Client;

public class main {

	public static void main(String[] args) {
		ArrayList<Client> t1=new ArrayList<>();
		Client c2=new Client("in2", null);
		t1.add(new Client("in", null));
		t1.add(c2);
		t1.add(new Client("in3", null));
		System.out.println("Avant : "+t1.size());
		ArrayList<Client> t2= (ArrayList<Client>) t1.clone();
		t1.remove(c2);
		System.out.println("Apres t1 : "+t1.size());
		System.out.println("Apres t2: "+t2.size());

	}

}
