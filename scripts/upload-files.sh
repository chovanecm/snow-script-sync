#!/bin/bash

files="$@"
if [[ -z ${files} ]];
then
  while read line
  do
      files="$files $line"
  done
fi

if [[ -z ${snow_instance} ]];
then
    snow_instance=dev53140.service-now.com
fi

if [[ -n ${proxy} ]];
then
  command=$(echo snow-scripts-sync -d . -i $snow_instance -u ${snow_user} -x $proxy -fu $files);
else
  command=$(echo snow-scripts-sync -d . -i $snow_instance -u ${snow_user} -fu $files);
fi
echo $command
eval $command -p $snow_pwd < /dev/tty
