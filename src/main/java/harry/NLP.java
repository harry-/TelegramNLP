package harry;

// Imports the Google Cloud client library
//whatnow
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.Document.Type;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.logging.BotLogger;
import org.telegram.telegrambots.logging.BotsFileHandler;

import harry.NLPBot;

public class NLP {
  public static void main(String... args) throws Exception {
    // Instantiates a client



        ApiContextInitializer.init();

        TelegramBotsApi botsApi = new TelegramBotsApi();

        try {
            botsApi.registerBot(new NLPBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    


    try (LanguageServiceClient language = LanguageServiceClient.create()) {

      // The text to analyze
      String text = "Wacht auf, Verdammte dieser Erde,die stets man noch zum Hungern zwingt! Das Recht wie Glut im Kraterherde nun mit Macht zum Durchbruch dringt. Reinen Tisch macht mit dem Bedränger!Heer der Sklaven, wache auf! Ein Nichts zu sein, tragt es nicht längerAlles zu werden, strömt zuhauf!";

      Document doc = Document.newBuilder()
          .setContent(text).setType(Type.PLAIN_TEXT).build();

      // Detects the sentiment of the text
      Sentiment sentiment = language.analyzeSentiment(doc).getDocumentSentiment();

      System.out.printf("Text: %s%n", text);
      System.out.printf("Sentiment: %s, %s%n", sentiment.getScore(), sentiment.getMagnitude());
    }
  }
}