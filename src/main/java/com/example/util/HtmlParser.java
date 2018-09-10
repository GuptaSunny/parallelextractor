package com.example.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.example.model.Link;
import com.example.service.DataExtractorService;

/**
 * This class provided utility method to parse HTML content.
 * 
 * @author Sunny Gupta
 *
 */
@Component
public class HtmlParser {
	private Logger logger = LoggerFactory.getLogger(DataExtractorService.class);

	/**
	 * This method used to filter the href links from HTML.
	 * 
	 * @param html
	 * @return List
	 */
	public List<Link> parseLinks(String html) {
		List<Link> linkList = Collections.emptyList();
		try {
			linkList = Jsoup.parse(html, "https://en.wikipedia.org/").select("a").parallelStream()
					.filter(link -> !(link.attr("href").contains("#") || link.attr("href").isEmpty()))
					.map(link -> new Link(link.absUrl("href"), link.text())).collect(Collectors.toList());
		} catch (Exception e) {
			logger.error("unable tp parse document", e);
		}
		return linkList;
	}
}
