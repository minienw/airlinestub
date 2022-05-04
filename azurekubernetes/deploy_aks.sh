#!/bin/bash

SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" &> /dev/null && pwd )"
if [ -f $SCRIPT_DIR/.env ]
then
  export $(cat $SCRIPT_DIR/.env | sed 's/#.*//g' | xargs)
fi

template=`cat "deploy.template.yaml" \
  | sed "s|{{AZ_NAME}}|$AZ_NAME|g" \
  | sed "s|{{AZ_LOCATION}}|$AZ_LOCATION|g" \
  `

# Debug line, uncomment to see interpolated template
# echo "$template";exit 0;
echo "$template" | kubectl apply -f -