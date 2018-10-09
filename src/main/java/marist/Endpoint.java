package marist;

import java.util.Date;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class Endpoint {

  @RequestMapping(method = RequestMethod.POST, value = "/")
  public Student uploadText(@RequestParam String degreeWorksText) {
    DegreeWorksParser degreeWorksParser = new DegreeWorksParser(degreeWorksText);
    Student student = degreeWorksParser.extractStudentInfo();
    return student;
  }

  @RequestMapping("/healthcheck")
  public String doHealthCheck() {
    return new Date().toString();
  }
}
