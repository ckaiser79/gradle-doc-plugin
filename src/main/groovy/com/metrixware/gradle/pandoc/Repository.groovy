
package com.metrixware.gradle.pandoc

import org.gradle.api.*;
import org.gradle.api.file.*;
import org.gradle.api.tasks.*;

class Repository {

	@Input
	String name

	@Input
	String url
	
	Repository(String name){
		this.name = name
	}
	
	void setUrl(String url) {
		this.url = url
	}
	
	String getName() {
		return name
	}

	String getUrl() {
		return url
	}

	
	@Override
	String toString() {
		return url
	}
}
