package marist;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class DegreeWorksParser {

  String degreeWorksText;
  List<String[]> textLines = new ArrayList<>();
  public int index = 0;
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
      // ignore arrays of size 0 to prevent ArrayIndexOutOfBoundsExceptions
      if (wordsInLine.length > 0) {
        textLines.add(wordsInLine);
      }
    }

    advanceUntil("Student");
    // Student name and [Undergraduate | Graduate]
    student.lastName = removeLastChar(textLines.get(index)[1]);
    student.firstName = textLines.get(index)[2];
    student.isUndergraduate = (findNextProperty("Level", 3).equals("Undergraduate"));
    index++;

    // CWID
    student.CWID = Integer.parseInt(textLines.get(index)[1]);
    index++;

    // Grade (ex: Senior) and School (ex: Computer Science & Mathematics)
    student.grade = textLines.get(index)[1];
    student.school = getSchool(2);
    index++;

    // Advisor and Major(s)
    student.advisor.lastname = removeLastChar(textLines.get(index)[1]);
    student.advisor.firstname = textLines.get(index)[2];
    student.majors = getMajorsMinors("Major", "Overall", 3);
    // don't enumerate index, already done in getMajorsMinors()

    // GPA and Minor(s)
    student.GPA = Double.parseDouble(textLines.get(index)[2]);
    student.minors = getMajorsMinors("Minor", "Student", 2);
    // don't enumerate index, already done in getMajorsMinors()

    // Degree Requirements %
    
    advanceUntil("Requirements");
    String requirementsPercent = removeLastChar(textLines.get(index)[0]);
    student.degreeProgress.requirementsPercent = Integer.valueOf(requirementsPercent);
    
    // Degree Credits %
    advanceUntil("Credits");

    String creditsPercent = removeLastChar(textLines.get(index)[0]);
    student.degreeProgress.creditsPercent = Integer.valueOf(creditsPercent);
    
    
    // Academic Year
    advanceUntil("Not");
    student.academicYear = findNextProperty("Year:", 0);
    
    // Degree Credits Required
    student.degreeProgress.creditsRequired = Integer.valueOf(findNextProperty("Required:", 2));
    
    // Credits Applied
    advanceUntil("Academic");
    student.degreeProgress.creditsApplied = Integer.valueOf(findNextProperty("Applied:", 0));
    
    
    // Requirements
    index++;
    student.requirements.isLast30Credits = checkStatus();
    student.requirements.hasMinLibArts = checkStatus();
    student.requirements.hasMinCredits = checkStatus();
    student.requirements.hasMinGPA = checkStatus();
    student.requirements.has20GPA = checkStatus();
    student.requirements.hasFoundation = checkStatus();
    student.requirements.hasBreadth = checkStatus();
    student.requirements.hasPathway = checkStatus();
    student.requirements.hasSkill = checkStatus();
    student.requirements.hasMajor = checkStatus();
    return student;
  }

  private String findNextProperty(String targetWord, int startingIndex) {
    int wordIndex = startingIndex;
    while (!textLines.get(index)[wordIndex].equals(targetWord)) {
      wordIndex++;
    }
    return textLines.get(index)[++wordIndex];
  }
  
   private void advanceUntil(String targetWord) {
    while (!textLines.get(index)[0].equals(targetWord)) {
      index++;
    }
    index++;
  }
   
   private Requirements.Status checkStatus() {
    if (textLines.get(index)[0].equals("Complete")) {
      if (textLines.get(index)[1].equals("except")) {
        index++;
        return Requirements.Status.IN_PROGRESS;
      } else {
        index++;
        return Requirements.Status.COMPLETE;
      }
    } else {
      index = index + 2;
      return Requirements.Status.INCOMPLETE;
    }
  }

  private String getSchool(int wordIndex) {
    while (!textLines.get(index)[wordIndex].equals("College")) {
      wordIndex++;
    }
    wordIndex++;
    int lineLength = textLines.get(index).length;
    StringBuilder schoolName = new StringBuilder();

    while (wordIndex < lineLength) {
      schoolName.append(textLines.get(index)[wordIndex]).append(" ");
      wordIndex++;
    }
    schoolName.setLength(schoolName.length() - 1);
    return schoolName.toString();
  }

  private List<String> getMajorsMinors(String majorOrMinor, String targetWord, int wordIndex) {
    List<String> majors = new LinkedList<>();
    while (!textLines.get(index)[wordIndex].contains(majorOrMinor)) {
      wordIndex++;
    }
    wordIndex++;

    do {
      int lineLength = textLines.get(index).length;
      StringBuilder sb = new StringBuilder();
      while (wordIndex < lineLength) {
        sb.append(textLines.get(index)[wordIndex]).append(" ");
        wordIndex++;
      }
      sb.setLength(sb.length() - 1);
      majors.add(sb.toString());
      wordIndex = 0;
      index++;
    } while (!textLines.get(index)[0].equals(targetWord));

    return majors;
  }

  private static String removeLastChar(String string) {
    return string.substring(0, string.length() - 1);
  }
}
