package harry;

// Imports the Google Cloud client library

import com.google.cloud.language.v1.AnalyzeEntitiesRequest;
import com.google.cloud.language.v1.AnalyzeEntitiesResponse;
import com.google.cloud.language.v1.AnalyzeEntitySentimentRequest;
import com.google.cloud.language.v1.AnalyzeEntitySentimentResponse;
import com.google.cloud.language.v1.AnalyzeSentimentResponse;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.Document.Type;
import com.google.cloud.language.v1.EncodingType;
import com.google.cloud.language.v1.Entity;
import com.google.cloud.language.v1.EntityMention;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;
import com.google.cloud.language.v1.Token;

import java.util.Map;

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

  public String getEnt(String... args) throws Exception {

    String text = "";
    String answer = "";

    for (String s : args) {
        text += s;
    }

    // Instantiate the Language client com.google.cloud.language.v1.LanguageServiceClient
    try (LanguageServiceClient language = LanguageServiceClient.create()) {
      Document doc = Document.newBuilder()
          .setContent(text)
          .setType(Type.PLAIN_TEXT)
          .build();
      AnalyzeEntitiesRequest request = AnalyzeEntitiesRequest.newBuilder()
          .setDocument(doc)
          .setEncodingType(EncodingType.UTF16)
          .build();

      AnalyzeEntitiesResponse response = language.analyzeEntities(request);
      
      // Return the response
      for (Entity entity : response.getEntitiesList()) {
        answer += "\n\nEntity: " + entity.getName();
        answer += "\nSalience: " + entity.getSalience();

        for (Map.Entry<String, String> entry : entity.getMetadataMap().entrySet()) {
          answer += "\n" + entry.getKey() + ": " + entry.getValue();
        }
        for (EntityMention mention : entity.getMentionsList()) {
          answer +="\nBegin offset: " + mention.getText().getBeginOffset();
          answer +="\nContent: " +  mention.getText().getContent();
          answer +="\nType:" + mention.getType();
        }
      }
    }
    return answer;
  }
}