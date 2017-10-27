import com.atlassian.renderer.wysiwyg.converter.DefaultWysiwygConverter;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HTML2AtlassianMarkupTests {

    /**
     * Original test-case written for demo purposes by <a href="https://stackoverflow.com/users/981922/dokaspar">dokaspar</a>
     *
     * @see <a href="https://stackoverflow.com/a/37623940/1478636">original stackoverflow answer</a>
     */
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
        // We're including a horizontal-rule at the end of the HTML string.
        String htmlString = "This is <em>emphasized</em> and <b>bold</b> <hr>";
        // The <hr> will be filtered out by Jsoup's relaxed whitelist.
        htmlString = Jsoup.clean(htmlString, Whitelist.relaxed());
        DefaultWysiwygConverter converter = new DefaultWysiwygConverter();
        String wikiMarkupString = converter.convertXHtmlToWikiMarkup(htmlString);
        assertEquals("This is _emphasized_ and *bold*", wikiMarkupString);
    }
}
