package com.quote.lifecycle.models;

import java.util.ArrayList;
import java.util.List;

import com.quote.lifecycle.enums.QuoteState;

public class Quote {
	
    private QuoteState currentState;
    private List<String> lineItems;

    public Quote() {
        this.currentState = QuoteState.DRAFT;
        this.lineItems = new ArrayList<>();
    }

    public QuoteState getCurrentState() {
        return currentState;
    }

    public void addLineItem(String item) {
        lineItems.add(item);
    }   

    public void publish() {
    	
    	//To check whether it is in draft state
        if (currentState != QuoteState.DRAFT) {
            throw new IllegalStateException("Quote can only be published from DRAFT state.");
        }
        
        //To check if it meets the criteria
        if (reviewQuote(lineItems)==false) {
            throw new IllegalStateException("Quote must have at least one line item to be published.");
        }
        
        //At last state changes to published
        currentState = QuoteState.PUBLISHED;
    }

    public void complete() {
    	
    	//To check whether it is in published state
        if (currentState != QuoteState.PUBLISHED) {
            throw new IllegalStateException("Quote can only be completed from PUBLISHED state.");
        }
        
        //State changes to completed
        currentState = QuoteState.COMPLETED;
        
        //calling invoice method
        generateInvoice();
    }

    public void expire() {
    	
    	//To check whether it is in published state
        if (currentState != QuoteState.PUBLISHED) {
            throw new IllegalStateException("Quote can only expire from PUBLISHED state.");
        }
        
        //State changes to expired
        currentState = QuoteState.EXPIRED;
    }

    public void archive() {
    	
    	//To check whether it is in draft or published state
        if (currentState != QuoteState.DRAFT && currentState != QuoteState.PUBLISHED) {
            throw new IllegalStateException("Quote can only be archived from DRAFT or PUBLISHED states.");
        }
        
        //State changes to archived
        currentState = QuoteState.ARCHIVED;
    }

    public void delete() {
    	//To check whether it is in archived state
        if (currentState != QuoteState.ARCHIVED) {
            throw new IllegalStateException("Quote can only be deleted from ARCHIVED state.");
        }
        
      //State changes to deleted
        currentState = QuoteState.DELETED;
    }

    private void generateInvoice() {
        System.out.println("Generating invoice for the quote...");
    }

    public String toHTML() {
    	
    	if(reviewQuote(lineItems)==false) {
    		return "There is no line items in quote";
    	}
    	
        return "<html><body><h1>Quote</h1><p>State: " + currentState + "</p><ul>" + 
                lineItems.stream().map(item -> "<li>" + item + "</li>").reduce("", String::concat) + 
                "</ul></body></html>";
    }

    public String toPDF() {
    	
    	if(reviewQuote(lineItems)==false) {
    		return "There is no line items in quote";
    	}
    	
        return "=====================================\n"+"PDF representation of Quote: State: " + currentState + ", Line Items: " + lineItems.stream().map(item -> "\n" + item ).reduce("", String::concat)+"\n=====================================";
    }
    
    
    //To review whether quote can be published or not
    public boolean reviewQuote(List<String> lineItems) {
    	 if (lineItems.isEmpty()) {
    		 
    		 //lineItems is empty,it does not meet the criteria.So cannot be published,therefore returning FALSE
    		 System.out.println("Quote does not meet the criteria...");
             return false;
         }
    	 
    	 
    	 System.out.println("Quote meets the criteria...");
    	 return true;
    }
    
    

}
