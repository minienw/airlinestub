#!/bin/bash

kubectl logs -f -l app=$@
