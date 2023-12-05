package features;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateUserInput {
	public static boolean isValidPhoneNumber(String phoneNumberPassedFromTheMainFunction) {
        String phoneRegex = "^[2-9]\\d{2}-\\d{3}-\\d{4}$";
        Pattern patternForPhoneNumber = Pattern.compile(phoneRegex);
        Matcher matcherForPhoneNumber = patternForPhoneNumber.matcher(phoneNumberPassedFromTheMainFunction);
        return matcherForPhoneNumber.matches();
    }

    public static boolean isValidEmailAddress(String emailAddressPassedFromTheMainFunction) {
        String emailRegexToBeFollowed = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern patternForEmailAddress = Pattern.compile(emailRegexToBeFollowed);
        Matcher matcherForEmailAddress = patternForEmailAddress.matcher(emailAddressPassedFromTheMainFunction);
        return matcherForEmailAddress.matches();
    }

    public static boolean isValidPostalCode(String postalCodePassedFromTheMainFunction) {
        String postalCodeRegex = "^[ABCEGHJKLMNPRSTVXY][0-9][A-Z] [0-9][A-Z][0-9]$";
        Pattern patternForPostalCode = Pattern.compile(postalCodeRegex);
        Matcher matcherForPostalCode = patternForPostalCode.matcher(postalCodePassedFromTheMainFunction);
        return matcherForPostalCode.matches();
    }

    public static boolean isValidWebsite(String websiteUrlPassedFromTheMainFunction) {
        String regexForWebsite = "^(https?|ftp):\\/\\/www\\.[\\w\\d\\-]+(\\.[\\w\\d\\-]+)+([\\w\\d\\-.,@?^=%&:/~+#]*[\\w\\d\\-@?^=%&/~+#])?$";
        Pattern patternForWebsite = Pattern.compile(regexForWebsite);
        Matcher matcherForWebsite = patternForWebsite.matcher(websiteUrlPassedFromTheMainFunction);

        return matcherForWebsite.matches();
    }
}
