package com.example.reader;

import java.util.Optional;

import com.example.exception.ReaderException;

public interface Reader {
	Optional<String> readContentFromSource(String source) throws ReaderException;
}
