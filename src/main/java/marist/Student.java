package marist;

import java.util.ArrayList;
import java.util.List;

public class Student {

  public String lastName;
  public String firstName;
  public boolean isUndergraduate;
  public int CWID;
  public String grade;
  public String school;
  public List<Advisor> advisors;
  public List<Study> majors;
  public double GPA;
  public List<Study> minors;
  public DegreeProgress degreeProgress;
  public String academicYear;
  public Requirements requirements;
  public InProgress inProgress;

  public Student() {
    this.advisors = new ArrayList<>();
    this.degreeProgress = new DegreeProgress();
    this.requirements = new Requirements();
    this.inProgress = new InProgress();
  }
}
