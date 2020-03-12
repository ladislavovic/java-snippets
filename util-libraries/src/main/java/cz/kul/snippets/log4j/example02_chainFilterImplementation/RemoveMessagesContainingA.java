package cz.kul.snippets.log4j.example02_chainFilterImplementation;

import org.apache.log4j.varia.StringMatchFilter;

@Annotation1
public class RemoveMessagesContainingA extends StringMatchFilter {

    public RemoveMessagesContainingA() {
        setStringToMatch("A");
        setAcceptOnMatch(false);
    }
    
}
