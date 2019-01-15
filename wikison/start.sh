#!/bin/sh


getjson=`curl -u fei.wang:Bianli24. http://192.168.1.30:8090/rest/api/content/14123063`
content=${getjson}
echo "$content" > content.json

java -jar wikison-0.0.1-SNAPSHOT.jar

/bin/bash write.sh


