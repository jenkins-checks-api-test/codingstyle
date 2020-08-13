package edu.hm.hafner.util;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests the class {@link FilteredLog}.
 *
 * @author Ullrich Hafner
 */
class FilteredLogTest {
    private static final String TITLE = "Title: ";

    @Test
    void shouldSkipAdditionalErrors() {
        FilteredLog filteredLog = new FilteredLog(TITLE, 5);

        filteredLog.logError("1");
        filteredLog.logError("2");
        filteredLog.logError("3");
        filteredLog.logError("4");
        filteredLog.logError("5");
        filteredLog.logError("6");
        filteredLog.logError("7");

        assertThatExactly5MessagesAreLogged(filteredLog);

        filteredLog.logSummary();

        assertThat(filteredLog.getErrorMessages()).containsExactly(TITLE, "1", "2", "3", "4", "5",
                "  ... skipped logging of 2 additional errors ...");
        assertThat(filteredLog.size()).isEqualTo(7);
    }

    private void assertThatExactly5MessagesAreLogged(final FilteredLog filteredLog) {
        assertThat(filteredLog.getErrorMessages()).containsExactly(TITLE, "1", "2", "3", "4", "5");
    }

    @Test
    void shouldLogExceptions() {
        FilteredLog filteredLog = new FilteredLog(TITLE, 1);

        filteredLog.logException(new IllegalArgumentException("Cause"), "Message");
        filteredLog.logException(new IllegalArgumentException(""), "Message");

        assertThat(filteredLog.getErrorMessages()).contains(TITLE,
                "Message", "java.lang.IllegalArgumentException: Cause",
                "\tat edu.hm.hafner.util.FilteredLogTest.shouldLogExceptions(FilteredLogTest.java:71)");
    }

    @Test
    void shouldLog20ErrorsByDefault() {
        FilteredLog filteredLog = new FilteredLog(TITLE);

        for (int i = 0; i < 25; i++) {
            filteredLog.logError("error" + i);
            filteredLog.logInfo("info" + i);
        }
        assertThat(filteredLog.getErrorMessages()).hasSize(21).contains("error19").doesNotContain("error20");
        assertThat(filteredLog.getInfoMessages()).hasSize(25).contains("info0").contains("info24");
    }
}
