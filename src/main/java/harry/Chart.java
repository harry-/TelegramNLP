package harry;

import org.knowm.xchart.*;
import org.knowm.xchart.BitmapEncoder.BitmapFormat;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.*;
import org.knowm.xchart.style.colors.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;


/**
 * Creates a simple Chart using QuickChart
 */
public class Chart {

  Logger logger = LogManager.getLogger();

  public static void chart() throws Exception {

    double[] xData = new double[]{0.0, 0.5, 1.0, 1.5, 2.0};
    double[] yData = new double[]{2.0, 1.2, 1.0, 0.8, 0.0};

    // Create Chart
    XYChart chart = QuickChart.getChart("mood board", "time", "mood", "y(x)", xData, yData);
    chart.getStyler().setPlotBackgroundColor(ChartColor.getAWTColor(ChartColor.BLACK));

		BitmapEncoder.saveBitmap(chart, "./pic", BitmapFormat.PNG);
    
    // Show it
    new SwingWrapper(chart).displayChart();
    
  }
  public void chart(double[] xData, double[] yData) throws Exception {

    // Create Chart
    XYChart chart = QuickChart.getChart("mood board", "time", "mood", "y(x)", xData, yData);
    chart.getStyler().setPlotBackgroundColor(ChartColor.getAWTColor(ChartColor.BLACK));

    BitmapEncoder.saveBitmap(chart, "./pic", BitmapFormat.PNG);

    logger.info("x values: " + Arrays.toString(xData));
    logger.info("y values: " + Arrays.toString(yData));
    
    // Show it
    new SwingWrapper(chart).displayChart();
    
  }
}