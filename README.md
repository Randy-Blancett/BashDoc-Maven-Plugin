Bash Doc Maven Plugin

This will create documentation files for a bash script based on in script comments

## Usage
	<plugin>
        <artifactId>maven-resources-plugin</artifactId>
        <executions>
          <execution>
            <id>copy-resources</id>
            <goals>
              <goal>copy-resources</goal>
            </goals>
            <phase>validate</phase>
            <configuration>
              <outputDirectory>${basedir}/target/bash</outputDirectory>
              <resources>
                <resource>
                  <directory>src/main/bash</directory>
                  <filtering>true</filtering>
                </resource>
              </resources>
            </configuration>
          </execution>
        </executions>
      </plugin>
      <plugin>
        <groupId>org.darkowl.bashDoc</groupId>
        <artifactId>bashDoc-maven-plugin</artifactId>
        <executions>
          <execution>
            <id>Document Library</id>
            <goals>
              <goal>document</goal>
            </goals>
            <phase>package</phase>
          </execution>
        </executions>
      </plugin>
      
## Deployment
    mvn release:clean release:prepare
    mvn release:perform      

### Plugin Configurations

#### outputDir
This tells the plugin where to place the documentation files

**DEFAULT**:${project.build.directory}/doc

#### sourceDir
This tells the plugin the source files to process are located

**DEFAULT**:${project.build.directory}/bash

#### outputRawXml
Tells the plugin to output a Raw xml version of the documentation

**DEFAULT**:true

#### outputText
This Tells the plugin to output a text version of the documentation

**DEFAULT**:true

## Tags
### AUTHOR
This is the general author of the file
### AUTHOR_EMAIL
This is the email of the author of the file
### EXAMPLES
This is the Examples of how to use the method
### EXIT_CODES
This documents the exit code for either a script or a function
### FILE
indicates that the comment is for the file as a whole
### METHOD
Indicates that the code block is for a method
### PRIVATE
Indicates that the code section was intended to be private
### PROTECTED
Indicates that the code section was intended to be protected
### PUBLIC
Indicates that the code section was intended to be public
### RELEASE
This is the date of a given release
### RETURN
This will describe the Return from the Method (if there is one)
### V
This is used under the VERSION tag and tells the name of the version to be described

### VARIABLE
This indicates that the comment will be for a variable


### VERSION
Current Version of the File

### VERSIONS
This indicates that a list of versions with notes about each will follow

## Version Log

### 1.3.0
### 16Apr2023
#### New Features
- Add ability to document return data from a method
- Fixed issues in how this handles non-text charators when formating lines
- Split Return code form method Exit code
- General Code quality cleanups

### 1.2.0
### 11NOV2021
#### New Features
- Added Exit codes

#### Known Bugs

### 1.1.1
#### New Features
- Update Flatten Pom Plugin so we can deploy to nexus

#### Known Bugs


### 1.1.0
#### New Features
- Added Examples
- Added Return Values
- Add Parameters
- Wrap Long Lines

#### Known Bugs
- Issue with flatten pom plugin that won't allow nexus deploy

# Developer Setup
## Setup PGP Signature
1) Install GnuPG
    * ``` shell
      sudo apt install gnupg
      ```
2) Generate a Key
    * ``` shell
      gpg --gen-key
      ```
## Release Artifact
1) Cut Release
    * ``` shell
       mvn release:prepare
       mvn release:perform
      ```

