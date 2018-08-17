# Readme

## Troubleshooting installation of source

### Formatting
No cuddly braces are used. The local project file _checkstyle.xml_ is used for verifying that.

Source code formatting has to know about that too. The local project file _codestyle.xml_ is references by the project properties.

### Eclipse additions

Checkstyle plugin via [](http://eclipse-cs.sourceforge.net/#!/ "Checkstyle") install by D&D to Eclipse.
Afterwards create new global style, name it as you like, load it from the checkstyle.xml of this project and use that style on the project settings of eclipse. JavaCodeStyle->Formatter->Import to read the current specification.

### Error RegCreateKeyEx

    Could not open/create prefs root node Software\JavaSoft\Prefs at root 0x80000002. Windows RegCreateKeyEx(...) returned error code 5

solved by creating a new registry key as administrator:

    Computer\HKEY_LOCAL_MACHINE\SOFTWARE\JavaSoft\Prefs