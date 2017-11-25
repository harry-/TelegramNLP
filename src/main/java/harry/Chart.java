package harry;

import org.knowm.xchart.*;
import org.knowm.xchart.BitmapEncoder.BitmapFormat;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.*;
import org.knowm.xchart.style.colors.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;

public class Chart {

  private static Logger logger = LogManager.getLogger();

 /**
  * Creates a simple Chart using QuickChart and saves it as pic.png
  */
  public void chart(double[] xData, double[] yData, String title) throws Exception {

    logger.entry(title);

    XYChart chart = QuickChart.getChart(title, "days", "mood", "y(x)", xData, yData);
    chart.getStyler().setPlotBackgroundColor(ChartColor.getAWTColor(ChartColor.BLACK));

    BitmapEncoder.saveBitmap(chart, "./pic", BitmapFormat.PNG);

    logger.debug("x values: " + Arrays.toString(xData));
    logger.debug("y values: " + Arrays.toString(yData));
    
    // Show it
    //new SwingWrapper(chart).displayChart();
    logger.exit();
  }
}