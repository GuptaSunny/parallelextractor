package com.example.loader;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.example.exception.LoaderException;
import com.example.task.LinkData;

/**
 * This class load the data in the mentioned file path.
 * 
 * @author Sunny Gupta
 *
 */
@Component
public class FileLoader implements Loader {

	private static final String DATA_SEPARATOR = ",";
	private Logger logger = LoggerFactory.getLogger("FileLoader");

	@Override
	public void loadData(LinkData linkData, String path) throws LoaderException {
		ConcurrentMap<String, Integer> data = linkData.getData();
		try {
			Path outputPath = Paths.get(path);
			Files.write(outputPath, () -> data.entrySet().stream()
					.<CharSequence>map(e -> e.getKey() + DATA_SEPARATOR + e.getValue()).iterator());
		} catch (IOException e) {
			logger.error("Unable to write data in csv file ", e);
			throw new LoaderException(e.getMessage(), e);
		}

	}

}
