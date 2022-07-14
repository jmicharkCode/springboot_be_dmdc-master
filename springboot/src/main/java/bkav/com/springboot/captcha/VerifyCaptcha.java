package bkav.com.springboot.captcha;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.json.Json;
import javax.json.JsonReader;
import javax.json.JsonObject;
import javax.net.ssl.HttpsURLConnection;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

public class VerifyCaptcha {

    private static final Logger logger = LoggerFactory.getLogger(VerifyCaptcha.class);

    @Value("${captcha.secret_key}")
    private static String secretKey;

    public static final String SITE_VERIFY_URL = //
            "https://www.google.com/recaptcha/api/siteverify";

    public static boolean verify(String gRecaptchaResponse) {
        if (gRecaptchaResponse == null || gRecaptchaResponse.length() == 0) {
            return false;
        }
        try {
            URL verifyUrl = new URL(SITE_VERIFY_URL);
            // Mở một kết nối (Connection) tới URL trên.
            HttpsURLConnection conn = (HttpsURLConnection) verifyUrl.openConnection();
            // Thêm các thông tin Header vào Request chuẩn bị gửi tới server.
            conn.setRequestMethod("POST");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");
            conn.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            // Dữ liệu sẽ gửi tới Server.
            String postParams = "secret=" + secretKey
                    + "&response=" + gRecaptchaResponse;

            // Send Request
            conn.setDoOutput(true);

            // Lấy Output Stream (Luồng đầu ra) của kết nối tới Server.
            // Ghi dữ liệu vào Output Stream, có nghĩa là gửi thông tin đến Server.
            OutputStream outStream = conn.getOutputStream();
            outStream.write(postParams.getBytes());

            outStream.flush();
            outStream.close();

            // Mã trả lời được trả về từ Server.
            int responseCode = conn.getResponseCode();
            logger.info("responseCode=" + responseCode);

            // Lấy Input Stream (Luồng đầu vào) của Connection
            // để đọc dữ liệu gửi đến từ Server.
            InputStream is = conn.getInputStream();
            logger.info("is " + is);

            JsonReader jsonReader = Json.createReader(is);
            JsonObject jsonObject = jsonReader.readObject();
            jsonReader.close();

            // ==> {"success": true}
            logger.info("Response: " + jsonObject);

            boolean success = jsonObject.getBoolean("success");
            logger.info("success " + success);
            is.close();
            conn.disconnect();
            return success;
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("catch " + e.getMessage());
            return false;
        }
    }
}
