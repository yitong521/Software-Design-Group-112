package nl.vu.cs.softwaredesign;

import com.jacob.activeX.ActiveXComponent;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;

public class Pronunciation {
    private static Integer VOLUME = 100;
    private static Integer READ_SPEED = 0;

    public static void main(String[] args) {
        String str = "hello";
        str2voice(str);
    }
    public static void str2voice(String str) {
        ActiveXComponent ax = new ActiveXComponent("Sapi.SpVoice");
        Dispatch spVoice = ax.getObject();
        ax.setProperty("Volume", new Variant(VOLUME));
        ax.setProperty("Rate", new Variant(READ_SPEED));
        Dispatch.call(spVoice, "Speak", new Variant(str));

    }
}