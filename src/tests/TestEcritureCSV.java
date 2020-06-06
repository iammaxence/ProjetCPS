package tests;

import java.io.FileNotFoundException;
import java.io.IOException;

import tests.EcritureCSV;

public class TestEcritureCSV {

	public static void main(String[] args) {
		try {
			
			EcritureCSV.Calculdutemps("Publisher", "quoi", 10);
			EcritureCSV.Calculdutemps("broker", "qui",System.currentTimeMillis());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
