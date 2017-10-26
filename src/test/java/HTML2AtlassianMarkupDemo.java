import com.atlassian.renderer.wysiwyg.converter.DefaultWysiwygConverter;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HTML2AtlassianMarkupDemo {

    @Test
    @DisplayName("Test HTML is Properly Converted to Atlassian Markup")
    public void testHTMLIsProperlyConverted() {
        String htmlString = "This is <em>emphasized</em> and <b>bold</b>";
        DefaultWysiwygConverter converter = new DefaultWysiwygConverter();
        String wikiMarkupString = converter.convertXHtmlToWikiMarkup(htmlString);
        assertEquals("This is _emphasized_ and *bold*", wikiMarkupString);
    }

    @Test
    @DisplayName("Test HTML is Properly Converted to Atlassian Markup - With JSOUP Cleaning")
    public void testHTMLIsProperlyConvertedAfterCleaned() {
        String htmlString = "This is <em>emphasized</em> and <b>bold</b>";
        htmlString = Jsoup.clean(htmlString, Whitelist.relaxed());
        DefaultWysiwygConverter converter = new DefaultWysiwygConverter();
        String wikiMarkupString = converter.convertXHtmlToWikiMarkup(htmlString);
        assertEquals("This is _emphasized_ and *bold*", wikiMarkupString);
    }
}
