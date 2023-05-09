package com.example.service_ui.constants;

public class ExplanationResDTO {
    private String comparedText;
    private String positiveSummary;

    public String getNegativeSummary() {
        return negativeSummary;
    }

    public void setNegativeSummary(String negativeSummary) {
        this.negativeSummary = negativeSummary;
    }

    private String negativeSummary;

    public String getComparedText() {
        return comparedText;
    }

    public void setComparedText(String comparedText) {
        this.comparedText = comparedText;
    }

    public String getPositiveSummary() {
        return positiveSummary;
    }

    public void setPositiveSummary(String positiveSummary) {
        this.positiveSummary = positiveSummary;
    }
}
