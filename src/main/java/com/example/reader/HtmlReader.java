package com.example.reader;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.example.exception.ReaderException;

/**
 * Convenience class for reading content from URL.
 * 
 * @author Sunny Gupta
 *
 */
@Component
public class HtmlReader implements Reader {
	private Logger logger = LoggerFactory.getLogger("HtmlReader");

	/**
	 * This method read the HTML content from the URL.
	 * 
	 * @throws IOException
	 * @throws ReaderException
	 */
	public Optional<String> readContentFromSource(String source) throws ReaderException {
		logger.info("Reading content from source {}", source);
		System.setProperty("https.proxyHost", "10.119.18.5");
		System.setProperty("https.proxyHost", "8080");
		try {
			URL url=new URL(source);
			Connection connection = Jsoup.connect(url.toString()).proxy("10.119.18.5", 8080) .timeout(60 * 1000);
			Connection.Response response = connection.execute();
			return Optional.of(response.body());
		} catch (Exception e) {
			logger.error("Invalid URL {}", source, e);
			throw new ReaderException(e);
		}
	}

}
