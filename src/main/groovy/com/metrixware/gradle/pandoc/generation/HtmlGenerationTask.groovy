package com.metrixware.gradle.pandoc.generation

import java.io.File

import org.apache.commons.io.FileUtils
import org.apache.commons.lang3.StringUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.metrixware.gradle.pandoc.Document
import com.metrixware.gradle.pandoc.Template
import com.metrixware.gradle.pandoc.postprocess.Latex2HtmlReferencesPostprocessor

import org.gradle.api.*;
import org.gradle.api.file.*;
import org.gradle.api.tasks.*;

class HtmlGenerationTask extends AbstractPandocGenerationTask{

	@Override
	protected boolean isSupported(Document doc, String output) {
		return 'html'.equals(output) && ('tex'.equals(doc.type)||'md'.equals(doc.type))
	}

	@Override
	protected String getPandocOutput(String output) {
		return 'html5'
	}

	@Input
	@Override
	protected int getTocDepth() {
		return 2
	}

	@Override
	protected void copyToSite(File folder, Document doc,
			Template template, String lang, String output) {
		def out =  getDocumentOutputFile(doc, template, lang, output)

		logger.debug('using tmp folder ' + folder)

		/* sources/example/en */
		project.copy {
			from(folder) {
				include 'images/**'
				include 'scripts/**'
				include 'css/**'
			}
			into getDocumentOutputFolder(doc, template, lang, output)
		}

		FileUtils.copyFile(getTempOutputDocument(doc, template, lang, output),out)
		def postprocess = new Latex2HtmlReferencesPostprocessor('utf-8')
		postprocess.process(out)
	}
}
