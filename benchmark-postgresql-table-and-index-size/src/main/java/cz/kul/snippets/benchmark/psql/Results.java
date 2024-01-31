package cz.kul.snippets.benchmark.psql;

public class Results {

  NumTableParams numTableParams = new NumTableParams();

  StrTableParams strTableParams = new StrTableParams();

  NormalTableParams normalTableParams = new NormalTableParams();

  TableResults numTableResults = new TableResults();

  TableResults strTableResults = new TableResults();

  TableResults normalTableResults = new TableResults();

  public NumTableParams getNumTableParams() {
    return numTableParams;
  }

  public void setNumTableParams(NumTableParams numTableParams) {
    this.numTableParams = numTableParams;
  }

  public StrTableParams getStrTableParams() {
    return strTableParams;
  }

  public void setStrTableParams(StrTableParams strTableParams) {
    this.strTableParams = strTableParams;
  }

  public TableResults getNumTableResults() {
    return numTableResults;
  }

  public void setNumTableResults(TableResults numTableResults) {
    this.numTableResults = numTableResults;
  }

  public TableResults getStrTableResults() {
    return strTableResults;
  }

  public void setStrTableResults(TableResults strTableResults) {
    this.strTableResults = strTableResults;
  }

  public TableResults getNormalTableResults() {
    return normalTableResults;
  }

  public void setNormalTableResults(TableResults normalTableResults) {
    this.normalTableResults = normalTableResults;
  }

  public NormalTableParams getNormalTableParams() {
    return normalTableParams;
  }

  public void setNormalTableParams(NormalTableParams normalTableParams) {
    this.normalTableParams = normalTableParams;
  }

}
