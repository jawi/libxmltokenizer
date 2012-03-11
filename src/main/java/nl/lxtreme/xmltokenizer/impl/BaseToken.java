/*
 * LibXmlTokenizer
 * 
 * (C) Copyright 2012 - J.W. Janssen <j.w.janssen@lxtreme.nl>
 */
package nl.lxtreme.xmltokenizer.impl;


import java.util.*;

import nl.lxtreme.xmltokenizer.*;


/**
 * Base class for all tokens.
 */
public abstract class BaseToken implements IToken
{
  // VARIABLES

  private final List<IAttribute> attributes;
  private final List<IToken>     children;
  private String                 name;
  private IToken                 parent;
  private final TokenType        type;

  // CONSTRUCTOR

  /**
   * Creates a new BaseToken.
   * 
   * @param aName
   *          the token name.
   */
  protected BaseToken( final String aName, final TokenType aType )
  {
    this.name = aName;
    this.type = aType;

    this.attributes = new ArrayList<IAttribute>();
    this.children = new ArrayList<IToken>();
  }

  /**
   * Creates a new BaseToken.
   */
  protected BaseToken( final TokenType aType )
  {
    this( null, aType );
  }

  // METHODS

  /**
   * {@inheritDoc}
   */
  @Override
  public void addAttribute( final IAttribute... aAttributeList )
  {
    for ( IAttribute attribute : aAttributeList )
    {
      this.attributes.add( attribute );
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addChild( final IToken... aChildList )
  {
    for ( IToken child : aChildList )
    {
      child.setParent( this );
      this.children.add( child );
    }
  }

  /**
   * {@inheritDoc}
   */
  public List<IAttribute> getAttributes()
  {
    return Collections.unmodifiableList( this.attributes );
  }

  /**
   * {@inheritDoc}
   */
  public List<IToken> getChildren()
  {
    return Collections.unmodifiableList( this.children );
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
  public IToken getParent()
  {
    return this.parent;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final TokenType getType()
  {
    return this.type;
  }

  /**
   * {@inheritDoc}
   */
  public boolean hasAttribute( final IAttribute aAttribute )
  {
    return this.attributes.contains( aAttribute );
  }

  /**
   * @param aName
   *          the name to set
   */
  public void setName( final String aName )
  {
    this.name = aName;
  }

  /**
   * {@inheritDoc}
   */
  public void setParent( final IToken aParent )
  {
    this.parent = aParent;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString()
  {
    return getClass().getSimpleName() + ": " + getName();
  }

  /**
   * Parses a given string of attributes and adds them as actual attributes of
   * this token.
   * 
   * @param aString
   */
  protected void parseAttributes( final String aString )
  {
    if ( aString == null )
    {
      return;
    }

    this.attributes.addAll( Attribute.parse( aString ) );
  }
}
