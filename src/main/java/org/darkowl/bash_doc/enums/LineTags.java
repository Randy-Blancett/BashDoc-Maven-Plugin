package org.darkowl.bash_doc.enums;

public enum LineTags {
    AUTHOR("AUTHOR ", MatchTypes.START, 4),
    AUTHOR_EMAIL("AUTHOR_EMAIL ", MatchTypes.START, 4),
    CODE(null, null, 2),
    COMMENT(null, null, 100),
    EXAMPLES("EXAMPLES", MatchTypes.EQ, 2),
    EXIT_CODES("EXIT_CODES", MatchTypes.EQ, 2),
    FILE("FILE", MatchTypes.EQ, 1),
    HISTORIC_VERSION("V ", MatchTypes.START, 3),
    METHOD("METHOD", MatchTypes.EQ, 1),
    PARAMETERS("PARAMETERS", MatchTypes.EQ, 2),
    PRIVATE("PRIVATE", MatchTypes.EQ, 2),
    PROTECTED("PROTECTED", MatchTypes.EQ, 2),
    PUBLIC("PUBLIC", MatchTypes.EQ, 2),
    RELEASE("RELEASE ", MatchTypes.START, 4),
    RETURN("RETURN", MatchTypes.EQ, 2),
    VARIABLE("VARIABLE", MatchTypes.EQ, 1),
    VERSION("VERSION ", MatchTypes.START, 2),
    VERSIONS("VERSIONS", MatchTypes.EQ, 2);

    public static LineTags parse(final String line) {
        if (!line.trim().startsWith("#"))
            return CODE;

        final String check = line.trim().replaceAll("^#", "").trim();

        if (check.isBlank())
            return COMMENT;
        for (final LineTags tag : LineTags.values()) {
            if (tag.tag == null)
                continue;
            switch (tag.type) {
            case EQ:
                if (tag.tag.equals(check))
                    return tag;
                break;
            case START:
                if (check.startsWith(tag.tag))
                    return tag;
                break;
            }
        }
        return COMMENT;
    }

    private final int level;
    private final String tag;

    private final MatchTypes type;

    LineTags(final String tag, final MatchTypes type, final int level) {
        this.tag = tag;
        this.type = type;
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public String stripTag(final String line) {
        return line.trim().replaceAll("^#", "").trim().replaceAll("^" + tag, "").trim();
    }

}
