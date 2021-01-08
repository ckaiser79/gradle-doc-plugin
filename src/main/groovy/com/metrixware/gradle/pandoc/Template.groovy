
package com.metrixware.gradle.pandoc

import org.gradle.api.*;
import org.gradle.api.file.*;
import org.gradle.api.tasks.*;

class Template {
	@Input
	String name

	@Input
	String[] outputs=['html']

	Template(String name){
		this.name = name
	}

	String getName() {
		return name
	}

	void setOutputs(String[] outputs) {
		this.outputs = outputs
	}
	
	String[] getOutputs() {
		return outputs
	}

	@Override
	String toString() {
		return name
	}
}
