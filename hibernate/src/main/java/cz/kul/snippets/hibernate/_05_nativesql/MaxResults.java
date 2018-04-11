package cz.kul.snippets.hibernate._05_nativesql;

public class MaxResults {

    public static MaxResults ZERO = new MaxResults(0);
    public static MaxResults ONE = new MaxResults(1);
    public static MaxResults TEN = new MaxResults(10);
    public static MaxResults ONE_HUNDRED = new MaxResults(100);
    public static MaxResults ONE_THOUSAND = new MaxResults(1000);
    public static MaxResults NO_LIMIT = new MaxResults(-1);

    private int maxResults;

    private MaxResults(int maxResults) {
        this.maxResults = maxResults;
    }

    public static MaxResults create(int limit) {
        if (limit < 0) {
            throw new IllegalArgumentException("Do not use negative values. For unlimited results use NO_LIMIT");
        }
        if (limit == 0) {
            throw new IllegalArgumentException("Use ZERO or NO_LIMIT");
        }
        return new MaxResults(limit);
    }
}
