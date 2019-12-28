## Welcome to JaStaCry Pages

![](https://img.shields.io/badge/license-MIT-brithgreen.svg)
![](https://bestpractices.coreinfrastructure.org/projects/2521/badge)

We are nearly live with the first version.

In the meantime have a look at [JaStaCry Blog](https://blog.jastacry.org)

### manual command lines

cd target/
dd if=/dev/zero of=input.dat bs=1M count=1
java -jar jastacry-0.6.5.jar -c ../src/test/resources/conf_dieharder.cfg --encode -i input.dat -o output.dat
