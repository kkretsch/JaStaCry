## Welcome to JaStaCry Pages

![](https://img.shields.io/badge/license-MIT-brithgreen.svg)
![](https://bestpractices.coreinfrastructure.org/projects/2521/badge)

We are nearly live with the first version.

In the meantime have a look at [JaStaCry Blog](https://blog.jastacry.org)

### GIT repos

Main development location is using a personal [GitLab](https://gitlab.kretschmann.software/stackedcrypto/JaStaCry) but we mirror the git repo also to [Github](https://github.com/kkretsch/JaStaCry).

### CI status

Sonarqube says:

![](https://sona.kretschmann.software/api/project_badges/measure?project=JaStaCry&metric=alert_status)

![](https://sona.kretschmann.software/api/project_badges/measure?project=JaStaCry&metric=coverage)


### manual command lines

#### Check for package updates

    mvn versions:display-dependency-updates

#### run script

    cd target/
    dd if=/dev/zero of=input.dat bs=1M count=1
    java -jar jastacry-0.6.5.jar -c ../src/test/resources/conf_dieharder.cfg --encode -i input.dat -o output.dat

# Versioning

Updating version hint following files:

* sonar-project.properties
* pom.xml
* VERSION.cpe


# Gitlab pages

Automated documentation [here](https://stackedcrypto.kretschmann.fyi/JaStaCry/).

# Discussion

Find a [Discourse](https://talk.kretschmann.social/c/projects/jastacry/6) area for this project.
