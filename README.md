# dash-api
Exposes a REST endpoint for parsing DegreeWorks text

## Instructions
1) Create the Dash network (if not created already) `docker network create dash-net`
2) Build and start the service `docker-compose up -d`
    * Just build: `docker build -t dash-api:0.1.0 .`

## Endpoints

### Parse DegreeWorks text

**URL**: `/upload`

**Method**: `POST`

**Body Parameters**:

| Name | Type | Required |
| --- | --- | --- |
| `degreeWorksText` | String | True |

**Returns**:
```
{
    "lastName": "Washington",
    "firstName": "George",
    "isUndergraduate": true,
    "CWID": 20014876,
    "grade": "Senior",
    "school": "Computer Science & Mathematics",
    "advisor": {
        "firstname": "Alexander",
        "lastname": "Hamilton"
    },
    "majors": [
        "Computer Science"
    ],
    "GPA": 3.593,
    "minors": [
        "Cybersecurity",
        "Information Systems"
    ],
    "degreeProgress": {
        "requirementsPercent": 97,
        "creditsPercent": 94,
        "creditsRequired": 120,
        "creditsApplied": 113
    },
    "academicYear": "2015-2016",
    "requirements": {
        "isLast30Credits": "COMPLETE",
        "hasMinLibArts": "COMPLETE",
        "hasMinCredits": "INCOMPLETE",
        "hasMinGPA": "COMPLETE",
        "has20GPA": "COMPLETE",
        "hasFoundation": "COMPLETE",
        "hasBreadth": "IN_PROGRESS",
        "hasPathway": "COMPLETE",
        "hasSkill": "COMPLETE",
        "hasMajor": "INCOMPLETE"
    }
}
```
### Healthcheck
Returns the date. Useful for checking service availability

**URL** : `/healthcheck`

**Method** : `GET`
