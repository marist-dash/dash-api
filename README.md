# dash-api
Exposes a REST endpoint for parsing DegreeWorks text

## Instructions
1) Create the Dash network (if not created already) `docker network create dash-net`
2) Build and start the service `docker-compose up -d`
    * Just build: `docker build -t dash-api:0.1.0 .`

## Endpoints

### Upload DegreeWorks text
**URL** : `/upload`

**Method** : `POST`

**Body Parameter** : `degreeWorksText`

### Healthcheck

Returns the date. Useful for checking service availability

**URL** : `/healthcheck`

**Method** : `GET`
