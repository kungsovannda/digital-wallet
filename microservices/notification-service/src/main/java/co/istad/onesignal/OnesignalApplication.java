package co.istad.onesignal;

import co.istad.onesignal.feature.mail.MailService;
import co.istad.onesignal.feature.mail.dto.MailRequest;
import com.onesignal.client.ApiClient;
import com.onesignal.client.Configuration;
import com.onesignal.client.api.DefaultApi;
import com.onesignal.client.auth.HttpBearerAuth;
import com.onesignal.client.model.CreateNotificationSuccessResponse;
import com.onesignal.client.model.LanguageStringMap;
import com.onesignal.client.model.Notification;
import com.onesignal.client.model.User;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.StringMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@RequiredArgsConstructor
public class OnesignalApplication {

    @Value("${onesignal.app-id}")
    private String APP_ID;
    private final DefaultApi api;
    private final MailService mailService;

    public static void main(String[] args) {
        SpringApplication.run(OnesignalApplication.class, args);
    }

    private Notification createNotification(){
        Notification notification = new Notification();
        notification.setAppId(APP_ID);

        notification.setIsChrome(true);
        notification.setIsFirefox(true);
        notification.setIsSafari(true);
        notification.setIsAnyWeb(true);

        notification.setIncludedSegments(Arrays.asList("All"));

        LanguageStringMap headings = new LanguageStringMap();
        headings.en("New Message");
        notification.setHeadings(headings);

        LanguageStringMap contents = new LanguageStringMap();
        contents.en("You have a new notification from your app!");
        notification.setContents(contents);

        notification.setChromeIcon("https://kuika.vercel.app/favicon.ico");

        notification.setUrl("https://kuika.vercel.app/");

        Map<String, Object> data = new HashMap<>();
        data.put("customKey", "customValue");
        notification.setData(data);

        return notification;
    }

    @Bean
    public CommandLineRunner commandLineRunner(){
        return args ->  {
            api.createNotification(createNotification());
            mailService.send(new MailRequest(
                    "onekiss.shit@gmail.com",
                    "Test",
                    "Hello this is the test",
                    false
            ));
        };
    }
}
