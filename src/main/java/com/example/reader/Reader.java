package com.example.reader;

import java.util.Optional;

public interface Reader {
	Optional<String> readContentFromSource(String source) ;
}
