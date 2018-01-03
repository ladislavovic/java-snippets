package cz.kul.snippets.spring._01_xmlconfig_hw;

import static org.junit.Assert.assertEquals;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Hello world Spring application.
 * 
 * It uses bean definition in xml. Creates a few beans and that's all.
 * 
 * @author Ladislav Kulhanek
 */
public class Main_SpringXMLConfigHw {

    public static void main(String[] args) {
        String configLocation = "cz/kul/snippets/spring/_01_xmlconfig_hw/spring.xml";

        try (ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(configLocation)) {
            
            WeatherConnector connector = (WeatherConnector) ctx.getBean("WC");
            assertEquals(WeatherConnector.class, connector.getClass());
    
            PrototypeBean p1 = (PrototypeBean) ctx.getBean("PB", "foo"); // This automatically found appropriate constructor
            assertEquals("foo", p1.getMsg());
        }

    }

    public static class WeatherConnector {

        private String url;

        private String login;

        private String password;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

    }

    /**
     * Another Spring bean.
     * 
     * It has prototype scope, it means it is not singleton but new instance is always
     * created.
     * 
     */
    public static class PrototypeBean {

        private String msg;

        public PrototypeBean(String msg) {
            this.msg = msg;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

    }

}
