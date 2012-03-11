/*
 * LibXmlTokenizer
 * 
 * (C) Copyright 2012 - J.W. Janssen <j.w.janssen@lxtreme.nl>
 */
package nl.lxtreme.xmltokenizer.impl;


/**
 * The XML document type declaration contains or points to markup declarations
 * that provide a grammar for a class of documents. This grammar is known as a
 * document type definition, or DTD. The document type declaration can point to
 * an external subset (a special kind of external entity) containing markup
 * declarations, or can contain the markup declarations directly in an internal
 * subset, or can do both. The DTD for a document consists of both subsets taken
 * together.
 */
public class DocTypeToken extends BaseToken
{
  // VARIABLES

  private String rootElementName;
  private String externalID;

  // CONSTRUCTORS

  /**
   * 
   */
  public DocTypeToken( final String... aDocTypeParts )
  {
    super( TokenType.DOCTYPE );

    this.rootElementName = "html";
    this.externalID = "PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"";

    if ( aDocTypeParts.length > 0 )
    {
      this.rootElementName = aDocTypeParts[0];
    }
    if ( aDocTypeParts.length > 1 )
    {
      final String systemOrPublic = aDocTypeParts[1].toUpperCase();
      if ( systemOrPublic.startsWith( "PUBLIC" ) )
      {
        this.externalID = aDocTypeParts[1];
      }
      else if ( systemOrPublic.startsWith( "SYSTEM" ) )
      {
        this.externalID = aDocTypeParts[1];
      }
    }
  }

  // METHODS

  /**
   * @return the rootElementName
   */
  public String getRootElementName()
  {
    return this.rootElementName;
  }

  /**
   * @return the externalID
   */
  public String getExternalID()
  {
    return this.externalID;
  }
}
