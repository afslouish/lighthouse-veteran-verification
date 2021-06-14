# lighthouse-veteran-verification

## Veteran Confirmation
### Veteran Status Endpoint
Currently, this endpoint makes a 1305 request (receiving 1306 response) to MPI in order to determine a veteran's status.
The search is performed using a POST to `/v0/status` including a body of attributes.
An example of these attributes is:
```
{
  "ssn": "796013145",
  "first_name": "Willard",
  "last_name": "Riley",
  "birth_date": "1959-02-25",
  "middle_name": "Tim",
  "gender": "M"
}
```
Example request:
```
curl --location --request POST 'http://localhost:8080/v0/status' \
--header 'Content-Type: application/json' \
--data-raw '{
  "ssn": "796013145",
  "first_name": "Willard",
  "last_name": "Riley",
  "birth_date": "1959-02-25",
  "middle_name": "Tim",
  "gender": "M"
}'
```

## Veteran Confirmation
### Veteran Status Endpoint
This endpoint takes in an ICN as a path variable and makes a 1305 request to MPI. An ICN or EDIPI is pulled from the 
MPI 1306 response and used to make an EMIS Veteran Status request to determine a veteran's status.
Example request:
```
curl --location --request GET 'http://localhost:8080/v0/status/1012667145V762142'
```


### Veteran Verification Documentation
Veteran Confirmation API
```
curl --location --request GET 'http://localhost:8080/v0/veteran_confirmation/openapi.json'
curl --location --request GET 'http://localhost:8080/v0/veteran_confirmation/'
```
