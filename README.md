# Bitexchange

Proof of concept for periodically (default 10s) fetching bitcoin exchange rates and store it in timed database (InfluxDB) that can be later displayed in Grafana dashboard

# Architecture

Project consist of microservice with scheduler that will periodically checks bitcoin stock (https://blockchain.info/ticker).
Result of this checks are stored in InnoDB (time series database) and are available to clients through REST API.
Results are also available in graphical form in Grafana dashboard.

DB Schema
- time - time of event
- source - source currency
- destination - destination currency
- rate - current exchange rate in between those currencies

Current version is checking just bitcoin stock that is why API is assuming conversion is made between BTC and chosen currency.
Database is already prepared to accept exchange between different currencies.


# API

see api.yml

# Setup locally

## Create local InfluxDB & Grafana

see ./scripts/influx_setup.sh

## Build application

mvn clean install

## Run

mvn spring-boot:run

after successful boot it should report UP status in health check endpoint
https://localhost:8080/actuator/health

## Setup Grafana

see:
https://towardsdatascience.com/get-system-metrics-for-5-min-with-docker-telegraf-influxdb-and-grafana-97cfd957f0ac

and "screenshots" dir