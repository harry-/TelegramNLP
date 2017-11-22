package harry;

import java.time.*;
import java.time.temporal.IsoFields;
 
public class Date {

  public static void date() {
    // ZoneId zoneId = ZoneId.of ( "America/Montreal" );

    ZonedDateTime now = ZonedDateTime.now ( );
    int weekOfYear = now.get ( IsoFields.WEEK_OF_WEEK_BASED_YEAR );
    int weekBasedYear = now.get ( IsoFields.WEEK_BASED_YEAR );
    System.out.println ( "weekOfYear: " + weekOfYear + " of weekBasedYear: " + weekBasedYear );
  }
}
 