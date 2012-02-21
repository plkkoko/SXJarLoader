package it.rokudo.commons.tools.SXJarLoader;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;

public class SXJarLoader {
	static public final String libPrefix = "lib/";
	static public final String libRoot = "./" + libPrefix;

	public static void main(String[] args) throws ClassNotFoundException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException, IOException
	{
		ClassLoader cl = Thread.currentThread().getContextClassLoader();

		InputStream is = cl.getResourceAsStream("META-INF/MANIFEST.MF");
		if (is == null)
			exitWithErr("Missing manifest file: META-INF/MANIFEST.MF");

		Manifest manifest = new Manifest(is);
		Attributes mainAttribs = manifest.getMainAttributes();
		String mainClass = mainAttribs.getValue("Real-Main-Class");
		if (mainClass == null)
			exitWithErr("Missing attributes 'Real-Main-Class' in Manifest");

		URL.setURLStreamHandlerFactory(new RsrcURLStreamHandlerFactory(cl));

//		String jarPath = SXJarLoader.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		URL jarlocation = SXJarLoader.class.getProtectionDomain().getCodeSource().getLocation();

		ArrayList<URL> aURL = new ArrayList<URL>();
		aURL.add(new URL("rsrc:./"+libPrefix));

		JarInputStream jarFile = new JarInputStream (jarlocation.openStream());
		JarEntry jarEntry;
		while (true) {
			jarEntry = jarFile.getNextJarEntry();
			if (jarEntry == null)
				break;
			String entryName = jarEntry.getName();
			if (entryName.startsWith(libPrefix)) {
				aURL.add(new URL("jar:rsrc:" + entryName + "!/"));
			}
		}
		
		URL[] rsrcUrls = aURL.toArray(new URL[aURL.size()]);

		ClassLoader newClassLoader = new URLClassLoader(rsrcUrls, null);
		Thread.currentThread().setContextClassLoader(newClassLoader);

		Class<?> c = Class.forName(mainClass, true, newClassLoader);
		Method main = c.getMethod("main", new Class[]{args.getClass()});
		main.invoke((Object)null, new Object[]{args});
	}

	private static void exitWithErr(String err)
	{
		System.err.println(err);
		System.exit(-1);
	}

}