# Veteran Verification Endpoints

## Veteran status confirmed user
```
curl http://localhost:8080/v1/status/1012667145V762142
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

## Veteran status not confirmed user
```
curl http://localhost:8080/v1/status/1012666182V203777
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

## Veteran Status Emis outage
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
