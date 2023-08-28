# Project Diagram Generator

[![CI](https://github.com/syoon2/Project-Diagram-Generator/actions/workflows/ci.yml/badge.svg)](https://github.com/syoon2/Project-Diagram-Generator/actions/workflows/ci.yml)
[![CodeQL](https://github.com/syoon2/Project-Diagram-Generator/actions/workflows/codeql.yml/badge.svg)](https://github.com/syoon2/Project-Diagram-Generator/actions/workflows/codeql.yml)

Program that takes the root folder of a Java project and builds a UML diagram
that corresponds to the provided project.

## Build

### Requirements

- Java 11 or later
- [Graphviz](https://graphviz.org/) (optional)

### Instructions

1. Clone this repository.
2. Run `./gradlew :project-diagram-generator-gui:run` (if you just want to run
  this) or `./gradlew :project-diagram-generator-gui:uberJar` (if you want an
  all-in-one JAR file)

## How to use this

- You need to provide three things for the program to run: the directory of the
  root path for your project (use the + button next to it that pops open a
  FileChooser browser to navigate your file system), any packages you want to
  ignore (please just use the + button to get a popout interface for selecting
  this, you can click on the packages to open and close them which translate to
  whether or not the contents of that package will be interpreted for the UML),
  and where your file will be saved.
- You can also filter out whether the UML will contain instance variables,
  functions, private instance variables/functions, and/or the constants using
  the four checkboxes on the right of the screen.

## License

- API: [![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)  
  The API submodule provides Java parsing library, independent of any GPL code.
  Thus, this submodule is distributed under the MIT License, matching
  the upstream license.
- GUI: [![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)  
  The GUI submodule involves GPL code, and thus, is distributed under GPLv3.
  The upstream repository, on the other hand, runs GUI using
  [non-GPL code](https://github.com/Reithger/SoftwareVisualInterface), and uses
  the MIT License for GUI as well.
