#!/bin/bash

####
# With this script you can login using an App Registration instead of using your Azure account
####

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"
if [ -f $SCRIPT_DIR/.env ]
then
  export $(cat $SCRIPT_DIR/.env | sed 's/#.*//g' | xargs)
fi

az login --service-principal --username $AZ_APP_ID --password $AZ_PASSWORD --tenant $AZ_TENANT_ID