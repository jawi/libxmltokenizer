/*
 * LibXmlTokenizer
 * 
 * (C) Copyright 2012 - J.W. Janssen <j.w.janssen@lxtreme.nl>
 */
package nl.lxtreme.xmltokenizer;


import static org.junit.Assert.*;

import java.io.*;

import nl.lxtreme.xmltokenizer.impl.*;

import org.junit.*;


/**
 * 
 */
public class XmlTokenizerTest
{
  // METHODS

  /**
   * Test method for XmlTokenizer#nextToken().
   * 
   * @throws IOException
   */
  @Test
  public void testNextToken() throws IOException
  {
    final String xml = "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">\r\n" + // 
        "  <head>\r\n" + //
        "    <title>A Math Example</title>\r\n" + // 
        "  </head>\r\n" + //
        "  <body>\r\n" + //
        "    <p>The following is MathML markup:</p>\r\n" + // 
        "    <math xmlns=\"http://www.w3.org/1998/Math/MathML\">\r\n" + // 
        "      <apply> <log/>\r\n" + //
        "        <logbase>\r\n" + //
        "          <cn> 3 </cn>\r\n" + // 
        "        </logbase>\r\n" + //
        "        <ci> x </ci>\r\n" + //
        "      </apply>\r\n" + //
        "    </math>\r\n" + //
        "  </body>\r\n" + //
        "</html>"; // 21 tags

    final XmlTokenizer parser = createParser( xml );

    IToken token;
    int count = 0;
    do
    {
      token = parser.nextToken();
      if ( ( token != null ) && !( token instanceof TextToken ) )
      {
        count++;
      }
    }
    while ( token != null );

    assertEquals( 21, count );
  }

  /**
   * Test method for XmlScanner#nextToken().
   */
  @Test
  public void testTokenizeComment() throws IOException
  {
    final String xml = "<!-- this is a comment -->";

    final XmlTokenizer parser = createParser( xml );

    final IToken token = parser.nextToken();
    assertNotNull( token );

    assertEquals( "this is a comment", ( ( CommentToken )token ).getComment() );

    assertNull( parser.nextToken() );
  }

  /**
   * Test method for XmlScanner#nextToken().
   */
  @Test
  public void testTokenizeDoctype() throws IOException
  {
    final String xml = "<!DOCTYPE html public \"-//w3c//dtd html 4.0 transitional//en\">";

    final XmlTokenizer parser = createParser( xml );

    final IToken token = parser.nextToken();
    assertNotNull( token );

    final DocTypeToken docTypeToken = ( DocTypeToken )token;

    assertEquals( "html", docTypeToken.getRootElementName() );
    assertEquals( "public \"-//w3c//dtd html 4.0 transitional//en\"", docTypeToken.getExternalID() );

    assertNull( parser.nextToken() );
  }

  /**
   * Test method for XmlScanner#nextToken().
   */
  @Test
  public void testTokenizeEmptyElement1() throws IOException
  {
    final String xml = "<input />";

    final XmlTokenizer parser = createParser( xml );

    final IToken token = parser.nextToken();
    assertNotNull( token );

    final ElementToken elementToken = ( ElementToken )token;

    assertEquals( "input", elementToken.getName() );
    assertTrue( elementToken.isEmptyElement() );
    assertFalse( elementToken.isEndTag() );

    assertNull( parser.nextToken() );
  }

  /**
   * Test method for XmlScanner#nextToken().
   */
  @Test
  public void testTokenizeEmptyElement2() throws IOException
  {
    final String xml = "<input/>";

    final XmlTokenizer parser = createParser( xml );

    final IToken token = parser.nextToken();
    assertNotNull( token );

    final ElementToken elementToken = ( ElementToken )token;

    assertEquals( "input", elementToken.getName() );
    assertTrue( elementToken.isEmptyElement() );
    assertFalse( elementToken.isEndTag() );

    assertNull( parser.nextToken() );
  }

  /**
   * Test method for XmlScanner#nextToken().
   */
  @Test
  public void testTokenizeEmptyElementWithAttribute() throws IOException
  {
    final String xml = "<input type=\"text\" />";

    final XmlTokenizer parser = createParser( xml );

    final IToken token = parser.nextToken();
    assertNotNull( token );

    final ElementToken elementToken = ( ElementToken )token;

    assertEquals( "input", elementToken.getName() );
    assertTrue( elementToken.isEmptyElement() );
    assertFalse( elementToken.isEndTag() );

    assertTrue( elementToken.hasAttribute( new Attribute( "type", "text" ) ) );

    assertNull( parser.nextToken() );
  }

  /**
   * Test method for XmlScanner#nextToken().
   */
  @Test
  public void testTokenizeEndElement() throws IOException
  {
    final String xml = "</p>";

    final XmlTokenizer parser = createParser( xml );

    final IToken token = parser.nextToken();
    assertNotNull( token );

    final ElementToken elementToken = ( ElementToken )token;

    assertEquals( "p", elementToken.getName() );
    assertFalse( elementToken.isEmptyElement() );
    assertTrue( elementToken.isEndTag() );

    assertNull( parser.nextToken() );
  }

  /**
   * Test method for XmlScanner#nextToken().
   */
  @Test
  public void testTokenizeNotWellformedComment1() throws IOException
  {
    final String xml = "<!-- B+, B, or B--->";

    final XmlTokenizer parser = createParser( xml );

    final IToken token = parser.nextToken();
    assertNotNull( token );

    assertEquals( "B+, B, or B-", ( ( CommentToken )token ).getComment() );

    assertNull( parser.nextToken() );
  }

  /**
   * Test method for XmlScanner#nextToken().
   */
  @Test
  public void testTokenizeWellformedComment() throws IOException
  {
    final String xml = "<!-- B+, B, or B-->";

    final XmlTokenizer parser = createParser( xml );

    final IToken token = parser.nextToken();
    assertNotNull( token );

    assertEquals( "B+, B, or B", ( ( CommentToken )token ).getComment() );

    assertNull( parser.nextToken() );
  }

  /**
   * Test method for XmlScanner#nextToken().
   */
  @Test
  public void testTokenizeXHTMLDeclaration() throws IOException
  {
    final String xml = "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">";

    final XmlTokenizer parser = createParser( xml );

    final IToken token = parser.nextToken();
    assertNotNull( token );

    final ElementToken elemToken = ( ElementToken )token;

    assertEquals( "html", elemToken.getName() );
    assertTrue( elemToken.hasAttribute( new Attribute( "xmlns", "http://www.w3.org/1999/xhtml" ) ) );
    assertTrue( elemToken.hasAttribute( new Attribute( "xml:lang", "en" ) ) );
    assertTrue( elemToken.hasAttribute( new Attribute( "lang", "en" ) ) );

    assertNull( parser.nextToken() );
  }

  /**
   * Test method for XmlScanner#nextToken().
   */
  @Test
  public void testTokenizeXmlDeclaration() throws IOException
  {
    final String xml = "<?xml encoding=\"utf-8\"?>";

    final XmlTokenizer parser = createParser( xml );

    final IToken token = parser.nextToken();
    assertNotNull( token );

    final ProcessingInstructionToken piToken = ( ProcessingInstructionToken )token;

    assertEquals( "xml", piToken.getName() );
    assertTrue( piToken.hasAttribute( new Attribute( "encoding", "utf-8" ) ) );

    assertNull( parser.nextToken() );
  }

  /**
   * @param aString
   * @return
   */
  private XmlTokenizer createParser( final String aString )
  {
    final XmlScanner tokenizer = new XmlScanner( new StringReader( aString ) );
    return new XmlTokenizer( tokenizer );
  }
}
