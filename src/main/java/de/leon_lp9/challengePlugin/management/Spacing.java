package de.leon_lp9.challengePlugin.management;

public enum Spacing {
    POSITIVE128PIXEl("\uDB00\uDC80"),
    POSITIVE64PIXEl("\uDB00\uDC40"),
    POSITIVE32PIXEl("\uDB00\uDC20"),
    POSITIVE16PIXEl("\uDB00\uDC10"),
    POSITIVE8PIXEl("\uDB00\uDC08"),
    POSITIVE4PIXEl("\uDB00\uDC04"),
    POSITIVE2PIXEl("\uDB00\uDC02"),
    POSITIVE1PIXEl("\uDB00\uDC01"),
    ZEROPIXEl("\uDB00\uDC00"),
    NEGATIVE128PIXEl("\uDAFF\uDF80"),
    NEGATIVE64PIXEl("\uDAFF\uDFC0"),
    NEGATIVE32PIXEl("\uDAFF\uDFE0"),
    NEGATIVE16PIXEl("\uDAFF\uDFF0"),
    NEGATIVE8PIXEl("\uDAFF\uDFF8"),
    NEGATIVE4PIXEl("\uDAFF\uDFFC"),
    NEGATIVE2PIXEl("\uDAFF\uDFFE"),
    NEGATIVE1PIXEl("\uDAFF\uDFFF");

    private final String spacing;

    Spacing(String spacing) {
        this.spacing = spacing;
    }

    public String getSpacing() {
        return spacing;
    }

}
