package com.example.task;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.exception.ReaderException;
import com.example.reader.Reader;
import com.example.util.HtmlParser;

public class LinkExtractorTask {
	@Autowired
	private Reader reader;
	@Autowired
	private HtmlParser htmlParser;
	private String source;
	@Autowired
	private LinkData linkData;
	private Logger logger = LoggerFactory.getLogger(LinkExtractorTask.class);
	public LinkExtractorTask(Reader reader, HtmlParser htmlParser, String source, LinkData linkData) {
		super();
		this.reader = reader;
		this.htmlParser = htmlParser;
		this.source = source;
		this.linkData = linkData;
	}

	public void run() {
		List<String> links = Collections.emptyList();
		try {
			Optional<String> htmlContent = reader.readContentFromSource(source);
			if (htmlContent.isPresent())
				links = htmlParser.parseLinks(htmlContent.get()).parallelStream().map(l -> l.getUrl())
						.collect(Collectors.toList());
			linkData.addAllData(links);
		} catch (ReaderException e) {
			logger.error("Unable to read content from source{}", source, e);
		}
	}
}
