# Veteran Confirmation Endpoints

## Happy Path retrieve by EDIPI

```bash
curl --location --request POST 'http://localhost:8080/v0/status' \
--header 'Content-Type: application/json' \
--data-raw '{
  "ssn": "796012476",
  "first_name": "Alfredo",
  "last_name": "Armstrong",
  "birth_date": "1993-06-08",
  "middle_name": "M",
  "gender": "M"
}'
```

**Expected Results**

```json
{ "veteran_status": "confirmed" }
```

## Happy Path retrieve by ICN

```bash
curl --location --request POST 'http://localhost:8080/v0/status' \
--header 'Content-Type: application/json' \
--data-raw '{
  "ssn": "796220828",
  "first_name": "ARTHUR",
  "last_name": "ROSE",
  "birth_date": "1954-05-26",
  "middle_name": "E",
  "gender": "M"
}'
```

**Expected Results**

```json
{ "veteran_status": "confirmed" }
```

## Not Confirmed User


```bash
curl --location --request POST 'http://localhost:8080/v0/status' \
--header 'Content-Type: application/json' \
--data-raw '{
  "ssn": "796126859",
  "first_name": "Hector",
  "last_name": "Allen",
  "birth_date": "1932-02-05",
  "middle_name": "J",
  "gender": "M"
}'
```

**Expected Results**

```json
{ "veteran_status": "not confirmed"}
```

## No Emis User

```bash
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

**Expected Results**

```json
{ "veteran_status": "not confirmed"}
```

## Bad Request

```bash
curl --location --request POST 'http://localhost:8080/v0/status' \
--header 'Content-Type: application/json' \
--data-raw '{}'
```

**Expected Results**

```json
{"errors": [
    {
        "title":"Missing parameter",
        "detail":"The required parameter \"ssn\", is missing",
        "code":"108",
        "status":"400"
    }
  ]
}
```

## No User

```bash
curl --location --request POST 'http://localhost:8080/v0/status' \
--header 'Content-Type: application/json' \
--data-raw '{
  "ssn": "000000000",
  "first_name": "John",
  "last_name": "Smith",
  "birth_date": "1234-04-05",
  "middle_name": "A",
  "gender": "M"
}'
```

**Expected Results**

```json
{ "veteran_status": "not confirmed"}
```

## Emis or MPI Outage

```json
{
  "errors":[
    {"title":"Service unavailable",
      "detail":"An external service is unavailable.",
      "code":"503",
      "status":"503"
    }
  ]
}
```

# Veteran Verification Endpoints

## Confirmed user
```
curl http://localhost:8080/v0/status/1012667145V762142
```
**Expected Results**
```json
{
  "data": {
    "id": "1012667145V762142",
    "type": "veteran_status_confirmations",
    "attributes": {
      "veteran_status": "confirmed"
    }
  }
}
```

## Not confirmed user
```
curl http://localhost:8080/v0/status/1012666182V203777
```

**Expected Results**
```json
{
  "data": {
    "id": "1012666182V203777",
    "type": "veteran_status_confirmations",
    "attributes": {
      "veteran_status": "not confirmed"
    }
  }
}
```

## Emis outage
**Expected Results**
```json
{
    "errors": [
        {
            "title": "Unexpected response body",
            "detail": "EMIS service responded with something other than veteran status information.",
            "code": "EMIS_STATUS502",
            "status": "502"
        }
    ]
}
```
