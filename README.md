
Rapid Scanning Maven Plugin
===========================

This is a maven plugin to run Black Duck rapid scans.

Adding plugin to your project
-----------------------------

    <build>
        ...
        <plugins>
            ...
            <plugin>
                <groupId>com.synopsys.blackduck</groupId>
                <artifactId>rapid-scan-maven-plugin</artifactId>
                <version>1.0-SNAPSHOT</version>
            </plugin>
            ...
        </plugins>
        ...
    </build>


Executing a Rapid Scan
----------------------

    % mvn rapid-scan:scan
