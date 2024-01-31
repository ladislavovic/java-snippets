package cz.kul.snippets.benchmark.psql;

public class TableResults {

  private long tableSizeBytes;

  private long pkeySizeBytes;

  public long getTableSizeBytes() {
    return tableSizeBytes;
  }

  public void setTableSizeBytes(long tableSizeBytes) {
    this.tableSizeBytes = tableSizeBytes;
  }

  public long getPkeySizeBytes() {
    return pkeySizeBytes;
  }

  public void setPkeySizeBytes(long pkeySizeBytes) {
    this.pkeySizeBytes = pkeySizeBytes;
  }

  public double getTableSizeMB() {
    return getTableSizeBytes() / (double) (1024 * 1024);
  }

  public double getPkeySizeMB() {
    return getPkeySizeBytes() / (double) (1024 * 1024);
  }

}
