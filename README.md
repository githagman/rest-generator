#Generate Java Spark Rest Api Routes From Database

Requires Java 8 and Maven install


***

###1 Install

   Download code from Github

   In the root directory
   
```
mvn clean install
```
###2 Configure

Edit the RestGenerator.properties to set connection info, databasename, and package info

Edit the template file to change the resulting generated files

###3 Build

```
mvn package
```

###4 Run

```
java -jar package/rest-generator-1.0-snapshot.jar
```

###5 Test

```
mvn test
```