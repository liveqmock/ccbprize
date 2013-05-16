package java.test;

import freemarker.template.Configuration;

import java.io.File;

/**
 * User: zhanrui
 * Date: 13-4-15
 * Time: обнГ2:55
 */
public class FreemakerTest {
    public static void main(String[] args) throws Exception {
        Configuration cfg = new Configuration();
        cfg.setDirectoryForTemplateLoading(new File("./"));
    }
}
