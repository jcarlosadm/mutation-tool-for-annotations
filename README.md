# mutation-tool-for-annotations

[![Build Status](https://travis-ci.org/easy-software-ufal/mutation-tool-for-annotations.svg?branch=master)](https://travis-ci.org/easy-software-ufal/mutation-tool-for-annotations) [![](https://jitpack.io/v/easy-software-ufal/mutation-tool-for-annotations.svg)](https://jitpack.io/#easy-software-ufal/mutation-tool-for-annotations) [![codecov](https://codecov.io/gh/easy-software-ufal/mutation-tool-for-annotations/branch/master/graph/badge.svg)](https://codecov.io/gh/easy-software-ufal/mutation-tool-for-annotations)

This a lib to mutate code annotations from Java source code. From a set of operators, this will create one javaMutant for each change on original code, related to code annotations. For example, consider the following code:

```java
@RequestMapping(value = "/foo")
public String form() {
  return "form";
}
```

The **ADAT** (*Add Attribute*) operator can create the following javaMutants:

```java
@RequestMapping(value = "/foo", method = RequestMethod.POST)
public String form() {
  return "form";
}
```

```java
@RequestMapping(value = "/foo", method = RequestMethod.GET)
public String form() {
  return "form";
}
```

With these javaMutants, the developer can evaluate your test suite. The ultimate goal is *kill* these javaMutants with tests, improving the test suite. This project was based on theory of *Mutation Testing*. For more details, see [An Analysis and Survey of the Development of Mutation Testing](https://ieeexplore.ieee.org/abstract/document/5487526) and [Mutation Operators for Code Annotations](https://dl.acm.org/citation.cfm?id=3266006&dl=ACM&coll=DL).

## Operators

This project have a set of 9 operators:

Operator | Description
---------|---------------
ADA | Add annotation
ADAT | Add attributes to annotation
CHODR | Change order of annotations
RMA | Remove annotations
RMAT | Remove attributes of annotations
RPA | Replace one annotation for another
RPAT | Replace one attribute for another
RPAV | Replace one attribute value for another
SWTG | Change location of one annotation

To use this project, it is necessary to choose one or more of these operators.

## Configuration

You will need the srcML tool: [https://www.srcml.org/](https://www.srcml.org/). Install on your OS.

First, create a *Maven* project with java.

On *pom.xml*, Add the [Jitpack](https://jitpack.io/) repository:

```xml
<repositories>
  <repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
  </repository>
</repositories>
```
  
And the Dependency:

```xml
<dependencies>
  <dependency>
    <groupId>com.github.easy-software-ufal</groupId>
    <artifactId>mutation-tool-for-annotations</artifactId>
    <version>Tag</version>
  </dependency>
</dependencies>
```

Replace the *Tag* with target release.

## How to use

First you need to fill the **annotations.json** of the project (located on the *config* folder). This is a configuration file, with information of annotations. The following code is a example of this file:

```javascript
{
  "annotations": [
    {
      "annotation": "@org.springframework.web.bind.annotation.RequestMapping",
      "replaceableBy": [],
      "targets": ["type", "method"],
      "attributes": [
        {
          "name": "value",
          "type": "java.lang.String[]",
          "validValues": ["\"/ex/foo\""],
          "default": "true"
        },
        {
          "name": "method",
          "type": "org.springframework.web.bind.annotation.RequestMethod[]",
          "validValues": ["org.springframework.web.bind.annotation.RequestMethod.POST"]
        },
        {
          "name": "headers",
          "type": "java.lang.String[]",
          "validValues": ["\"content-type=text/*\""]
        }
      ]
    },
    {
      "annotation": "@org.springframework.beans.factory.annotation.Autowired",
      "replaceableBy": ["@org.springframework.beans.factory.annotation.Qualifier"],
      "targets": ["field", "method"],
      "attributes": [{}]
    },
    {
      "annotation": "@org.springframework.beans.factory.annotation.Qualifier",
      "replaceableBy": ["@org.springframework.beans.factory.annotation.Autowired"],
      "targets": ["field", "parameter"],
      "attributes": [{}]
    }
  ]
}
```

For each annotation, we have the following fields:

**annotation**: the base of annotation, with `@`, the package and his name. Mandatory.

**replaceableBy**: list of replacements of the annotation. Each replacement is a base of annotation, such as the *annotation* field. Optional.

**targets**: list of targets of the annotation. Targets are locations where the annotations can be placed. valid values: type, method, field, parameter. Mandatory (at least one target).

**attributes**: list of attributes of annotations. Optional.

For each attribute, we have the following fields:

**name**: name of the attribute. Mandatory.

**type**: type of the attribute. Can be any Java type. Mandatory.

**validValues**: list of valid values. Mandatory.

**default**: this mark the attribute as default, which means that the annotation will receive only the value without the attribute name. Only works when the annotation have one attribute. Can be true or false. Optional.

-----

After fill the *annotations.json*, it is necessary to create a Java code to use this project to generate the javaMutants. The following code is an example:

```java
MutationToolConfig config = new MutationToolConfig(new File(SOURCE_PATH));
config.setProjectName(PROJECT_NAME);
config.getOperators().addAll(Arrays.asList(OperatorsEnum.RMA, OperatorsEnum.RMAT));
config.setThreads(2);
new MutationTool(config).run();
```

### Basic configuration

The constructor of the *MutationToolConfig* class have the following field:

**pathSources**: directory of the source code. This doesn't include the test code.

This class have the following methods:

**setProjectName**: set the name of original project.

**setThreads**: set the number of threads.

**getOperators**: get the list of operators.

### Setting the operators

The method *getOperators* of *MutationToolConfig* class retrieve the list of operators. It is possible to add operators to this list:

**Add all operators**

```java
config.getOperators().addAll(Arrays.asList(OperatorsEnum.values()));
```

**Add some operators**

```java
config.getOperators().addAll(Arrays.asList(OperatorsEnum.RMA, OperatorsEnum.RMAT));
```

### Generate the javaMutants

The last thing is the use the method *run* of the *MutationTool* class:

```java
new MutationTool(config).run();
```

All the javaMutants will be generated in the *data/javaMutants* folder.