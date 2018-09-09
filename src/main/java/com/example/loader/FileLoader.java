package com.example.loader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Component;

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

	@Override
	public void loadData(LinkData linkData,String path) {
		ConcurrentMap<String, Integer> data = linkData.getData();
		Path mOutputPath = Paths.get(path);
		try {
			Files.write(mOutputPath, () -> data.entrySet().stream()
					.<CharSequence>map(e -> e.getKey() + DATA_SEPARATOR + e.getValue()).iterator());
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}

	}

}
