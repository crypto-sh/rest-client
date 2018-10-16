package ir.vasl.library.enums;

public enum EncodingType {
    NO_ENCODING(0),
    GZIP(1);

    private final int value;

    private EncodingType(int value) {
        this.value = value;
    }

    public int getCode() {
        return this.value;
    }

    public static EncodingType Parse(int value) {
        if (value == 0) {
            return NO_ENCODING;
        }
        EncodingType[] arr$ = values();
        for (EncodingType val : arr$) {
            if (val.value == value) {
                return val;
            }
        }
        return NO_ENCODING;
    }
}
