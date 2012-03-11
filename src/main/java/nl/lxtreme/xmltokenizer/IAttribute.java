/*
 * LibXmlTokenizer
 * 
 * (C) Copyright 2012 - J.W. Janssen <j.w.janssen@lxtreme.nl>
 */
package nl.lxtreme.xmltokenizer;


/**
 * Each attribute specification has a name and a value.
 */
public interface IAttribute
{
  // METHOD

  /**
   * Returns the name-part of this attribute.
   * 
   * @return the name, never <code>null</code>.
   */
  public abstract String getName();

  /**
   * Returns the value-part of this attribute.
   * 
   * @return the value, never <code>null</code>.
   */
  public abstract String getValue();
}
