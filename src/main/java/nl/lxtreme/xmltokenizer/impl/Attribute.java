/*
 * LibXmlTokenizer
 * 
 * (C) Copyright 2012 - J.W. Janssen <j.w.janssen@lxtreme.nl>
 */
package nl.lxtreme.xmltokenizer.impl;


import java.io.*;
import java.util.*;

import nl.lxtreme.xmltokenizer.*;


/**
 * Each attribute specification has a name and a value.
 */
public class Attribute implements IAttribute
{
  // VARIABLES

  private final String name;
  private final String value;

  // CONSTRUCTORS

  /**
   * Creates a new Attribute.
   * 
   * @param aName
   *          the name, cannot be <code>null</code>.
   * @throws IllegalArgumentException
   *           in case either name or value argument are <code>null</code>.
   */
  public Attribute( final String aName ) throws IllegalArgumentException
  {
    this( aName, aName );
  }

  /**
   * Creates a new Attribute.
   * 
   * @param aName
   *          the name, cannot be <code>null</code>;
   * @param aValue
   *          the value, cannot be <code>null</code>.
   * @throws IllegalArgumentException
   *           in case either name or value argument are <code>null</code>.
   */
  public Attribute( final String aName, final String aValue ) throws IllegalArgumentException
  {
    this.name = aName;

    if ( aValue == null )
    {
      throw new IllegalArgumentException( "Value cannot be null!" );
    }
    this.value = aValue;
  }

  // METHODS

  /**
   * Parses a given attribute string, assuming it is something like <code>foo = 'bar'</code>
   * .
   * 
   * @param aAttributeString
   * @return
   */
  public static Collection<Attribute> parse( final String aAttributeString ) throws IllegalArgumentException
  {
    if ( aAttributeString == null )
    {
      throw new IllegalArgumentException( "Attribute string cannot be null!" );
    }

    final Stack<Attribute> result = new Stack<Attribute>();
    final KeyValuePairParser tokenizer = new KeyValuePairParser( aAttributeString );

    String[] attr = null;
    do
    {
      try
      {
        attr = tokenizer.nextKeyValuePair();
        if ( attr != null )
        {
          // Did we find a "continuation"?
          if ( ( attr[0] == null ) && ( !result.isEmpty() ) )
          {
            final IAttribute lastAttr = result.pop();
            result.push( new Attribute( lastAttr.getName(), lastAttr.getValue() + " " + attr[1] ) );
          }
          else
          {
            result.push( new Attribute( attr[0], attr[1] ) );
          }
        }
      }
      catch ( IOException exception )
      {
        exception.printStackTrace();
      }
    }
    while ( attr != null );

    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals( final Object aObject )
  {
    if ( this == aObject )
    {
      return true;
    }
    if ( ( aObject == null ) || ( getClass() != aObject.getClass() ) )
    {
      return false;
    }

    final Attribute other = ( Attribute )aObject;
    if ( this.name == null )
    {
      if ( other.name != null )
      {
        return false;
      }
    }
    else if ( !this.name.equals( other.name ) )
    {
      return false;
    }
    if ( this.value == null )
    {
      if ( other.value != null )
      {
        return false;
      }
    }
    else if ( !this.value.equals( other.value ) )
    {
      return false;
    }
    return true;
  }

  /**
   * {@inheritDoc}
   */
  public String getName()
  {
    return this.name;
  }

  /**
   * {@inheritDoc}
   */
  public String getValue()
  {
    return this.value;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ( ( this.name == null ) ? 0 : this.name.hashCode() );
    result = prime * result + ( ( this.value == null ) ? 0 : this.value.hashCode() );
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString()
  {
    return this.name + "=\"" + this.value + "\"";
  }
}
