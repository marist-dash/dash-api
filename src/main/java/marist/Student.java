package marist;

import java.util.List;

public class Student {

  public String lastName;
  public String firstName;
  public boolean isUndergraduate;
  public int CWID;
  public String grade;
  public String school;
  public Advisor advisor;
  public List<String> majors;
  public double GPA;
  public List<String> minors;
  public DegreeProgress degreeProgress;
  public String academicYear;
  public Requirements requirements;

  public Student() {
    this.advisor = new Advisor();
    this.degreeProgress = new DegreeProgress();
    this.requirements = new Requirements();
  }
}
