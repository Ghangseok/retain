#!/bin/bash
jar_file_name="bill-0.8.0-SNAPSHOT.jar"
java_command="java -jar ${jar_file_name}"
arguements=""

while [ "$1" != "" ]; do
    case $1 in
        -h | --host )       shift
                           arguements+="--endpoint=$1 "
                           ;;
        -u | --username )  shift
                           arguements+="--username=$1 "
                           ;;
        -p | --password )  shift
                           arguements+="--password=$1 "
                           ;;
        -t | --thread )    shift
                           arguements+="--maxThreadWorkers=$1 "
                           ;;
        -s | --pagesize )  shift
                           arguements+="--pageSize=$1 "
                           ;;
        -d | --dir )       shift
                           arguements+="--responsePath=$1 "
                           ;;
    esac
    shift
done

echo "${java_command} ${arguements}"
eval ${java_command} ${arguements}
