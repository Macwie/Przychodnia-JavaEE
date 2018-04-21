import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.swing.*;

import java.util.Hashtable;

import static org.junit.Assert.*;

/**
 * Created by Maciek on 2017-06-07.
 */
public class GUITest {

    private JFrame frame;
    private InitialContext ic;
    private Hashtable jndiProperties;

    @Before
    public void setUp() throws Exception {
        frame = new JFrame("Przychodnia");
        jndiProperties = new Hashtable();
        jndiProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY,"org.jboss.naming.remote.client.InitialContextFactory");
        jndiProperties.put(Context.PROVIDER_URL, "http-remoting://localhost:8080");

        ic = new InitialContext(jndiProperties);

    }

    @After
    public void tearDown() throws Exception {
        frame = null;
        jndiProperties = null;
        ic = null;
    }

    @Test
    public void main() throws Exception {

        Assert.assertNotNull(frame);
        Assert.assertNotNull(jndiProperties);
        Assert.assertNotNull(ic);

    }

}