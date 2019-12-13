package eu.qanswer.mapping.configuration;

public abstract class CSVConfigurationFile {

    public char separator;

    public char getSeparator() {
        return separator;
    }

    public void setSeparator(char separator) {
        this.separator = separator;
    }
}
