# languagepack
A simple utility to display text depending on the user's language (data: key=translation)

## usage
in java code:
```
import freedom1b2830.languagepack.LangPack;


String lang="ru";
String key="WORD";
String msg1 = LangPack.getTextForKey(key);
String msg2 = LangPack.getTextForKey(lang, key);

System.out.println(String.format("lang %s word:%s", lang, msg1));
System.out.println(String.format("word:%s", msg2));

```

in ```src/main/resources/LANGPACK.txt``` OR in the directory where the jar is running (see ```PWD```)


```
WORD-ru=Слово
WORD-en=WoRd
```
the ```LANGPACK.TODO.txt``` file contains notifications about the need to translate the text, as well as the ```necessary keys``` (the word before the symbol ```=```)
