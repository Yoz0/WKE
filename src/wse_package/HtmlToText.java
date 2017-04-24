package wse_package;

import java.io.IOException;
import java.io.StringReader;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;

/**
 * @author sindarus
 * Class that implements a function to extract plain text from an html document
 */
public class HtmlToText {
	private static StringBuilder sb = new StringBuilder();	//output for html to plain text parser
	private static HTMLEditorKit.ParserCallback parserCallback = new HTMLEditorKit.ParserCallback() {
	    public boolean readyForNewline;

	    @Override
	    public void handleText(final char[] data, final int pos) {
	        String s = new String(data);
	        sb.append(s.trim());
	        sb.append(" ");
	        readyForNewline = true;
	    }

	    @Override
	    public void handleStartTag(final HTML.Tag t, final MutableAttributeSet a, final int pos) {
	        if (readyForNewline && (t == HTML.Tag.DIV || t == HTML.Tag.BR || t == HTML.Tag.P)) {
	            sb.append("\n");
	            readyForNewline = false;
	        }
	    }

	    @Override
	    public void handleSimpleTag(final HTML.Tag t, final MutableAttributeSet a, final int pos) {
	        handleStartTag(t, a, pos);
	    }
	};
	
	/**
	 * @param html : String in which the html document is stored
	 * @return A string containing plain text extracted from the html document
	 */
	public static String html_to_text(String html) throws IOException{
		sb.delete(0, sb.length());	//clear sb
		new ParserDelegator().parse(new StringReader(html), parserCallback, false);
		return new String(sb);
	}
}
