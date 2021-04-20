package metrics;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.junit.platform.suite.api.SelectClasses;

@RunWith(JUnitPlatform.class)
@SelectClasses({MetricsTest.class,IndicatorTest.class,ReadJavaProjectTest.class,FileDealerTest.class})
public class AllTests {

}
