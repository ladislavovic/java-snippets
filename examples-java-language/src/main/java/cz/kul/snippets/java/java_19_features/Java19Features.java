package cz.kul.snippets.java.java_19_features;

import org.apache.http.util.Asserts;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class Java19Features {

  record Point(int x, int y) {
  }

  ;

  record LineSegment(Point p1, Point p2) {
  }

  ;

  @Test
  public void gettingStartedWithRecordPatterns() {

    Object obj = new Point(1, 2);

    if (obj instanceof Point(int x, int y)) { // So here you can work directly with record values
      Assert.assertEquals(3, x + y);
    }
  }

  @Test
  public void nestedRecordPatterns() {

    LineSegment obj = new LineSegment(new Point(1, 2), new Point(3, 4));

    if (obj instanceof LineSegment(
        Point(int x1, int y1), Point(int x2, int y2)
    )) { // you can even access nested record values
      Assert.assertEquals(10, x1 + y1 + x2 + y2);
    }
  }

}
