
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
                <groupId>com.synopsys.blackducksoftware</groupId>
                <artifactId>rapid-scan-maven-plugin</artifactId>
                <version>1.0</version>
            </plugin>
            ...
        </plugins>
        ...
    </build>


Executing a Rapid Scan
----------------------

    % mvn rapid-scan:scan

Ignore the below readme
----------------------
This is a test
