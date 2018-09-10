package com.example.loader;

import java.io.IOException;

import com.example.exception.LoaderException;
import com.example.task.LinkData;

public interface Loader {

	void loadData(LinkData linkData, String path) throws LoaderException;
}
