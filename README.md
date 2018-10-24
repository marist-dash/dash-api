# dash-api
Exposes a REST endpoint for parsing DegreeWorks text

## Instructions
1) Create the Dash network (if not created already) `docker network create dash-net`
2) Building the image and starting the service
    - Development: `mvn clean package`, `docker-compose up -d`
        - To rebuild the image: `docker-compose up -d --build`
    - Production: `docker-compose -f production.yml up -d`
        - To rebuild the image: `docker-compose -f production.yml up -d --build`

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
        {
            "name": "Computer Science",
            "GPA": 3.22,
            "creditsRequired": 67,
            "creditsApplied": 53
        }
    ],
    "GPA": 3.593,
    "minors": [
        {
            "name": "Information Technology",
            "GPA": 3.05,
            "creditsRequired": 23,
            "creditsApplied": 18
        }
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
        "hasMajor": "INCOMPLETE",
        "hasHonors": "NA"
    }
}
```
### Healthcheck
Returns the date. Useful for checking service availability

**URL** : `/healthcheck`

**Method** : `GET`



## Classes

| Name | Notes | Link |
| ---- | ------- | ---- |
| Advisor | A student has one or many advisors | [Advisor.java](https://github.com/marist-dash/dash-parse/blob/master/src/main/java/marist/Advisor.java) |
| Course | Represents a class taken by a student | [Course.java](https://github.com/marist-dash/dash-parse/blob/master/src/main/java/marist/Course.java) | 
| DegreeProgress | Numerical representation of student's progress towards graduation | [DegreeProgress.java](https://github.com/marist-dash/dash-parse/blob/master/src/main/java/marist/DegreeProgress.java) | 
| DegreeWorksParser | Extracts relavant information from a DegreeWorks report and populates all other objects | [DegreeWorksParser.java](https://github.com/marist-dash/dash-parse/blob/master/src/main/java/marist/DegreeWorksParser.java) | 
| InProgress | A list of type `Course` | [InProgress.java](https://github.com/marist-dash/dash-parse/blob/master/src/main/java/marist/InProgress.java)| 
| Requirements | Object representing student's requirements for graduation | [Requirements.java](https://github.com/marist-dash/dash-parse/blob/master/src/main/java/marist/Requirements.java) | 
| Student | Represents a student and his/her DegreeWorks report  | [Student.java](https://github.com/marist-dash/dash-parse/blob/master/src/main/java/marist/Student.java) | 
| Study | Represents a major or minor | [Study.java](https://github.com/marist-dash/dash-parse/blob/master/src/main/java/marist/Study.java) |

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
| hasHonors | Honors Student (Status for non-honors students is `NA`) |
