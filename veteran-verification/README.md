# Veteran Confirmation Endpoints

## Status Expected Functionality

### Happy Path retrieve by EDIPI

```
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

`{ "veteran_status": "confirmed" }`

### Happy Path retrieve by ICN

```
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

`{ "veteran_status": "confirmed" }`

### Not Confirmed User


```
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

`{ "veteran_status": "not confirmed"}`

### No Emis User

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

**Expected Results**

```
{
"errors":[
        {
            "title":"Internal server error",
            "detail":"Internal server error",
            "code":"500",
            "status":"500"
        }
    ]
}
```

### Bad Request

```
curl --location --request POST 'http://localhost:8080/v0/status' \
--header 'Content-Type: application/json' \
--data-raw '{}'
```

**Expected Results**

```
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

### No User

```
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

`{ "veteran_status": "not confirmed"}`
