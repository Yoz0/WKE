package wse_package;

import java.util.regex.Pattern;

/**
 * @author sindarus
 * TODO : do not delete content inside templates and html tags
 * Should we take computed html as an input ? Should we take printable version ?
 */
public class WikitextToText {
	public static String Wikitext_to_text(String wikitext){
		String ret = new String(wikitext);					//copy wikitext into ret
		ret = ret.replaceAll("\n", " ");					//delete newlines
		ret = ret.replaceAll("\\|", " ");					//delete pipes
		
		//Things to delete, case insensitive
		String[] to_delete_i = {"=\\s*notes et références\\s*=.*", "=\\s*Voir aussi\\s*=.*",
								"=\\s*Articles connexes\\s*=.*", "=\\s*Lien externe\\s*=.*",
								"=\\s*Bibliographie\\s*=.*"};
		for(int i=0; i<to_delete_i.length; i++){
		     ret = Pattern.compile(to_delete_i[i], java.util.regex.Pattern.CASE_INSENSITIVE)
		    		 	  .matcher(ret).replaceAll("");
		}
		
		//delete templates
		ret = ret.replaceAll("\\{\\{[^\\{\\}]*?\\}\\}", "");
		ret = ret.replaceAll("\\{\\{[^\\{\\}]*?\\}\\}", "");
		ret = ret.replaceAll("\\{\\{[^\\{\\}]*?\\}\\}", "");
		//can delete up to 3-times-nested template
		
		//Things to delete	   all html tags       all templates
		String[] to_delete = {"<([A-Z][A-Z0-9]*)\b[^>]*>.*?</\1>",
							  "'''", "''", "\\[\\[", "\\]\\]", "=====", "====",
							  "====", "===", "==", "Catégorie:", "\\*", "\\[.*?\\]"};
		for(int i=0; i<to_delete.length; i++){
			ret = ret.replaceAll(to_delete[i], "");
		}
		
		//delete duplicate whitespace
		ret = ret.replaceAll("\\s+", " ");
		//delete first and last space
		ret = ret.replaceAll(" (.*) ", "$1");
		return ret;
	}
}
