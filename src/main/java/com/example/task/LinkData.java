package com.example.task;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Component;

@Component
public class LinkData {

	ConcurrentMap<String, Integer> data = new ConcurrentHashMap<>();

	public ConcurrentMap<String, Integer> getData() {
		return data;
	}

	public int size() {
		return data.size();
	}

	public void addAllData(List<String> keys) {
		keys.parallelStream().forEach(key -> {
			data.putIfAbsent(key, 0);
			data.merge(key, 1, (a, b) -> a + b);
		});
	}

}
