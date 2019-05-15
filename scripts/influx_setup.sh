# Source: https://towardsdatascience.com/get-system-metrics-for-5-min-with-docker-telegraf-influxdb-and-grafana-97cfd957f0ac
# with modified db name and user

docker network create monitoring

docker network create monitoring
docker volume create grafana-volume
docker volume create influxdb-volume

docker network ls
docker volume ls

docker run --rm \
  -e INFLUXDB_DB=bitexchange -e INFLUXDB_ADMIN_ENABLED=true \
  -e INFLUXDB_ADMIN_USER=admin \
  -e INFLUXDB_ADMIN_PASSWORD=supersecretpassword \
  -e INFLUXDB_USER=bitexchange -e INFLUXDB_USER_PASSWORD=secretpassword \
  -v influxdb-volume:/var/lib/influxdb \
  influxdb /init-influxdb.sh


docker-compose up -d