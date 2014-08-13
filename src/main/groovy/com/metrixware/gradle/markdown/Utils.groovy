/*
 * Copyright 2014-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the 'License');
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an 'AS IS' BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.metrixware.gradle.markdown

class Utils {

	/**
	 * Get just the name of the file minus the path and extension.
	 *
	 * @param file
	 */
	static fileBaseName(file) {

		return file.name.replaceFirst(~/\.[^\.]+$/, '')
	}

	static getTemplates(project) {
		def docTypeNames    = [] as Set
		def docFolder = project.file(project.documentation.folder_docs)
		project.fileTree(docFolder) { include '**/*.md' }.each { docFile ->
			docTypeNames.add(docFile.parentFile.name)
		}
		return docTypeNames
	}

	static indexDocsPerType(project) {
		def docTypes        = [:]
		def docFolder = project.file(project.documentation.folder_docs)
		project.fileTree(docFolder) { include '**/*.md' }.each { docFile ->
			docTypes.put(fileBaseName(docFile), docFile.parentFile.name)
		}
		return docTypes
	}
}

