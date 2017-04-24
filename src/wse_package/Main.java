package wse_package;

import java.io.IOException;

import article_list_parser.ParseWiki;

public class Main {
	public static void main(String[] args) throws IOException {
		//re-generate list of articles
		ParseWiki.main(null);
	}
}
