# mutation-tool-for-annotations

[![Build Status](https://travis-ci.org/easy-software-ufal/mutation-tool-for-annotations.svg?branch=master)](https://travis-ci.org/easy-software-ufal/mutation-tool-for-annotations) [![](https://jitpack.io/v/easy-software-ufal/mutation-tool-for-annotations.svg)](https://jitpack.io/#easy-software-ufal/mutation-tool-for-annotations) [![codecov](https://codecov.io/gh/easy-software-ufal/mutation-tool-for-annotations/branch/master/graph/badge.svg)](https://codecov.io/gh/easy-software-ufal/mutation-tool-for-annotations)

## Using with maven

Add repository:

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
<dependency>
  <groupId>com.github.easy-software-ufal</groupId>
  <artifactId>mutation-tool-for-annotations</artifactId>
  <version>Tag</version>
</dependency>
```

Replace the *Tag* with target release.

## Example

```java
MutationToolConfig config = new MutationToolConfig(new File(SOURCE_PATH));
config.setProjectName(PROJECT_NAME);
config.getOperators().addAll(Arrays.asList(OperatorsEnum.RMA, OperatorsEnum.RMAT));
config.setThreads(2);
new MutationTool(config).run();
```