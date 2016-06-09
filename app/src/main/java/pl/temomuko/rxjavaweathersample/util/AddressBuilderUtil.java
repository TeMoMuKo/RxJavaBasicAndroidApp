package pl.temomuko.rxjavaweathersample.util;


import pl.temomuko.rxjavaweathersample.Constants;

/**
 * Created by Rafal on 2015-07-14.
 */
public class AddressBuilderUtil {

    private AddressBuilderUtil() {
        throw new AssertionError();
    }

    public static String getIconAddress(String iconName) {
        if (iconName != null) {
            return Constants.ICON_BASE_URL.concat(iconName).concat(".png");
        }
        return null;
    }

}
