package features;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateUserInput {
	public static boolean isValidPhoneNumber(String phoneNumber) {
        String phoneRegex = "^[2-9]\\d{2}-\\d{3}-\\d{4}$";
        Pattern pattern = Pattern.compile(phoneRegex);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

    public static boolean isValidEmailAddress(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isValidPostalCode(String postalCode) {
        String postalCodeRegex = "^[ABCEGHJKLMNPRSTVXY][0-9][A-Z] [0-9][A-Z][0-9]$";
        Pattern pattern = Pattern.compile(postalCodeRegex);
        Matcher matcher = pattern.matcher(postalCode);
        return matcher.matches();
    }

    public static boolean isValidWebsite(String websiteUrl) {
        String regex = "^(https?|ftp):\\/\\/www\\.[\\w\\d\\-]+(\\.[\\w\\d\\-]+)+([\\w\\d\\-.,@?^=%&:/~+#]*[\\w\\d\\-@?^=%&/~+#])?$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(websiteUrl);

        return matcher.matches();
    }
}
