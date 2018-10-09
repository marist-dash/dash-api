package marist;

public class Requirements {

  public Status isLast30Credits;
  public Status hasMinLibArts;
  public Status hasMinCredits;
  public Status hasMinGPA;
  public Status has20GPA;
  public Status hasFoundation;
  public Status hasBreadth;
  public Status hasPathway;
  public Status hasSkill;
  public Status hasHonors;
  public Status hasMajor;

  public enum Status {
    COMPLETE, INCOMPLETE, IN_PROGRESS, NA
  }
}
