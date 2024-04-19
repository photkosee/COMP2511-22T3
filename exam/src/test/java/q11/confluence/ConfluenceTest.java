package q11.confluence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Iterator;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class ConfluenceTest {
    @Nested
    public class RegressionTests {
        @Test
        public void testIntegration() {
            ConfluencePage page = new ConfluencePage(); // status = editing
            ConfluenceAuthor author = new ConfluenceAuthor("Carl");
            ConfluenceAuthor author2 = new ConfluenceAuthor("Webster");
            ConfluenceAuthor author3 = new ConfluenceAuthor("Noa");

            page.interact("This is my confluence page", author);
            
            page.updateStatus("Viewing"); // status = viewing the draft
            String result = page.interact("", author2);
            assertOutputEquals("==== Draft in Progress ===  26 characters long  ============================= This is my confluence page", result);

            page.updateStatus("Editing"); // status = editing
            page.interact("Now the content has been updated!", author3);

            page.updateStatus("Publishing"); // status = publishing
            page.interact("Document title", author2);

            page.updateStatus("Published");
            result = page.interact("", author3);
            assertOutputEquals("==== Document title ===  By Carl, Noa ============================= Now the content has been updated!", result);
        }

        @Test
        public void testEditWithEmptyContent() {
            ConfluencePage page = new ConfluencePage();
            ConfluenceAuthor author = new ConfluenceAuthor("Carl");
            assertThrows(IllegalArgumentException.class, () -> page.interact("", author));
        }

        @Test
        public void testPublishingWithEmptyTitle() {
            ConfluencePage page = new ConfluencePage();
            ConfluenceAuthor author = new ConfluenceAuthor("Carl");
            page.updateStatus("Publishing");
            assertThrows(IllegalArgumentException.class, () -> page.interact("", author));
        }


        @Test
        public void testPublishingWithTitleTooLong() {
            ConfluencePage page = new ConfluencePage();
            ConfluenceAuthor author = new ConfluenceAuthor("Carl");
            page.updateStatus("Publishing");
            assertThrows(IllegalArgumentException.class, () -> page.interact("t".repeat(55), author));
        }

    }

    @Nested
    public class PartDConfluenceSpaceTest {
        @Test
        public void testSpaces() {
            ConfluenceSpace spaceComp2511 = new ConfluenceSpace();
            ConfluenceSpace space22t3 = new ConfluenceSpace();
            ConfluencePage courseOutline = new ConfluencePage();
            ConfluencePage coursePhilosophy = new ConfluencePage();
            ConfluencePage examSpec = new ConfluencePage();

            spaceComp2511.addSubnode(space22t3);
            spaceComp2511.addSubnode(courseOutline);
            spaceComp2511.addSubnode(coursePhilosophy);
            space22t3.addSubnode(examSpec);

            assertEquals(1, space22t3.getNumPages());
            assertEquals(3, spaceComp2511.getNumPages());
        }
    }

    @Nested
    public class PartEIteratorTest {
        @Test
        public void testIterator() {
            ConfluenceSpace spaceComp2511 = new ConfluenceSpace();
            ConfluenceSpace space22t3 = new ConfluenceSpace();
            ConfluencePage courseOutline = new ConfluencePage();
            ConfluencePage coursePhilosophy = new ConfluencePage();
            ConfluencePage examSpec = new ConfluencePage();
            ConfluencePage examSuppSpec = new ConfluencePage();

            spaceComp2511.addSubnode(courseOutline);
            spaceComp2511.addSubnode(coursePhilosophy);
            spaceComp2511.addSubnode(space22t3);
            space22t3.addSubnode(examSpec);
            space22t3.addSubnode(examSuppSpec);

            Iterator<ConfluencePage> iter = spaceComp2511.iterator();
            assertEquals(courseOutline, iter.next());
            assertEquals(coursePhilosophy, iter.next());
            assertEquals(examSpec, iter.next());
            assertEquals(examSuppSpec, iter.next());
            assertFalse(iter.hasNext());
        }
    }

    public void assertOutputEquals(String expected, String actual) {
        assertEquals(expected, actual.replace("\n", " "));
    }

}