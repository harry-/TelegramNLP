package harry;

// Import the Google Cloud client library

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
import java.util.List;

public class GoogleCloud {

  public String getSent(String text) throws Exception {
    try (LanguageServiceClient language = LanguageServiceClient.create()) {

      Document doc = Document.newBuilder()
          .setContent(text).setType(Type.PLAIN_TEXT).build();

      // Detect the sentiment of the text
      Sentiment sentiment = language.analyzeSentiment(doc).getDocumentSentiment();

      return("Sentiment: "+sentiment.getScore()+", "+sentiment.getMagnitude());
    }
  }

  public String getEnt(String text) throws Exception {
    String answer = "";

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

  public String getEntSent(String text) throws Exception {
    String answer = "";

    // Instantiate a beta client : com.google.cloud.language.v1beta2.LanguageServiceClient
    try (LanguageServiceClient language = LanguageServiceClient.create()) {
      Document doc = Document.newBuilder()
          .setContent(text).setType(Type.PLAIN_TEXT).build();
      AnalyzeEntitySentimentRequest request = AnalyzeEntitySentimentRequest.newBuilder()
          .setDocument(doc)
          .setEncodingType(EncodingType.UTF16).build();
      
      // detect entity sentiments in the given string
      AnalyzeEntitySentimentResponse response = language.analyzeEntitySentiment(request);
      
      // aggregate the response
      for (Entity entity : response.getEntitiesList()) {
        answer += "\n\nEntity: " + entity.getName();
        answer += "\nSalience: " + entity.getSalience();
        answer += "\nScore: " + entity.getSentiment().getScore();
        answer += "\nMagnitude: " + entity.getSentiment().getMagnitude();
        for (EntityMention mention : entity.getMentionsList()) {
          answer += "\nBegin offset: " + mention.getText().getBeginOffset();
          answer += "\nContent: " + mention.getText().getContent();
          answer += "\nMagnitude: " + mention.getSentiment().getMagnitude();
          answer += "\nSentiment score : " + mention.getSentiment().getScore();
          answer += "\nType: " + mention.getType();
        }
      }
       return answer;
    }
  }

  public List getEntSent2(String text) throws Exception {
    // Instantiate a beta client : com.google.cloud.language.v1beta2.LanguageServiceClient
    try (LanguageServiceClient language = LanguageServiceClient.create()) {
      Document doc = Document.newBuilder()
          .setContent(text).setType(Type.PLAIN_TEXT).build();
      AnalyzeEntitySentimentRequest request = AnalyzeEntitySentimentRequest.newBuilder()
          .setDocument(doc)
          .setEncodingType(EncodingType.UTF16).build();
      
      // detect entity sentiments in the given string
      AnalyzeEntitySentimentResponse response = language.analyzeEntitySentiment(request);
      
      return response.getEntitiesList();    
    }
  }
}