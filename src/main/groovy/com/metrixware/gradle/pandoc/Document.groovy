
package com.metrixware.gradle.pandoc

import groovy.lang.Closure

import java.util.Map

import org.gradle.api.*;
import org.gradle.api.file.*;
import org.gradle.api.tasks.*;

class Document {
	@Input
	String name

	@Input
	String type='md'

	@Input
	String[] languages=['en']

	@Input
	String[] templates=['all']
	
	@Input
	String[] includedSources = []

	Document(String name){
		this.name = name
	}

	String getName() {
		return name
	}

	String[] getLanguages() {
		return languages
	}

	boolean support(Template template ){
		if(templates.find {String t -> t.equals('all')}!=null){
			return true
		}
		def collected =  templates.findAll {String t -> template.name.equals(t)}
		def hasTemplate = !collected.isEmpty()
		return hasTemplate
	}

	String getType() {
		return type
	}

	String[] getTemplates() {
		return templates
	}

	String[] getIncludedSources() {
		return includedSources
	}

	@Override
	String toString() {
		return name
	}
}
