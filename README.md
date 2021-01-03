[![Maven Central](https://maven-badges.herokuapp.com/maven-central/ru.oleg-cherednik.utils/jackson-utils/badge.svg)](https://maven-badges.herokuapp.com/maven-central/ru.oleg-cherednik.utils/jackson-utils)
[![javadoc](https://javadoc.io/badge2/ru.oleg-cherednik.utils/jackson-utils/javadoc.svg)](https://javadoc.io/doc/ru.oleg-cherednik.utils/jackson-utils)
[![Build Status](https://travis-ci.org/oleg-cherednik/jackson-utils.svg?branch=dev)](https://travis-ci.org/oleg-cherednik/jackson-utils)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.txt)
[![codecov](https://codecov.io/gh/oleg-cherednik/jackson-utils/branch/dev/graph/badge.svg)](https://codecov.io/gh/oleg-cherednik/jackson-utils)
[![Known Vulnerabilities](https://snyk.io//test/github/oleg-cherednik/jackson-utils/badge.svg?targetFile=build.gradle)](https://snyk.io//test/github/oleg-cherednik/jackson-utils?targetFile=build.gradle)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/a2abf7ff8b1b4e82ad2cd0d039aea353)](https://www.codacy.com/gh/oleg-cherednik/jackson-utils/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=oleg-cherednik/jackson-utils&amp;utm_campaign=Badge_Grade)
[![coverity](https://scan.coverity.com/projects/22381/badge.svg)](https://scan.coverity.com/projects/oleg-cherednik-jackson-utils)
     
jackson-utils - a java tool to make working with Jackson Project more comfortable
=====================

## Features
* Encapsulate all checked exceptions from Jackson with custom runtime exception;
* A centralized configuration of `ObjectMapper`;
* `ObjectMapper` central holder - the place where single instance of `ObjectMapper` lives;
* Utility class to make most common operations much more comfortable to use;
* Ability to change `Zone` to save `ZonedDateTime` independently of original zone.

## Gradle

```
compile 'ru.oleg-cherednik.utils:jackson-utils:2.12.0.2'
```

## Maven

```xml
<dependency>
    <groupId>ru.oleg-cherednik.utils</groupId>
    <artifactId>jackson-utils</artifactId>
    <version>2.12.0.2</version>
</dependency>
```                                                    

In the version, first 3 places are the version of `Jackson` that is used in this utils.
The last section is the `jackson-utils` version. This number is unique. 

## Usage 

To simplify usage of _jackson-utils_, there're following classes:
* [JsonUtils](#jsonutils) - utility class with set of method to use json transformation;

### JsonUtils

#### Read json string as given class type (not a collection)

```java
public class Data {
    private int intVal;
    private String strVal;
}
```
```json                        
{
    "intVal" : 666,
    "strVal" : "omen"
}
```
```
Data data = JsonUtils.readValue(json, Data.class);
```

#### Read json string as a list of a given class type

```java
public class Data {
    private int intVal;
    private String strVal;
}
```
```json                        
[
    {
        "intVal" : 555,
        "strVal" : "victory"
    },
    {
        "intVal" : 666,
        "strVal" : "omen"
    }
]
```
```
List<Data> res = JsonUtils.readList(json, Data.class);
```

#### Read json string as a map

##### Map with `String` keys and `Map` or primitive types as values

```json                        
{
    "victory" : {
        "intVal" : 555,
        "strVal" : "victory"
    },
    "omen" : {
        "intVal" : 666,
        "strVal" : "omen"
    }
}
```
```
Map<String, ?> map = JsonUtils.readMap(json);
```
**Note:** `map` values have either primitive type or `Map`.

##### Map with `String` keys and given type as value

```java
public class Data {
    private int intVal;
    private String strVal;
}
```
```json                        
{
    "victory" : {
        "intVal" : 555,
        "strVal" : "victory"
    },
    "omen" : {
        "intVal" : 666,
        "strVal" : "omen"
    }
}
```
```
Map<String, Data> map = JsonUtils.readMap(json, Data.class);
```

##### Map with `Integer` keys and given type as value

```java
public class Data {
    private int intVal;
    private String strVal;
}
```
```json                        
{
    "1" : {
        "intVal" : 555,
        "strVal" : "victory"
    },
    "2" : {
        "intVal" : 666,
        "strVal" : "omen"
    }
}
```
```
Map<Integer, Data> map = JsonUtils.readMap(json, Integer.class, Data.class);
```

##### Links
* Home page: https://github.com/oleg-cherednik/jackson-utils
* Maven:
  * **central:** https://mvnrepository.com/artifact/ru.oleg-cherednik.utils/jackson-utils
  * **download:** https://repo1.maven.org/maven2/ru/oleg-cherednik/utils/jackson-utils/
