# lighthouse-veteran-verification

## Veteran Confirmation
### Veteran Status Endpoint
Currently, this enpoint makes a 1305 request (receiving 1306 response) to MPI in order to determine a veteran's status.
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
