# customparameters

The custom properties plugin fetches different information and adds to the properties list which can be used by maven later on. As of now it has two goals:
* gitInfo : which fetches the last commit's SHA1 Id, Author, Timestamp & current branch ( To be used if it's a git repository only)
* hostInfo : which fetches the host name & ip on which the build is being done  

## Usage
```xml
<build>
  <plugins>
    <plugin>
        <groupId>in.papavicktorjuliett</groupId>
        <artifactId>customProperty-maven-plugin</artifactId>
        <version>1.0</version>
        <executions>
            <execution>
                <phase>initialize</phase>
                <goals>
                    <goal>gitInfo</goal>
                    <goal>hostInfo</goal>
                </goals>
            </execution>
        </executions>
    </plugin>
    .
    .
    .
  </plugins>
</build>
```
  
