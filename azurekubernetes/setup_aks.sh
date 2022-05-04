#!/bin/bash

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"
if [ -f $SCRIPT_DIR/.env ]
then
  export $(cat $SCRIPT_DIR/.env | sed 's/#.*//g' | xargs)
fi

az deployment group create --resource-group $AZ_RESOURCE_GROUP --template-file aks_template.json -p \
  resourceName=$AZ_NAME \
  location=$AZ_LOCATION \
  dnsPrefix=$AZ_NAME
