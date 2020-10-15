#!/usr/bin/env bash

if [[ -z ${snow_instance} ]];
then
    snow_instance=dev53140.service-now.com;
fi

if [[ -z ${proxy} ]];
then
    snow_proxy=;
else
    snow_proxy="-x $proxy";
fi

snow-scripts-sync -d . -i ${snow_instance} ${snow_proxy} -u ${snow_user} -rd
