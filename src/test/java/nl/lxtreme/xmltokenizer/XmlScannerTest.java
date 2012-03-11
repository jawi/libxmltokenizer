/*
 * LibXmlTokenizer
 * 
 * (C) Copyright 2012 - J.W. Janssen <j.w.janssen@lxtreme.nl>
 */
package nl.lxtreme.xmltokenizer;


import static org.junit.Assert.*;

import java.io.*;

import org.junit.*;


/**
 * 
 */
public class XmlScannerTest
{
  // METHODS

  /**
   * Test method for XmlScanner#nextToken().
   */
  @Test
  public void testSimpleCase1() throws IOException
  {
    final String xml = "<p> foo </p>";

    final XmlScanner tokenizer = createTokenizer( xml );
    assertEquals( "<p>", tokenizer.nextToken() );
    assertEquals( " foo ", tokenizer.nextToken() );
    assertEquals( "</p>", tokenizer.nextToken() );
    assertNull( tokenizer.nextToken() );
  }

  /**
   * Test method for XmlScanner#nextToken().
   */
  @Test
  public void testSimpleCase2() throws IOException
  {
    final String xml = "<p>foo</p>";

    final XmlScanner tokenizer = createTokenizer( xml );
    assertEquals( "<p>", tokenizer.nextToken() );
    assertEquals( "foo", tokenizer.nextToken() );
    assertEquals( "</p>", tokenizer.nextToken() );
    assertNull( tokenizer.nextToken() );
  }

  /**
   * Test method for XmlScanner#nextToken().
   */
  @Test
  public void testSimpleCase3() throws IOException
  {
    final String xml = "<p></p>";

    final XmlScanner tokenizer = createTokenizer( xml );
    assertEquals( "<p>", tokenizer.nextToken() );
    assertEquals( "</p>", tokenizer.nextToken() );
    assertNull( tokenizer.nextToken() );
  }

  /**
   * Test method for XmlScanner#nextToken().
   */
  @Test
  public void testSimpleCase4() throws IOException
  {
    final String xml = "foo";

    final XmlScanner tokenizer = createTokenizer( xml );
    assertEquals( "foo", tokenizer.nextToken() );
    assertNull( tokenizer.nextToken() );
  }

  /**
   * Test method for XmlScanner#nextToken().
   */
  @Test
  public void testSimpleCase5() throws IOException
  {
    final String xml = "foo <p> bar <b> qux </b> <em> koo </em>";

    final XmlScanner tokenizer = createTokenizer( xml );
    assertEquals( "foo ", tokenizer.nextToken() );
    assertEquals( "<p>", tokenizer.nextToken() );
    assertEquals( " bar ", tokenizer.nextToken() );
    assertEquals( "<b>", tokenizer.nextToken() );
    assertEquals( " qux ", tokenizer.nextToken() );
    assertEquals( "</b>", tokenizer.nextToken() );
    assertEquals( " ", tokenizer.nextToken() );
    assertEquals( "<em>", tokenizer.nextToken() );
    assertEquals( " koo ", tokenizer.nextToken() );
    assertEquals( "</em>", tokenizer.nextToken() );
    assertNull( tokenizer.nextToken() );
  }

  /**
   * Test method for XmlScanner#nextToken().
   */
  @Test
  public void testSimpleCase6() throws IOException
  {
    final String xml = "<p class=\"test\"></p>";

    final XmlScanner tokenizer = createTokenizer( xml );
    assertEquals( "<p class=\"test\">", tokenizer.nextToken() );
    assertEquals( "</p>", tokenizer.nextToken() );
    assertNull( tokenizer.nextToken() );
  }

  /**
   * Test method for XmlScanner#nextToken().
   */
  @Test
  public void testSimpleCase7() throws IOException
  {
    final String xml = "<p> foo </p> ";

    final XmlScanner tokenizer = createTokenizer( xml );
    assertEquals( "<p>", tokenizer.nextToken() );
    assertEquals( " foo ", tokenizer.nextToken() );
    assertEquals( "</p>", tokenizer.nextToken() );
    assertEquals( " ", tokenizer.nextToken() );
    assertNull( tokenizer.nextToken() );
  }

  /**
   * Test method for XmlScanner#nextToken().
   */
  @Test
  public void testTokenizeCData1() throws IOException
  {
    final String xml = "<![CDATA[Hello World]]>";

    final XmlScanner tokenizer = createTokenizer( xml );
    assertEquals( xml, tokenizer.nextToken() );
    assertNull( tokenizer.nextToken() );
  }

  /**
   * Test method for XmlScanner#nextToken().
   */
  @Test
  public void testTokenizeCData2() throws IOException
  {
    final String xml = "<![CDATA[Hello < World]]>";

    final XmlScanner tokenizer = createTokenizer( xml );

    assertEquals( xml, tokenizer.nextToken() );
    assertNull( tokenizer.nextToken() );
  }

  /**
   * Test method for XmlScanner#nextToken().
   */
  @Test
  public void testTokenizeComment() throws IOException
  {
    final String xml = "<!-- this is a comment -->";

    final XmlScanner tokenizer = createTokenizer( xml );
    assertEquals( xml, tokenizer.nextToken() );
    assertNull( tokenizer.nextToken() );
  }

  /**
   * Test method for XmlScanner#nextToken().
   */
  @Test
  public void testTokenizeDoctype() throws IOException
  {
    final String xml = "<!DOCTYPE html public \"-//w3c//dtd html 4.0 transitional//en\">";

    final XmlScanner tokenizer = createTokenizer( xml );
    assertEquals( xml, tokenizer.nextToken() );
    assertNull( tokenizer.nextToken() );
  }

  /**
   * Test method for XmlScanner#nextToken().
   */
  @Test
  public void testTokenizeXmlDeclaration() throws IOException
  {
    final String xml = "<?xml encoding=\"utf-8\"?>";

    final XmlScanner tokenizer = createTokenizer( xml );
    assertEquals( xml, tokenizer.nextToken() );
    assertNull( tokenizer.nextToken() );
  }

  /**
   * @param aString
   * @return
   */
  private XmlScanner createTokenizer( final String aString )
  {
    return new XmlScanner( new StringReader( aString ) );
  }
}
