package com.example.service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.exception.LoaderException;
import com.example.exception.ReaderException;
import com.example.loader.Loader;
import com.example.model.Link;
import com.example.reader.Reader;
import com.example.task.LinkData;
import com.example.task.LinkExtractorTask;
import com.example.util.HtmlParser;

@Component
public class DataExtractorService {
	@Autowired
	private Reader reader;
	@Autowired
	private HtmlParser htmlParser;
	@Autowired
	private LinkData linkData;
	@Autowired
	private Loader loader;
	private Logger logger = LoggerFactory.getLogger(DataExtractorService.class);

	/**
	 * This is main method that initiate the process of link extraction from URL and
	 * load data to the destination.
	 * 
	 * @param source
	 */
	public void process(String source, String filePath) {
		logger.info("URL entered by user {}", source);
		try {
			Optional<String> htmlContent = reader.readContentFromSource(source);
			htmlContent.ifPresent(content -> {
				List<Link> filterLinks = htmlParser.parseLinks(htmlContent.get());
				useCompletableFutureWithExecutor(filterLinks);
			});
			loader.loadData(linkData, filePath);
		} catch (ReaderException e) {
			logger.error("Unable to read content from source {}", source);
		} catch (LoaderException e) {
			logger.error("Unable to write data in csv file for source {}", source);
		}
	}

	/**
	 * This method create the task Using CompletableFutures with a custom Executor
	 * and
	 * 
	 * @param tasks
	 */
	public void useCompletableFutureWithExecutor(List<Link> filterLinks) {
		List<LinkExtractorTask> tasks = filterLinks.parallelStream()
				.map(l -> new LinkExtractorTask(reader, htmlParser, l.getUrl(), linkData)).collect(Collectors.toList());
		long start = System.nanoTime();
		ExecutorService executor = Executors.newFixedThreadPool(100);
		List<CompletableFuture<Void>> futures = tasks.stream()
				.map(t -> CompletableFuture.runAsync(() -> t.run(), executor)).collect(Collectors.toList());
		futures.stream().map(CompletableFuture::join).collect(Collectors.toList());
		long duration = (System.nanoTime() - start) / 1_000_000_000;
		logger.info("Processed {} tasks in {} sec\n", tasks.size(), duration);
		executor.shutdown();
	}

}