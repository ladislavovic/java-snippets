package cz.kul.snippets.java.generics;

public class StaticGenericMethod
{

    public static <T> String bar() {
        Provider<T> provider = new Provider<T>()
        {

        };
        return provider.toString();
    }

    public static String baz() {
        Provider<String> provider = new Provider<String>()
        {

        };
        return provider.toString();
    }


    public interface Provider<SettingsType> {

        default SettingsType getSettings() {
            return null;
        }

    }

    private interface Provider2<SettingsType>
    {

    }

}
