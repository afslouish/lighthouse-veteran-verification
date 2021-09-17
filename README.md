# lighthouse-veteran-verification

## Veteran Verification
### Veteran Status Endpoint
This endpoint takes in an ICN as a path variable and makes a 1305 request to MPI. An ICN or EDIPI is pulled from the 
MPI 1306 response and used to make an EMIS Veteran Status request to determine a veteran's status.
Example request:
```
curl --location --request GET 'http://localhost:8080/v1/status/1012667145V762142'
```

### Documentation
```
curl --location --request GET 'http://localhost:8080/v1/docs/veteran_verification'
```
