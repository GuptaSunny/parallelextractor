package com.example.service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

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
	@Value("${file.path}")
	private String filePath;

	public void process(String source) {
		System.out.println("URL entered by User=" + source);
		Optional<String> htmlContent = reader.readContentFromSource(source);
		htmlContent.ifPresent(content -> {
			List<Link> filterLinks = htmlParser.parseLinks(htmlContent.get());
			useCompletableFutureWithExecutor(filterLinks.subList(0, 1000));
		});
		loader.loadData(linkData, filePath);
	}

	/**
	 * Approach : Using CompletableFutures with a custom Executor
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
		List<Void> result = futures.stream().map(CompletableFuture::join).collect(Collectors.toList());
		long duration = (System.nanoTime() - start) / 1_000_000_000;
		System.out.printf("Processed %d tasks in %d sec\n", tasks.size(), duration);
		executor.shutdown();
	}

}