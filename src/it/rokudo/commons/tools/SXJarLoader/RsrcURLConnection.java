package it.rokudo.commons.tools.SXJarLoader;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;

public class RsrcURLConnection extends URLConnection {

	private ClassLoader classLoader;

	public RsrcURLConnection(URL url, ClassLoader classLoader) {
		super(url);
		this.classLoader = classLoader;
	}

	@Override
    public InputStream getInputStream() throws IOException
    {
        String file= URLDecoder.decode(url.getFile(), "UTF-8");
        InputStream result= classLoader.getResourceAsStream(file);
        if (result == null) {
            throw new MalformedURLException("Could not open InputStream for URL '" + url + "'");
        }
        return result;
    }

	@Override
	public void connect() throws IOException {}


}