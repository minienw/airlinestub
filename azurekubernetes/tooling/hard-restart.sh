#!/bin/bash

# Do a shutdown and restart of the pod, useful when pod won't come up after a rollout.
# But usually this means, there are not enough resources on the nodes, or disk is not available on the node which has resources
# Check kubectl describe pods podname (or use ./describe.sh podname)

kubectl scale deployment $@ --replicas=0
kubectl scale deployment $@ --replicas=1
