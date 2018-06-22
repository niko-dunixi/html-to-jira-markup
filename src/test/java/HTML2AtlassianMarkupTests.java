import com.atlassian.renderer.wysiwyg.converter.DefaultWysiwygConverter;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class HTML2AtlassianMarkupTests {

    /**
     * Original test-case written for demo purposes by <a href="https://stackoverflow.com/users/981922/dokaspar">dokaspar</a>
     *
     * @see <a href="https://stackoverflow.com/a/37623940/1478636">original stackoverflow answer</a>
     */
    @Test
    @DisplayName("Test HTML is Properly Converted to Atlassian Markup")
    public void testHtmlIsProperlyConverted() {
        String htmlString = "This is <em>emphasized</em> and <b>bold</b>";
        DefaultWysiwygConverter converter = new DefaultWysiwygConverter();
        String wikiMarkupString = converter.convertXHtmlToWikiMarkup(htmlString);
        assertEquals("This is _emphasized_ and *bold*", wikiMarkupString);
    }

    @Test
    @DisplayName("Test HTML is Properly Converted to Atlassian Markup - With JSOUP Cleaning")
    public void testHtmlIsProperlyConvertedAfterCleaned() {
        // We're including a horizontal-rule at the end of the HTML string.
        String htmlString = "This is <em>emphasized</em> and <b>bold</b> <hr>";
        // The <hr> will be filtered out by Jsoup's relaxed whitelist.
        htmlString = Jsoup.clean(htmlString, Whitelist.relaxed());
        DefaultWysiwygConverter converter = new DefaultWysiwygConverter();
        String wikiMarkupString = converter.convertXHtmlToWikiMarkup(htmlString);
        assertEquals("This is _emphasized_ and *bold*", wikiMarkupString);
    }

    /**
     * Written as a demo for the github issue:
     *
     * @see <a href=https://github.com/paul-nelson-baker/html-to-jira-markup/issues/2">Github Issue</a>
     */
    @Test
    @DisplayName("Test HTML without JSoup")
    public void testHtmlToMarkupWithoutJSoup() {
        String htmlString = "<table>\n" +
                "<tr>\n" +
                "  <th>First Name</th>\n" +
                "  <th>Last Name</th>\n" +
                "  <th>Age</th>\n" +
                "</tr>\n" +
                "<tr>\n" +
                "  <td>Jill</td>\n" +
                "  <td>Smith</td>\n" +
                "  <td>50</td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                "  <td>Eve</td>\n" +
                "  <td>Jackson</td>\n" +
                "  <td>94</td>\n" +
                "</tr>\n" +
                "<tr>\n" +
                "  <td>John</td>\n" +
                "  <td>Doe</td>\n" +
                "  <td>80</td>\n" +
                "</tr>\n" +
                "</table>";
        htmlString = Jsoup.clean(htmlString, Whitelist.relaxed());
        DefaultWysiwygConverter converter = new DefaultWysiwygConverter();
        String wikiMarkupString = converter.convertXHtmlToWikiMarkup(htmlString);
        assertEquals("|| First Name || Last Name || Age ||\n" +
                "| Jill | Smith | 50 |\n" +
                "| Eve | Jackson | 94 |\n" +
                "| John | Doe | 80 |", wikiMarkupString);
    }

    /**
     * Written as a demo for the reply on the atlassian formums:
     * @see <a href="https://community.atlassian.com/t5/Jira-questions/How-to-convert-html-to-Atlassian-markup-in-java-app/qaq-p/657726#M266074">Atlassian Forum Reply</a>
     */
    @Test
    @DisplayName("Test HTML with inline style")
    public void testHtmlWithInlineStyle() {
        DefaultWysiwygConverter converter = new DefaultWysiwygConverter();

        String htmlInlineStyleString = "<div><span style=\"font-style:italic;\">Test Italic</span></div>";
        String wikiMarkupInlineStyleString = converter.convertXHtmlToWikiMarkup(htmlInlineStyleString);

        String expected = "_Test Italic_";
        assertNotEquals(expected, wikiMarkupInlineStyleString, "Inline styles are not apparently supported, if this changes in the future this assertion will need to be updated.");

        String htmlITagString = "<div><i>Test Italic</i></div>";
        String wikiMarkupITagString = converter.convertXHtmlToWikiMarkup(htmlITagString);
        assertEquals(expected, wikiMarkupITagString);
    }
}
