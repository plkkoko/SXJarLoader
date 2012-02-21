package it.rokudo.commons.tools.SXJarLoader;

import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;

public class RsrcURLStreamHandlerFactory implements URLStreamHandlerFactory {

    private URLStreamHandlerFactory chainFac;
    private RsrcURLStreamHandler urlHandler;
    
    public RsrcURLStreamHandlerFactory(ClassLoader cl) {
    	urlHandler = new RsrcURLStreamHandler(cl);
    }

    public URLStreamHandler createURLStreamHandler(String protocol)
    {
        if ("rsrc".equals(protocol))
            return urlHandler;
        if (chainFac != null)
            return chainFac.createURLStreamHandler(protocol);
        return null;
    }
    
    /**
     * Allow one other URLStreamHandler to be added.
     * URL.setURLStreamHandlerFactory does not allow
     * multiple factories to be added.
     * The chained factory is called for all other protocols,
     * except "rsrc". Use null to clear previously set Handler. 
     * @param fac another factory to be chained with ours.
     */
    public void setURLStreamHandlerFactory(URLStreamHandlerFactory fac)
    {
        chainFac = fac;
    }
    
}