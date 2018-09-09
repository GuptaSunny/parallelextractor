package com.example.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import com.example.model.Link;

/**
 * This class provided utility method to parse HTML content.
 * 
 * @author Sunny Gupta
 *
 */
@Component
public class HtmlParser {

	/**
	 * This method used to filter the href links from HTML.
	 * 
	 * @param html
	 * @return List
	 */
	public List<Link> parseLinks(Document doc) {
		List<Link> linkList = new ArrayList<>();
		try {
			linkList = doc.select("a").parallelStream()
					.filter(link -> !(link.attr("href").contains("#") || link.attr("href").isEmpty()))
					.map(link -> new Link(link.absUrl("href"), link.text())).collect(Collectors.toList());
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return linkList;
	}
}
