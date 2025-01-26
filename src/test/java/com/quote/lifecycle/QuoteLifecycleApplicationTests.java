package com.quote.lifecycle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.quote.lifecycle.enums.QuoteState;
import com.quote.lifecycle.models.Quote;

@SpringBootTest
class QuoteLifecycleApplicationTests {
	
	  private Quote quote;

	    @BeforeEach
	    public void setUp() {
	        quote = new Quote();
	    }

	    @Test
	    public void testInitialStateIsDraft() {
	        assertEquals(QuoteState.DRAFT, quote.getCurrentState());
	    }

	    @Test
	    public void testPublishWithLineItems() {
	        quote.addLineItem("Item 1");
	        quote.publish();
	        assertEquals(QuoteState.PUBLISHED, quote.getCurrentState());
	    }

	    @Test
	    public void testPublishWithoutLineItemsThrowsException() {
	        Exception exception = assertThrows(IllegalStateException.class, () -> quote.publish());
	        assertEquals("Quote must have at least one line item to be published.", exception.getMessage());
	    }

	    @Test
	    public void testCompleteFromPublishedState() {
	        quote.addLineItem("Item 1");
	        quote.publish();
	        quote.complete();
	        assertEquals(QuoteState.COMPLETED, quote.getCurrentState());
	    }

	    @Test
	    public void testCompleteFromNonPublishedStateThrowsException() {
	        Exception exception = assertThrows(IllegalStateException.class, () -> quote.complete());
	        assertEquals("Quote can only be completed from PUBLISHED state.", exception.getMessage());
	    }

	    @Test
	    public void testExpireFromPublishedState() {
	        quote.addLineItem("Item 1");
	        quote.publish();
	        quote.expire();
	        assertEquals(QuoteState.EXPIRED, quote.getCurrentState());
	    }

	    @Test
	    public void testExpireFromNonPublishedStateThrowsException() {
	        Exception exception = assertThrows(IllegalStateException.class, () -> quote.expire());
	        assertEquals("Quote can only expire from PUBLISHED state.", exception.getMessage());
	    }

	    @Test
	    public void testArchiveFromPublishedState() {
	        quote.addLineItem("Item 1");
	        quote.publish();
	        quote.archive();
	        assertEquals(QuoteState.ARCHIVED, quote.getCurrentState());
	    }
	    
	    @Test
	    public void testArchiveFromDraftState() {
	        quote.addLineItem("Item 1");
	        quote.archive();
	        assertEquals(QuoteState.ARCHIVED, quote.getCurrentState());
	    }
	    
	    @Test
	    public void testArchiveFromNonDraftAndNonPublishedState() {
	        quote.addLineItem("Item 1");
	        quote.publish();
	        quote.expire();
	        Exception exception = assertThrows(IllegalStateException.class, () ->  quote.archive());
	        assertEquals("Quote can only be archived from DRAFT or PUBLISHED states.", exception.getMessage());
	    }


	    @Test
	    public void testDeleteFromArchivedState() {
	        quote.addLineItem("Item 1");
	        quote.publish();
	        quote.archive();
	        quote.delete();
	        assertEquals(QuoteState.DELETED, quote.getCurrentState());
	    }

	    @Test
	    public void testDeleteFromNonArchivedStateThrowsException() {
	        Exception exception = assertThrows(IllegalStateException.class, () -> quote.delete());
	        assertEquals("Quote can only be deleted from ARCHIVED state.", exception.getMessage());
	    }

	    @Test
	    public void testGenerateHtmlRepresentation() {
	        quote.addLineItem("Item 1");
	        quote.publish();
	        String html = quote.toHTML();
	        assertTrue(html.contains("<h1>Quote</h1>"));
	        assertTrue(html.contains("<p>State: PUBLISHED</p>"));
	        assertTrue(html.contains("<li>Item 1</li>"));
	    }

	    @Test
	    public void testGeneratePdfRepresentation() {
	        quote.addLineItem("Item 1");
	        quote.publish();
	        String pdf = quote.toPDF();
	        assertTrue(pdf.contains("PDF representation of Quote: State: PUBLISHED"));
	        assertTrue(pdf.contains("Line Items:"));
	        assertTrue(pdf.contains("Item 1"));
	    }


}
