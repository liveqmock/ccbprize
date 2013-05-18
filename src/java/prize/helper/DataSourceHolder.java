package prize.helper;

/**
 * Created with IntelliJ IDEA.
 * User: zhanrui
 * Date: 13-5-17
 */
public class DataSourceHolder {
    private static DataSourceHolder ourInstance = new DataSourceHolder();

    public static DataSourceHolder getInstance() {
        return ourInstance;
    }

    private DataSourceHolder() {
    }
}
