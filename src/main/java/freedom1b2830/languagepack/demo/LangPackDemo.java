package freedom1b2830.languagepack.demo;

import freedom1b2830.languagepack.LangPack;

public class LangPackDemo {


    public static void main(String[] args) {
        LangPackDemo demo = new LangPackDemo();
        demo.exec();
    }

    private static String[] createDefaultLangPair() {
        String current = LangPack.currentLang;
        String fallback = LangPack.fallbackLang;
        String langNotExist = "AEEE";
        if (current.equalsIgnoreCase(fallback)) {
            return new String[]{current, langNotExist};
        }
        return new String[]{current, fallback, langNotExist};
    }

    private static final String[] keys = new String[]{"HELP", "TODO", "EXEC"};
    private static final String[] langs = createDefaultLangPair();


    private void exec() {
        for (String lang : langs) {
            for (String key : keys) {
                try {
                    String msg1 = LangPack.getTextForKey(key);
                    String msg2 = LangPack.getTextForKey(lang, key);
                    System.out.println(String.format("without %s lang: %s", lang, msg1));
                    System.out.println(String.format("with %s lang: %s", lang, msg2));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
