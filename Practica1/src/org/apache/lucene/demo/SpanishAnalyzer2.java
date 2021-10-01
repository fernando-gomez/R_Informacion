//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.apache.lucene.demo;

import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.StopwordAnalyzerBase;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.WordlistLoader;
import org.apache.lucene.analysis.Analyzer.TokenStreamComponents;
import org.apache.lucene.analysis.miscellaneous.SetKeywordMarkerFilter;
import org.apache.lucene.analysis.snowball.SnowballFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.util.IOUtils;

public final class SpanishAnalyzer2 extends StopwordAnalyzerBase {
    private final CharArraySet stemExclusionSet;
    public static final String DEFAULT_STOPWORD_FILE = "spanish_stop.txt";

    public static CharArraySet createStopSet2(){
        String[] stopWords = {"el", "la", "lo", "en"};
        CharArraySet stopSet = StopFilter.makeStopSet(stopWords);
        return stopSet;
    }

    public static CharArraySet createStopSet3(){
        try {
            CharArraySet stopSet = WordlistLoader.getSnowballWordSet(
                    new FileReader("spanish_stop.txt"));
            return stopSet;
        } catch (IOException ex) {
            throw new RuntimeException("Unable to load default stopword set");
        }
    }

    public static CharArraySet getDefaultStopSet() {
        return SpanishAnalyzer2.DefaultSetHolder.DEFAULT_STOP_SET;
    }

    public SpanishAnalyzer2() {
        this(SpanishAnalyzer2.DefaultSetHolder.DEFAULT_STOP_SET);
    }

    public SpanishAnalyzer2(CharArraySet stopwords) {
        this(stopwords, CharArraySet.EMPTY_SET);
    }

    public SpanishAnalyzer2(CharArraySet stopwords, CharArraySet stemExclusionSet) {
        super(stopwords);
        this.stemExclusionSet = CharArraySet.unmodifiableSet(CharArraySet.copy(stemExclusionSet));
    }

    protected TokenStreamComponents createComponents(String fieldName) {
        final Tokenizer source = new StandardTokenizer();
        TokenStream result = new LowerCaseFilter(source);
        result = new StopFilter(result, stopwords);
        if(!stemExclusionSet.isEmpty())
            result = new SetKeywordMarkerFilter(result, stemExclusionSet);
        result = new SnowballFilter(result, "Spanish");
        return new TokenStreamComponents(source, result);
    }

    protected TokenStream normalize(String fieldName, TokenStream in) {
        return new LowerCaseFilter(in);
    }

    private static class DefaultSetHolder {
        static final CharArraySet DEFAULT_STOP_SET;

        private DefaultSetHolder() {
        }

        static {
            try {
                DEFAULT_STOP_SET = WordlistLoader.getSnowballWordSet(IOUtils.getDecodingReader(SnowballFilter.class, "spanish_stop.txt", StandardCharsets.UTF_8));
            } catch (IOException var1) {
                throw new RuntimeException("Unable to load default stopword set");
            }
        }
    }
}
