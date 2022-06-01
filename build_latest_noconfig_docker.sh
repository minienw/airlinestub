docker build -f latest_noconfig.Dockerfile -t airline_stub:latest_noconfig .
docker tag airline_stub:latest_noconfig ghcr.io/minienw/airline_stub:latest_noconfig
docker push ghcr.io/minienw/airline_stub:latest_noconfig


