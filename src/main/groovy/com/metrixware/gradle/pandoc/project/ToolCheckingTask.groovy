/*
 * Copyright 2014-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.metrixware.gradle.pandoc.project

import org.gradle.api.GradleScriptException
import org.gradle.api.tasks.TaskAction
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.metrixware.gradle.pandoc.AbstractDocumentationTask

/**
 * @author Sylvain Leroy
 */
class ToolCheckingTask extends AbstractDocumentationTask {

	private static final Logger LOGGER = LoggerFactory.getLogger('pandoc-tools')



	protected void process() {

		if (!checkPandocPresence()) {
			throw new GradleScriptException('Pandoc is missing, please install it or change the path in the configuration.', null)
		}

		if (!checkLatexPresence()) {
			throw new GradleScriptException('Latex is missing, please install it or change the path in the configuration.', null)
		}
	}

	def checkPandocPresence() {
		ProcessBuilder builder = new ProcessBuilder("${project.documentation.panDocBin} -v".split())
		builder.redirectErrorStream(true)
		builder.directory(project.rootDir)
		Process process = builder.start()
		def errorCode = process.waitFor()
		LOGGER.info("Checking if pandoc is installed :\t[${errorCode == 0 ? 'OK' : 'NOK'}]")
		return errorCode == 0
	}

	def checkLatexPresence() {
		ProcessBuilder builder = new ProcessBuilder("${project.documentation.pdfTexBin} --version".split())
		builder.redirectErrorStream(true)
		builder.directory(project.rootDir)
		Process process = builder.start()
		def errorCode = process.waitFor()
		LOGGER.info("Checking if latex is installed :\t[${errorCode == 0 ? 'OK' : 'NOK'}]")
		return errorCode == 0
	}
	
}


