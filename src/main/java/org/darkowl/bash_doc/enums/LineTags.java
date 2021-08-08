package org.darkowl.bash_doc.enums;

public enum LineTags {
    AUTHOR("AUTHOR ", MatchTypes.START, 4), AUTHOR_EMAIL("AUTHOR_EMAIL ", MatchTypes.START, 4), CODE(null, null, 2),
    COMMENT(null, null, 100), FILE("FILE", MatchTypes.EQ, 1), HISTORIC_VERSION("V ", MatchTypes.START, 3),
    PRIVATE("PRIVATE", MatchTypes.EQ, 2), PROTECTED("PROTECTED", MatchTypes.EQ, 2), PUBLIC("PUBLIC", MatchTypes.EQ, 2),
    RELEASE("RELEASE ", MatchTypes.START, 4), VARIABLE("VARIABLE", MatchTypes.EQ, 1),
    VERSION("VERSION ", MatchTypes.START, 2), VERSIONS("VERSIONS", MatchTypes.EQ, 2);

    public static LineTags parse(final String line) {
        if (!line.trim().startsWith("#"))
            return CODE;

        final String check = line.trim().replaceAll("^#", "").trim();

        if (check.isBlank())
            return COMMENT;
        for (final LineTags tag : LineTags.values()) {
            if (tag.tag == null)
                continue;
            if (MatchTypes.EQ == tag.type) {
                if (tag.tag.equals(check))
                    return tag;
            } else if (MatchTypes.START == tag.type)
                if (check.startsWith(tag.tag))
                    return tag;
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
