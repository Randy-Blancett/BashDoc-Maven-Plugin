Bash Doc Maven Plugin

This will create documentation files for a bash script based on in script comments

## Usage

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

### Plugin Configurations

#### outputDir
This tells the plugin where to place the documentation files

**DEFAULT**:${project.build.directory}/doc

#### sourceDir
This tells the plugin the source files to process are located

**DEFAULT**:${project.build.outputDirectory}

#### outputRawXml
Tells the plugin to output a Raw xml version of the documentation

**DEFAULT**:true

#### outputText
This Tells the plugin to output a text version of the documentation

**DEFAULT**:true

## Tags

### FILE

indicates that the comment is for the file as a whole

