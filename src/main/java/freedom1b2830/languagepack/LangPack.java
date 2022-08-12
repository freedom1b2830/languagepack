package freedom1b2830.languagepack;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class LangPack {
    public static final String currentLang = Locale.getDefault().getLanguage();
    public static final String fallbackLang = "en";
    public static final File externalLangPackFile = new File("LANGPACK.txt");
    public static final File langPackTODOFile = new File("LANGPACK.TODO.txt");
    private static Thread.UncaughtExceptionHandler uncaughtExceptionHandler;

    public static Thread.UncaughtExceptionHandler getUncaughtExceptionHandler() {
        return uncaughtExceptionHandler;
    }

    public static void setUncaughtExceptionHandler(Thread.UncaughtExceptionHandler uncaughtExceptionHandler) {
        LangPack.uncaughtExceptionHandler = uncaughtExceptionHandler;
    }

    private static final ConcurrentHashMap<String, String> data = new ConcurrentHashMap<>();

    static {
        load();
    }

    public static void load() {
        try (InputStream inputStream = LangPack.class.getClassLoader().getResourceAsStream(externalLangPackFile.getName());) {
            if (inputStream != null) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                    while (true) {
                        String line = reader.readLine();
                        if (Objects.isNull(line)) {
                            break;
                        }
                        appendToData(line);
                    }
                }
            }
        } catch (IOException e) {
            if (Objects.nonNull(getUncaughtExceptionHandler())) {
                uncaughtExceptionHandler.uncaughtException(Thread.currentThread(), e);
            } else {
                e.printStackTrace();
            }
        }
        try {
            if (externalLangPackFile.exists()) {
                Files.readAllLines(externalLangPackFile.toPath(), StandardCharsets.UTF_8).forEach(LangPack::appendToData);
            }
        } catch (IOException e) {
            if (Objects.nonNull(getUncaughtExceptionHandler())) {
                uncaughtExceptionHandler.uncaughtException(Thread.currentThread(), e);
            } else {
                e.printStackTrace();
            }
        }
        //TODO LOAD FROM FILE
    }

    private static void appendToData(String line) {
        String[] pair = line.split("=", 2);
        String key = pair[0];
        if (data.containsKey(key)) {
            return;
        }
        String value = pair[1];
        data.putIfAbsent(key, value);
    }

    private LangPack() {
    }

    public static String getTextForKey(String key) {
        return getTextForKey(currentLang, key);
    }

    public static String getTextForKey(String lang, String key) {
        String convertedKey = convertKey(key, lang);
        String err;
        if (data.containsKey(convertedKey)) {
            return data.get(convertedKey);
        }
        err = getErrorMsgForKeys(convertedKey);
        String convertedFallbackKey = convertKey(key, fallbackLang);
        if (data.containsKey(convertedFallbackKey)) {
            writeTODO(err);
            return data.get(convertedFallbackKey);
        }
        err = getErrorMsgForKeys(convertedKey, convertedFallbackKey);
        writeTODO(err);
        throw new NoSuchElementException(err);
    }

    private static void writeTODO(String err) {
        try {
            if (!langPackTODOFile.isFile()) {
                Files.createFile(langPackTODOFile.toPath());
            }
            Files.write(langPackTODOFile.toPath(), (err + '\n').getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
        } catch (Exception e) {
            if (Objects.nonNull(getUncaughtExceptionHandler())) {
                uncaughtExceptionHandler.uncaughtException(Thread.currentThread(), e);
            } else {
                e.printStackTrace();
            }
        }
    }

    private static String getErrorMsgForKeys(String... keys) {
        StringBuilder errorMsg = new StringBuilder();
        errorMsg.append("write text for ");
        AtomicInteger index = new AtomicInteger();
        Arrays.stream(keys).forEachOrdered(key -> {
            errorMsg.append(key);
            if (index.get() < keys.length - 1) {
                errorMsg.append(" , ");
            }
            index.getAndIncrement();
        });
        errorMsg.append(" and place in resource dir(see maven) or in file: ").append(externalLangPackFile.getAbsolutePath());
        return errorMsg.toString();
    }

    private static String convertKey(String key, String lang) {
        return (key + "-" + lang).toUpperCase();
    }
}