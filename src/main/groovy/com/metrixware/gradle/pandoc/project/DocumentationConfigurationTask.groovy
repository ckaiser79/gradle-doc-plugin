package com.metrixware.gradle.pandoc.project

import org.apache.commons.io.FileUtils
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.metrixware.gradle.pandoc.AbstractDocumentationTask
import com.metrixware.gradle.pandoc.Document
import com.metrixware.gradle.pandoc.Template

class DocumentationConfigurationTask extends AbstractDocumentationTask {
	private static final Logger LOGGER = LoggerFactory.getLogger('pandoc-configure')

	private final List<ITemplateProcessor> templatesConfigurators

	DocumentationConfigurationTask(){
		this.templatesConfigurators = new ArrayList<ITemplateProcessor>()
		templatesConfigurators.add(new HtmlTemplateConfigurator())
		templatesConfigurators.add(new DefaultTemplateConfigurator())
	}


	protected void process() {
		println('Configure the documentation directories')
		def root = rootFolder
		root.mkdirs()
		this.tmpFolder.mkdirs()

		LOGGER.debug('Initialization of configured templates folders')
		templatesFolder.mkdir()

		for(Template template : project.documentation.templates){
			LOGGER.debug('--Initialize template '+template.name)
			def folder = getTemplateFolder(template)
			folder.mkdirs()
			for(String output : template.outputs){
				File outputDir = getTemplateFolder(template,output)
				outputDir.mkdir()
				configure(template,output,outputDir)
				getTemplateFile(template, output).createNewFile()
			}
		}
		LOGGER.debug('Initialization of configured documents')

		for(Document document : project.documentation.documents){
			LOGGER.debug('--Initialize document '+document.name)
			def folder =  FileUtils.getFile(sourcesFolder,document.name)
			folder.mkdirs()
			for(String lang : document.languages){
				def outputDir = FileUtils.getFile(folder,lang)
				outputDir.mkdir()
				getDocumentPropertiesFile(document, lang).createNewFile()

				if(document.includedSources.length == 0) {
					getDocumentSourceFile(document, lang).createNewFile()
				}
				else {
					document.includedSources.each { s -> 
						File f = FileUtils.getFile(getDocumentFolder(document,lang),s + '.' + document.type)
						f.createNewFile()
					}

				}
				FileUtils.getFile(outputDir,'images')
			}
		}
	}

	void configure(Template template,String output,File folder){
		for(ITemplateProcessor configurator : templatesConfigurators){
			if(configurator.configure(template,output,folder)){
				return 
			}
		}
		throw new IllegalStateException('Unable to find a compatible configurator for template '+template)
	}

}
