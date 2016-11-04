
# Memor-i Wind Music Game for the blind

![Memor-i logo](https://raw.githubusercontent.com/scify/Wind-Music-Game-for-the-blind/master/src/main/resources/img/windmusicgame_large.png)

A Set of "Memory" card games especially tailored to meet the needs of blind music students.
Check out our [YouTube video](https://www.youtube.com/watch?v=M2DqT5e975w)


## Getting Started

Please take a look at the [project roadmap](http://jira.scify.org/secure/RapidBoard.jspa?rapidView=121&projectKey=WMG&view=detail&selectedIssue=WMG-3")
To see currently open tasks, discussed ideas and submitted bugs

### Prerequisities

After cloning the code repository, Java 1.8 and Apache Maven 3.0.5 are required to be able to compile it.
Ensure that Java and Maven version installed on your machine by running:
```
mvn -v

Apache Maven 3.0.5
Maven home: /usr/share/maven
Java version: 1.8.0_101, vendor: Oracle Corporation
Java home: /usr/lib/jvm/java-8-oracle/jre

```

### Installing

By looking at pom.xml you will see some plugins this project uses. 
Supposing that Maven runs correctly on your machine and you have configured this project as a Maven project, 
these plugins will be automativcally downloaded and installed upon compilation.

After compiling, in order for the standalone .jar file to be built, run these 2 commands:

```
mvn assembly:single 

mvn dependency:copy-dependencies
```

These tasks can also be accomplished more easily just by running 
```
./build_project.sh
```

which is a UNIX executable file located at the root of the project.

## Built With

* Java - 1.8
* Maven - 3.0.5
* IntelliJ - 16

## Authors

* **SciFY Development team** - *Initial work* - [SciFY](https://github.com/scify)

See also the list of [contributors](https://github.com/scify/Memor-i/graphs/contributors) who participated in this project.

## License

All images and sounds are licenced under [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/)

Copyright 2016

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

## Sponsor

![Sponsor logo](https://raw.githubusercontent.com/scify/Wind-Music-Game-for-the-blind/master/src/main/resources/img/WIND_logo_blue.png)

## Acknowledgments

* [Maven plugin for parsing JSON files](https://mvnrepository.com/artifact/org.json/json)
