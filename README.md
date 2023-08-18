# Project-Diagram-Generator

Program that takes the root folder of a programming project and automatically generates a UML diagram for that entire project; will only process .java files for now as that is what I am familiar with.

## Build

### Requirements

- Java 11 or later
- A local copy of [SoftwareVisualInterface](https://github.com/syoon2/SoftwareVisualInterface)
- [Graphviz](https://graphviz.org/) (optional)

### Instructions

1. Get a copy of [SoftwareVisualInterface](https://github.com/syoon2/SoftwareVisualInterface) and install it in
your local Maven repository (run `./gradlew publishToMavenLocal`).
2. Clone this repository.
3. Run `./gradlew run` (if you just want to run this) or `./gradlew uberJar` (if you want an all-in-one JAR file)

## How to use this

- You need to provide three things for the program to run: the directory of the root path for your project (either manual entry or using the + button next to it that pops open a FileChooser browser to navigate your file system), any packages you want to ignore (please just use the + button to get a popout interface for selecting this, you can click on the packages to open and close them which translate to whether or not the contents of that package will be interpreted for the UML), and a name for the output image.

- You can also filter out whether the UML will contain instance variables, functions, and/or the private instance variables/functions using the three checkboxes on the right of the screen.

That's about it! Let me know if anything doesn't work or if you would recommend some changes; it currently only works on java projects (it has to know how to read the files in the project, which takes a lot of care) but I plan to eventually support more languages once I get to understanding them well enough to process them robustly.
