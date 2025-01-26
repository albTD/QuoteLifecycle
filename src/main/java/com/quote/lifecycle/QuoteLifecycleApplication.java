package com.quote.lifecycle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.quote.lifecycle.models.Quote;

@SpringBootApplication
public class QuoteLifecycleApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuoteLifecycleApplication.class, args);
		
//		Quote quote=new Quote();
//		quote.addLineItem("line1");
//		quote.addLineItem("line2");
//		System.out.println(quote.toPDF());
//		quote.complete();
		
	}

}
