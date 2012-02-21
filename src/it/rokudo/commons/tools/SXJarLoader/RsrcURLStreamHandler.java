package it.rokudo.commons.tools.SXJarLoader;

import java.io.IOException;
import java.net.URL;

/**
 * Handle URLs with protocol "rsrc". "rsrc:path/file.ext" identifies the content accessible as
 * classLoader.getResourceAsStream("path/file.ext"). "rsrc:path/" identifies a base-path for
 * resources to be searched. The spec "file.ext" is combined to "rsrc:path/file.ext".
 * 
 * @since 3.5
 */
public class RsrcURLStreamHandler extends java.net.URLStreamHandler {

	private ClassLoader classLoader;

	public RsrcURLStreamHandler(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	protected java.net.URLConnection openConnection(URL u) throws IOException
	{
		return new RsrcURLConnection(u, classLoader);
	}

	protected void parseURL(URL url, String spec, int start, int limit)
	{
		String file;
		if (spec.startsWith("rsrc:"))
			file = spec.substring(5);
		else if (url.getFile().equals(SXJarLoader.libRoot))
			file = spec;
		else if (url.getFile().endsWith("/"))
			file = url.getFile() + spec;
		else 
			file = spec;
		setURL(url, "rsrc", "", -1, null, null, file, null, null);
	}

}