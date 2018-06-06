# Snow-scripts-sync

Snow Script Synchronizer is a tool helping developers to write code for the [ServiceNow](http://www.servicenow.com) platform.

Current version: 0.2.1 (alpha)

This tool downloads scripts from a ServiceNow instance to a local directory, allowing using IDE of own choice
to take advantage of code autocompletion, code navigation and documentation etc.

## Installation
Download the latest version from
https://github.com/chovanecm/snow-script-sync/releases

### Building from source
You can also build the software from source code:

    gradlew distZip

The command assumes you have a working JDK 1.8 or newer and creates a zip files with all dependencies under build/distributions.
## Usage

Unpack the distribution file and run in the bin directory    

	snow-scripts-sync -d c:\dev_instance -i demo019.service-now.com -u user_name

This will download scripts from demo019 to the c:\dev_instance directory. You can also specify a proxy using the "-x" argument:

	snow-scripts-sync -d c:\dev_instance -i demo019.service-now.com -u user_name -x 10.0.0.1:3128

## No Liability

As long as the software only reads scripts from ServiceNow, it won't modify the instance under normal circumstances.

However, the author holds no liability for possible damages of the ServiceNow database, especially, but not limited to,
problems caused by network failure, bugs in the software itself or in other software systems that it relies on.

Particularly, they were known issues with escaping characters of downloaded scripts. The problem seems to be solved,
but users are encouraged to always check the code they are copy-pasting or uploading back to ServiceNow.

## Contribution
Improvements, reviews and new features are highly appreciated!

These are some ideas to be worked on:

* The software currently downloads both sourcecode and other attributes of the record and stores them into a single file
 (attributes as a comment block).
  - It would be nice to have the attributes separated while providing a quick access to them, especially to the URL of the record
* Write more tests
* Uploading the changes back to ServiceNow
    - detecting (and possibly resolving) conflicts
* GUI

  

## License
Snow Script Synchronizer is licensed under the [GNU Lesser General Public License](https://www.gnu.org/licenses/gpl-3.0.txt).

For details, see LICENSE.txt.

*The GNU Lesser General Public License applies to all previous releases, even on those originally released with the MIT
or other license.*

### Credits
The software takes advantage of using the following libraries:

- [jsonj](https://github.com/jillesvangurp/jsonj/) by [Jilles van Gurp](https://github.com/jillesvangurp)
- [Apache Commons CLI](https://commons.apache.org/proper/commons-cli/)
- [Apache HttpComponents](https://hc.apache.org/)
