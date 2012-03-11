/*
 * LibXmlTokenizer
 * 
 * (C) Copyright 2012 - J.W. Janssen <j.w.janssen@lxtreme.nl>
 */
package nl.lxtreme.xmltokenizer.impl;


/**
 * Each XML document contains one or more elements, the boundaries of which are
 * either delimited by start-tags and end-tags, or, for empty elements, by an
 * empty-element tag. Each element has a type, identified by name, sometimes
 * called its "generic identifier" (GI), and may have a set of attribute
 * specifications.
 */
public class ElementToken extends BaseToken
{
  // VARIABLES

  private boolean endTag       = false;
  private boolean emptyElement = false;

  // CONSTRUCTORS

  /**
   * @param aName
   */
  public ElementToken( final String aRawTokenText )
  {
    super( TokenType.TAG );

    String tokenText = aRawTokenText.trim();

    final String[] parts = tokenText.split( "\\s+", 2 );
    if ( parts.length > 0 )
    {
      String name = parts[0].trim();
      if ( name.startsWith( "/" ) )
      {
        this.endTag = true;
        name = name.substring( 1 );
      }
      else if ( name.endsWith( "/" ) )
      {
        this.emptyElement = true;
        name = name.substring( 0, name.length() - 1 ).trim();
      }
      else if ( tokenText.endsWith( "/" ) )
      {
        this.emptyElement = true;
        tokenText = tokenText.substring( 0, tokenText.length() - 1 ).trim();
      }
      setName( name );
    }

    tokenText = tokenText.replaceFirst( "\\Q" + getName() + "\\E", "" );
    if ( !tokenText.isEmpty() )
    {
      parseAttributes( tokenText );
    }
  }

  // METHODS

  /**
   * @return the emptyElement
   */
  public boolean isEmptyElement()
  {
    return this.emptyElement;
  }

  /**
   * @return the endTag
   */
  public boolean isEndTag()
  {
    return this.endTag;
  }
}
