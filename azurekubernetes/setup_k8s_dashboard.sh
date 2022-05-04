#!/bin/bash

ADMIN_USER="$(kubectl -n kubernetes-dashboard get sa/admin-user -o jsonpath="{.secrets[0].name}" 2> /dev/null)"

if [ ! -z "$ADMIN_USER" ]
then
  echo "Dashboard was already setup!"
  exit 1;
fi

kubectl apply -f https://raw.githubusercontent.com/kubernetes/dashboard/v2.2.0/aio/deploy/recommended.yaml

cat <<EOF | kubectl apply -f -
apiVersion: v1
kind: ServiceAccount
metadata:
  name: admin-user
  namespace: kubernetes-dashboard
EOF

cat <<EOF | kubectl apply -f -
apiVersion: rbac.authorization.k8s.io/v1
kind: ClusterRoleBinding
metadata:
  name: admin-user
roleRef:
  apiGroup: rbac.authorization.k8s.io
  kind: ClusterRole
  name: cluster-admin
subjects:
- kind: ServiceAccount
  name: admin-user
  namespace: kubernetes-dashboard
EOF