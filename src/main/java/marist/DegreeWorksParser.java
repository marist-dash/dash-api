package marist;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class DegreeWorksParser {

  String degreeWorksText;
  List<String[]> report = new ArrayList<>();
  public int currLine = 0;
  public Student student = new Student();

  public DegreeWorksParser(String degreeWorksText) {
    this.degreeWorksText = degreeWorksText;
  }

  public Student extractStudentInfo() {
    Scanner scanner = new Scanner(this.degreeWorksText);
    String currentLine;
    String[] wordsInLine;
    while (scanner.hasNextLine()) {
      currentLine = scanner.nextLine();
      wordsInLine = currentLine.split("\\s+");
      // ignore arrays of size 0 to prevent ArrayIndexOutOfBoundsException           
      if (wordsInLine.length > 0) {
        if (wordsInLine.length == 1 && wordsInLine[0].length() == 0) {
          // ignore lines with nothing in them
        } else {
          report.add(wordsInLine);
        }
      }
    }

    advanceUntil("Student", 0);
    // Student name and [Undergraduate | Graduate]
    student.lastName = removeLastChar(report.get(currLine)[1]);
    student.firstName = report.get(currLine)[2];
    student.isUndergraduate = (findNextProperty("Level", 3).equals("Undergraduate"));
    currLine++;

    // CWID
    student.CWID = Integer.parseInt(report.get(currLine)[1]);
    currLine++;

    // Grade (ex: Senior) and School (ex: Computer Science & Mathematics)
    student.grade = report.get(currLine)[1];
    student.school = getSchool(2);
    currLine++;

    // Advisor(s)
    student.advisors = getAdvisors();

    // Major(s)
    student.majors = getMajorsMinors("Major", "Overall");
    // don't enumerate currLine, already done in getMajorsMinors()

    // GPA and Minor(s)
    student.GPA = Double.parseDouble(report.get(currLine)[2]);
    student.minors = getMajorsMinors("Minor", "Student");
    // don't enumerate currLine, already done in getMajorsMinors()

    // Degree Requirements %
    advanceUntil("Requirements", 0);
    String requirementsPercent = removeLastChar(report.get(currLine)[0]);
    student.degreeProgress.requirementsPercent = Integer.valueOf(requirementsPercent);

    // Degree Credits %
    advanceUntil("Credits", 0);

    String creditsPercent = removeLastChar(report.get(currLine)[0]);
    student.degreeProgress.creditsPercent = Integer.valueOf(creditsPercent);

    // Academic Year
    advanceUntil("Not", 0);
    student.academicYear = findNextProperty("Year:", 0);

    // Degree Credits Required
    student.degreeProgress.creditsRequired = Integer.valueOf(findNextProperty("Required:", 2));

    // Credits Applied
    advanceUntil("Academic", 0);
    student.degreeProgress.creditsApplied = Integer.valueOf(findNextProperty("Applied:", 0));

    // Requirements
    currLine++;
    student.requirements.isLast30Credits = checkStatus();
    student.requirements.hasMinLibArts = checkStatus();
    student.requirements.hasMinCredits = checkStatus();
    student.requirements.hasMinGPA = checkStatus();
    student.requirements.has20GPA = checkStatus();
    student.requirements.hasFoundation = checkStatus();
    student.requirements.hasBreadth = checkStatus();
    student.requirements.hasPathway = checkStatus();
    student.requirements.hasSkill = checkStatus();
    if (isHonorsStudent()) {
      student.requirements.hasHonors = checkStatus();
    } else {
      student.requirements.hasHonors = Requirements.Status.NA;
    }
    student.requirements.hasMajor = checkStatus();

    // get classes in progress
    getInProgress();

    return student;
  }

  private String findNextProperty(String targetWord, int startingIndex) {
    int wordIndex = startingIndex;
    while (!report.get(currLine)[wordIndex].equals(targetWord)) {
      wordIndex++;
    }
    return report.get(currLine)[++wordIndex];
  }

  private void advanceUntil(String targetWord, int targetIndex) {
    while (!report.get(currLine)[targetIndex].equals(targetWord)) {
      currLine++;
    }
    currLine++;
  }

  private Requirements.Status checkStatus() {
    if (report.get(currLine)[0].equals("Complete")) {
      if (report.get(currLine)[1].equals("except")) {
        currLine++;
        return Requirements.Status.IN_PROGRESS;
      } else {
        currLine++;
        return Requirements.Status.COMPLETE;
      }
    } else {
      currLine = currLine + 2;
      return Requirements.Status.INCOMPLETE;
    }
  }

  private String getSchool(int wordIndex) {
    while (!report.get(currLine)[wordIndex].equals("College")) {
      wordIndex++;
    }
    wordIndex++;
    int lineLength = report.get(currLine).length;
    StringBuilder schoolName = new StringBuilder();

    while (wordIndex < lineLength) {
      schoolName.append(report.get(currLine)[wordIndex]).append(" ");
      wordIndex++;
    }
    schoolName.setLength(schoolName.length() - 1);
    return schoolName.toString();
  }

  private List<String> getMajorsMinors(String majorOrMinor, String targetWord) {
    List<String> majors = new LinkedList<>();
    int wordIndex = 0;
    while (!report.get(currLine)[wordIndex].contains(majorOrMinor)) {
      wordIndex++;
    }
    wordIndex++;

    do {
      int lineLength = report.get(currLine).length;
      StringBuilder sb = new StringBuilder();
      while (wordIndex < lineLength) {
        sb.append(report.get(currLine)[wordIndex]).append(" ");
        wordIndex++;
      }
      sb.setLength(sb.length() - 1);
      majors.add(sb.toString());
      wordIndex = 0;
      currLine++;
    } while (!report.get(currLine)[0].equals(targetWord));

    return majors;
  }

  private List<Advisor> getAdvisors() {
    List<Advisor> advisors = new ArrayList<>();
    Advisor advisor = new Advisor();
    advisor.lastname = removeLastChar(report.get(currLine)[1]);
    advisor.firstname = report.get(currLine)[2];
    advisors.add(advisor);

    // check if student has one advisor
    for (String word : report.get(currLine)) {
      if (word.contains("Major")) {
        return advisors;
      }
    }
    // student has multiple advisors
    boolean hasAnotherAdvisor = true;
    while (hasAnotherAdvisor) {
      currLine++;
      for (String word : report.get(currLine)) {
        if (word.contains("Major")) {
          hasAnotherAdvisor = false;
        }
      }
      advisor = new Advisor();
      advisor.lastname = removeLastChar(report.get(currLine)[0]);
      advisor.firstname = report.get(currLine)[1];
      advisors.add(advisor);
    }
    return advisors;
  }

  private boolean isHonorsStudent() {
    for (String word : report.get(currLine)) {
      if (word.contains("Honors")) {
        return true;
      }
    }
    return false;
  }

  private void getInProgress() {
    advanceUntil("In-progress", 1);
    student.inProgress.numCredits = Integer.parseInt(report.get(currLine)[2]);
    int numClasses = student.inProgress.numClasses = Integer.parseInt(report.get(currLine)[5]);
    int endIndex = currLine + numClasses;
    currLine++;
    while (currLine < endIndex) {
      Course course = new Course();
      course.dept = report.get(currLine)[0];
      course.number = report.get(currLine)[1];
      int wordIndex = 2;
      StringBuilder courseNameBuilder = new StringBuilder();
      while (!report.get(currLine)[wordIndex].equals("IP")) {
        courseNameBuilder.append(report.get(currLine)[wordIndex])
                .append(" ");
        wordIndex++;
      }
      courseNameBuilder.setLength(courseNameBuilder.length() - 1);
      course.name = courseNameBuilder.toString();

      // skip "IP"
      wordIndex++;

      // credits
      course.credits = Integer.parseInt(report.get(currLine)[wordIndex]);
      wordIndex++;

      // semester
      course.semester = report.get(currLine)[wordIndex];

      // year
      course.semester = report.get(currLine)[wordIndex];

      student.inProgress.courses.add(course);
      currLine++;
    }
  }

  private static String removeLastChar(String string) {
    return string.substring(0, string.length() - 1);
  }
}
