package tests;


import java.io.IOException;

import tests.EcritureCSV;


/**
 * This JUnit class test all the 
 * method of the class EcritureCSV
 * 
 * @author Group LAMA
 *
 */
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
