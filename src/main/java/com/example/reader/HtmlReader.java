package com.example.reader;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

/**
 * Convenience class for reading content from URL.
 * 
 * @author Sunny Gupta
 *
 */
@Component
public class HtmlReader implements Reader {

	public Optional<String> readContentFromSource(String source) {
		Optional<String> html = Optional.empty();
		try {
			URL url = new URL(source);
			Connection connection = Jsoup.connect(url.toString()).timeout(60 * 1000);
			Document document = connection.get();
			html = Optional.of(connection.get().html());
			System.out.println("processed URl:" + source);
		} catch (MalformedURLException me) {
			System.err.println(me.getMessage() + ":" + source);
		} catch (IOException e) {
			System.err.println(e.getMessage() + ":" + source);
		}
		return html;
	}

}
