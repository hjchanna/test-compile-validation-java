# test-compile-validation-java
> A test project for compile validation in java using `javax.annotation.processing.*` packages.

## Installation 
There are two main projects in this repository which are;
* validation_project
* test_project

### Validation Project
The validation project contains validation logic which will be processed in the compile time of the `test_project`. Maven is used as primary build tool of the validation_project. Following command will be used to build the `validation_project`.
````
mvn clean package
````

### Test Project
The test project is used to test validation logic defined in the `validation_project`. Maven build could be triggered for the project similar to the `validation_project`.
````
mvn clean package
````
## Prerequisites
Above projects have following dependencies.
* JDK _(tested with JDK1.8)_
* Maven
