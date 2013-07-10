package org.dpi.util.locale;

import java.util.Locale;

import org.dpi.util.BaseObject;


/**
 * Very simple class that just holds a text toghether with the information on
 * the language the text was written in. Can be used to be aware of
 * the language the text is written in and decide whether to show it or not depending on
 * Locale settings of who is going to visualize the text.
 * 
 * @author Martino Piccinato
 */
public class LocaleText extends BaseObject {

	private static final long serialVersionUID = 1L;
	
	/** The language specific text */
	protected String text;
	
	/** The language of the text */
	protected Locale locale;

	// No argument constructor needed by Hibernate persistence
	public LocaleText() {
		super();
	}
	
	public LocaleText(String text, Locale locale) {
		
		this.setText(text);
		this.setLocale(locale);
		
	}

	/**
	 * Get the text language
	 * 
	 * @return Returns the locale.
	 * 
	 * @hibernate.property
	 */
	public Locale getLocale() {
		return locale;
	}

	/**
	 * @param locale The locale to set.
	 * 
	 * 
	 */
	protected void setLocale(Locale locale) {
		this.locale = locale;
	}

	/**
	 * Get the text
	 * 
	 * @return Returns the text.
	 * 
	 * @hibernate.property
	 * 
	 */
	public String getText() {
		return text;
	}
	
	/**
	 * Return the text if the locale matches the parameter, null otherwise
	 * 
	 * @param locale The locale for which to get the text
	 * @return The text if the parameter matches the object locale, null otherwise
	 */
	public String getText(Locale locale) {
		
		if (locale.equals(this.getLocale())) {
			
			return this.getText();
			
		} else {
			
			return null;
			
		}
		
	}
	
	/**
	 * Returns true is the text is written in the same Locale as the
	 * argument passed.
	 * 
	 * @param locale The Locale to compare text Locale 
	 */
	public boolean isLocaleText(Locale locale) {
		
		if (this.getLocale().equals(locale)) {
			
			return true;
			
		} else {
			
			return false;
			
		}
		
	}

	/**
	 * 
	 * @param text The text to set.
	 */
	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return text;
	}
	
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof LocaleText)) {
            return false;
        }
        LocaleText i = (LocaleText) object;
        
        return new org.apache.commons.lang.builder.EqualsBuilder()
            .append(i.getText(), this.getText())
            .append(i.getLocale(), this.getLocale())
            .isEquals();
    }

    @Override
    public int hashCode() {
        return new org.apache.commons.lang.builder.HashCodeBuilder(-2022315247, 1437659757)
            .append(this.getLocale())
            .hashCode();
    }

}
