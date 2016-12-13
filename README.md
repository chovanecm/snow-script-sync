# Snow-scripts-sync

This tool downloads all your scripts from a ServiceNow instance, so that you can use your favourite IDE
to take advantage of autocompletion, generating code documentation etc.
As uploading modified files back to ServiceNow has not been implemented yet,
each file contains an URL pointing to the script editor, so you can simply click / copy-paste it and open it in your browser to update the fiel yourself.

## Usage:
	java -jar snow-scripts-sync-3.0-SNAPSHOT-jar-with-dependencies.jar -d c:\dev_instance -i demo019.service-now.com -u user_name -p password
will download scripts from demo019 to c:\dev_instance directory. You can also specify proxy using the "-x" argument like that:
	java -jar snow-scripts-sync-3.0-SNAPSHOT-jar-with-dependencies.jar -d c:\dev_instance -i demo019.service-now.com -u user_name -p password -x 10.0.0.1:3128

	
## License

The MIT License (MIT)
Copyright (c) 2016 Martin Chovanec

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.