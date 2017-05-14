package wse_package;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import article_list_parser.ParseWiki;

public class Main {
	public static void main(String[] args) throws IOException {
		int n = 0;
		Boolean done = false;
		Boolean goodInput = false;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String s = "";
		System.out.println("Welcome to the <insert_name_here> search engine for Wikipedia");
		while(!done){
			System.out.println("What would you like to do ?");
			System.out.println("0 - Quit this program");
			System.out.println("1 - Analyse wikipedia to build the search engine.");
			System.out.println("2 - Search wikipedia");
			System.out.println("Waiting for input.");
			goodInput = false;
			while(!goodInput){
				System.out.println("Enter a number: ");
				try {
					s = br.readLine();
				} catch (IOException e) {
					System.out.println("IOException while reading user input : " + e.getMessage());
				}
				try {
					n = Integer.parseInt(s); // Scans the input line as an int.	
				} catch (NumberFormatException e) {
					System.out.println("Could not interpret your input as a number.");
					continue;
				}
				if(n < 0 || n > 2){
					System.out.println("Option number out of range.");
					continue;
				}
				goodInput = true;
			}
			switch (n) {
				case 0:
					done = true;
					break;
				case 1:
					buildEngine();
					break;
				case 2:
					queryInterface();
					break;
				default:
					break;
			}
		}
		br.close();
	}
	
	private static void buildEngine(){
		//Gathering list of articles
		System.out.println("Gathering list of articles");
		try {
			ParseWiki.getListArticles();
		} catch (IOException e) {
			System.out.println("IOException : " + e.getMessage());
		}
		
		//TODO: Complete this when other classes are done
	}
	
	private static void queryInterface(){
		System.out.println("Enter your query.");
		//TODO: Complete this when other classes are done
	}
}
