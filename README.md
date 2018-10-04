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



## Classes

### Student

| Name  | Datatype | Example
| ------------- | ------------- | ------------- |
| lastname  | string  |
| firstname  | string  |
| isUndergraduate | boolean |
| CWID | integer | 20043823 |
| GPA | double | 3.75 |
| grade | string | Junior |
| academicYear | string | 2015-2016 |
| school | string | Computer Science & Mathematics |
| majors | List of strings |
| minors | List of strings |
| advisor | `Advisor` | [See `Advisor`] |
| degreeProgress | `DegreeProgress` | [See `DegreeProgress`] |
| requirements | `Requirements` | [See `Requirements`] |

### Advisor

| Name  | Datatype |
| ------------- | ------------- |
| lastname | string |
| firstname | string |

### DegreeProgress

| Name  | Datatype |
| ------------- | ------------- |
| requirementsPercent | integer |
| creditsPercent | integer |
| creditsRequired | integer |
| creditsApplied | integer |

### Requirements
All properties are of type `Status` (see below)

| Name  | Notes
| ------------- | ------------- |
| isLast30Credits | Last 30 Credits At Marist |
| hasMinLibArts | Mimimum Liberal Art Credits |
| hasMinCredits | Minimum Degree Credits |
| hasMinGPA | Minimum GPA |
| has20GPA | 2.0 GPA Requirement |
| hasFoundation | Foundation |
| hasBreadth | Distribution: Breadth |
| hasPathway | Distribution: Pathway |
| hasSkill | Skill Requirement |
| hasMajor | Major Requirements |

### Status (Java Enum)
Constants: `COMPLETE`, `INCOMPLETE`, `IN_PROGRESS`

