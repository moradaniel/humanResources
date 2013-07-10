package org.dpi.util.locale;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.logging.LogManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class Utf8ResourceBundle {
	
	private static final Log log = LogFactory.getLog(Utf8ResourceBundle.class);
		
	public static final ResourceBundle getBundle(String baseName) {
	  ResourceBundle bundle = ResourceBundle.getBundle(baseName);
	  return createUtf8PropertyResourceBundle(bundle);
	}
	
	public static final ResourceBundle getBundle(String baseName, Locale locale) {
	  ResourceBundle bundle = ResourceBundle.getBundle(baseName, locale);
	  return createUtf8PropertyResourceBundle(bundle);
	}
	
	public static ResourceBundle getBundle(String baseName, Locale locale, ClassLoader loader) {
	  ResourceBundle bundle = ResourceBundle.getBundle(baseName, locale);
	  return createUtf8PropertyResourceBundle(bundle);
	}
	
	private static ResourceBundle createUtf8PropertyResourceBundle(ResourceBundle bundle) {
	  if (!(bundle instanceof PropertyResourceBundle)) return bundle;
	
	  return new Utf8PropertyResourceBundle((PropertyResourceBundle)bundle);
	}
	
	private static class Utf8PropertyResourceBundle extends ResourceBundle {
	  PropertyResourceBundle bundle;
	
	  private Utf8PropertyResourceBundle(PropertyResourceBundle bundle) {
	    this.bundle = bundle;
	  }
	  /* (non-Javadoc)
	   * @see java.util.ResourceBundle#getKeys()
	   */
	  public Enumeration getKeys() {
	    return bundle.getKeys();
	  }
	  /* (non-Javadoc)
	   * @see java.util.ResourceBundle#handleGetObject(java.lang.String)
	   */
	  protected Object handleGetObject(String key) {
		  String value = "";
		  try {
			  value = (String)bundle.getString(key);
			  if (value == null) return null;
		  } catch (MissingResourceException e) {
			log.error("Couldn't find resource " + bundle.toString() + ": " + e);
		  }
	
		  try {	  
			  return new String (value.getBytes("ISO-8859-1"),"UTF-8") ;
		  } catch (UnsupportedEncodingException e) {
		  	log.error("Couldn't encode " + value + ": " + e);
		  	return null;
		  }
	  }

}
}