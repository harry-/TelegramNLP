package harry;

// Imports the Google Cloud client library

import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.Document.Type;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;

public class GoogleCloud {
  public String getSent(String... args) throws Exception {

    String text = "";

    for (String s : args) {
        text += s;
    }

    try (LanguageServiceClient language = LanguageServiceClient.create()) {

      Document doc = Document.newBuilder()
          .setContent(text).setType(Type.PLAIN_TEXT).build();

      // Detects the sentiment of the text
      Sentiment sentiment = language.analyzeSentiment(doc).getDocumentSentiment();

     
      return("Sentiment: "+sentiment.getScore()+", "+sentiment.getMagnitude());
    }
    
  }
}