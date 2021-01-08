package com.metrixware.gradle.pandoc.generation

import org.apache.commons.io.FileUtils
import org.apache.commons.lang3.StringUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.metrixware.gradle.pandoc.Document
import com.metrixware.gradle.pandoc.Template

import org.gradle.api.*;
import org.gradle.api.file.*;
import org.gradle.api.tasks.*;

abstract class AbstractPandocGenerationTask extends AbstractGenerationTask{
	protected static final Logger LOGGER = LoggerFactory.getLogger('pandoc-generation')
	
	@Override
	protected void process(File folder, Document doc,
			Template template, String lang, String output) {

		def input = getTempSourceDocument(doc, template, lang, output)
		
		boolean mainFileExists = input.exists() && !input.text.isEmpty()

		if(doc.includedSources.length > 0 && mainFileExists) {
			LOGGER.warn("includedSources is used, but main files exists. Ignore includedSources.")
		}
		else {
			def tmpOutputDir = getTempOutputFolder(doc, template, lang, output)		
			writeMainSourceFile(doc, tmpOutputDir, input)			
		}

		
		def tmpOut = getTempOutputDocument(doc, template, lang, output)
		def generateCmdLine = [
			project.documentation.panDocBin,
			'--write='+getPandocOutput(output),
			'--toc',
			'--toc-depth=' + getTocDepth(),
			'--section-divs',
			'--output=' + tmpOut,
			'--metadata=title:"foo"',
			input
		]

		if(hasTemplate(template,output)){
			generateCmdLine.add('--template=' + getTempTemplateFile(doc, template, lang, output))
		}else{
			generateCmdLine.add('--standalone')
		}	
		LOGGER.debug('Execute cmd '+StringUtils.join(generateCmdLine, ' '))
		project.exec({
			commandLine = generateCmdLine
			workingDir = getTempOutputFolder(doc, template, lang, output)
		})

		copyToSite(folder, doc, template, lang, output)
	}
			
	private void writeMainSourceFile(doc, tmpOutputDir, inputFile) {
		
		doc.includedSources.each { includedSource -> 
			File f = FileUtils.getFile(tmpOutputDir, includedSource + "." + doc.type)
			if(f.exists()) {
				inputFile.append(f.text, "UTF-8")
				inputFile.append("\n\n")
			}
			else {
				LOGGER.error("Sourcefile does not exist: " + f.absoluteName)
			}
		}

	}

	@Input
	protected int getTocDepth(){
		return 3	
	}
	
	protected String getPandocOutput(String output){
		return output
	}

	protected void copyToSite(File folder, Document doc, Template template,String lang, String output) {
		def tmpOut = getTempOutputDocument(doc, template, lang, output)
		def out =  getDocumentOutputFile(doc, template, lang, output)
		LOGGER.info("Copy ${folder} into ${out}")
		out.parentFile.mkdirs()
		FileUtils.copyFile(tmpOut,out)
	}

}