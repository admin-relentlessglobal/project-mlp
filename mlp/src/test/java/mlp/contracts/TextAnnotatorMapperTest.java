package mlp.contracts;

import com.google.common.base.Throwables;
import mlp.PatternMatchingRelationFinder;
import mlp.cli.FlinkMlpCommandConfig;
import mlp.flink.ListCollector;
import mlp.pojos.*;
import mlp.text.PosTag;
import mlp.text.WikiTextUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static org.junit.Assert.*;

public class TextAnnotatorMapperTest {

  private static final Random RND = new Random();

  public static final TextAnnotatorMapper TEST_INSTANCE = createTestInstance();


  @Test
  public void testTestResources() throws Exception{
    final String rawMath = ":&lt;math xmlns=&quot;http://www.w3.org/1998/Math/MathML&quot; id=&quot;p1.1.m1&quot; class=&quot;ltx_Math&quot; alttext=&quot;i\\hbar\\frac{\\partial}{\\partial t}\\Psi(\\mathbb{r},\\,t)=-\\frac{\\hbar^{2}}{2m}%&amp;#10;\\nabla^{2}\\Psi(\\mathbb{r},\\,t)+V(\\mathbb{r})\\Psi(\\mathbb{r},\\,t).&quot; xml:id=&quot;p1.1.m1.1&quot; display=&quot;inline&quot; xref=&quot;p1.1.m1.1.cmml&quot;&gt;\n" +
      "  &lt;semantics xml:id=&quot;p1.1.m1.1a&quot; xref=&quot;p1.1.m1.1.cmml&quot;&gt;\n" +
      "    &lt;mrow xml:id=&quot;p1.1.m1.1.36&quot; xref=&quot;p1.1.m1.1.36.cmml&quot;&gt;\n" +
      "      &lt;mrow xml:id=&quot;p1.1.m1.1.36a&quot; xref=&quot;p1.1.m1.1.36.cmml&quot;&gt;\n" +
      "        &lt;mrow xml:id=&quot;p1.1.m1.1.36.1&quot; xref=&quot;p1.1.m1.1.36.1.cmml&quot;&gt;\n" +
      "          &lt;mi xml:id=&quot;p1.1.m1.1.1&quot; xref=&quot;p1.1.m1.1.1.cmml&quot;&gt;i&lt;/mi&gt;\n" +
      "          &lt;mo xml:id=&quot;p1.1.m1.1.36.1.1&quot; xref=&quot;p1.1.m1.1.36.1.1.cmml&quot;&gt;\u2062&lt;/mo&gt;\n" +
      "          &lt;mi mathvariant=&quot;normal&quot; xml:id=&quot;p1.1.m1.1.2&quot; xref=&quot;p1.1.m1.1.2.cmml&quot;&gt;ℏ&lt;/mi&gt;\n" +
      "          &lt;mo xml:id=&quot;p1.1.m1.1.36.1.1a&quot; xref=&quot;p1.1.m1.1.36.1.1.cmml&quot;&gt;\u2062&lt;/mo&gt;\n" +
      "          &lt;mfrac xml:id=&quot;p1.1.m1.1.3&quot; xref=&quot;p1.1.m1.1.3.cmml&quot;&gt;\n" +
      "            &lt;mo xml:id=&quot;p1.1.m1.1.3.2&quot; xref=&quot;p1.1.m1.1.3.2.cmml&quot;&gt;∂&lt;/mo&gt;\n" +
      "            &lt;mrow xml:id=&quot;p1.1.m1.1.3.3&quot; xref=&quot;p1.1.m1.1.3.3.cmml&quot;&gt;\n" +
      "              &lt;mo xml:id=&quot;p1.1.m1.1.3.3.1&quot; xref=&quot;p1.1.m1.1.3.3.1.cmml&quot;&gt;∂&lt;/mo&gt;\n" +
      "              &lt;mo xml:id=&quot;p1.1.m1.1.3.3a&quot; xref=&quot;p1.1.m1.1.3.3.cmml&quot;&gt;\u2061&lt;/mo&gt;\n" +
      "              &lt;mi xml:id=&quot;p1.1.m1.1.3.3.2&quot; xref=&quot;p1.1.m1.1.3.3.2.cmml&quot;&gt;t&lt;/mi&gt;\n" +
      "            &lt;/mrow&gt;\n" +
      "          &lt;/mfrac&gt;\n" +
      "          &lt;mo xml:id=&quot;p1.1.m1.1.36.1.1b&quot; xref=&quot;p1.1.m1.1.36.1.1.cmml&quot;&gt;\u2062&lt;/mo&gt;\n" +
      "          &lt;mi mathvariant=&quot;normal&quot; xml:id=&quot;p1.1.m1.1.4&quot; xref=&quot;p1.1.m1.1.4.cmml&quot;&gt;Ψ&lt;/mi&gt;\n" +
      "          &lt;mo xml:id=&quot;p1.1.m1.1.36.1.1c&quot; xref=&quot;p1.1.m1.1.36.1.1.cmml&quot;&gt;\u2062&lt;/mo&gt;\n" +
      "          &lt;mrow xml:id=&quot;p1.1.m1.1.36.1.2&quot; xref=&quot;p1.1.m1.1.36.1.2.cmml&quot;&gt;\n" +
      "            &lt;mo xml:id=&quot;p1.1.m1.1.36.1.2a&quot; xref=&quot;p1.1.m1.1.36.1.2.cmml&quot;&gt;(&lt;/mo&gt;\n" +
      "            &lt;mrow xml:id=&quot;p1.1.m1.1.36.1.2b&quot; xref=&quot;p1.1.m1.1.36.1.2.cmml&quot;&gt;\n" +
      "              &lt;mi xml:id=&quot;p1.1.m1.1.6&quot; xref=&quot;p1.1.m1.1.6.cmml&quot;&gt;\uD835\uDD63&lt;/mi&gt;\n" +
      "              &lt;mo separator=&quot;true&quot; xml:id=&quot;p1.1.m1.1.36.1.2c&quot; xref=&quot;p1.1.m1.1.36.1.2.cmml&quot;&gt;, &lt;/mo&gt;\n" +
      "              &lt;mi xml:id=&quot;p1.1.m1.1.9&quot; xref=&quot;p1.1.m1.1.9.cmml&quot;&gt;t&lt;/mi&gt;\n" +
      "            &lt;/mrow&gt;\n" +
      "            &lt;mo xml:id=&quot;p1.1.m1.1.36.1.2d&quot; xref=&quot;p1.1.m1.1.36.1.2.cmml&quot;&gt;)&lt;/mo&gt;\n" +
      "          &lt;/mrow&gt;\n" +
      "        &lt;/mrow&gt;\n" +
      "        &lt;mo xml:id=&quot;p1.1.m1.1.11&quot; xref=&quot;p1.1.m1.1.11.cmml&quot;&gt;=&lt;/mo&gt;\n" +
      "        &lt;mrow xml:id=&quot;p1.1.m1.1.36.2&quot; xref=&quot;p1.1.m1.1.36.2.cmml&quot;&gt;\n" +
      "          &lt;mrow xml:id=&quot;p1.1.m1.1.36.2.1&quot; xref=&quot;p1.1.m1.1.36.2.1.cmml&quot;&gt;\n" +
      "            &lt;mo xml:id=&quot;p1.1.m1.1.12&quot; xref=&quot;p1.1.m1.1.12.cmml&quot;&gt;-&lt;/mo&gt;\n" +
      "            &lt;mrow xml:id=&quot;p1.1.m1.1.36.2.1.1&quot; xref=&quot;p1.1.m1.1.36.2.1.1.cmml&quot;&gt;\n" +
      "              &lt;mfrac xml:id=&quot;p1.1.m1.1.13&quot; xref=&quot;p1.1.m1.1.13.cmml&quot;&gt;\n" +
      "                &lt;msup xml:id=&quot;p1.1.m1.1.13.2&quot; xref=&quot;p1.1.m1.1.13.2.cmml&quot;&gt;\n" +
      "                  &lt;mi mathvariant=&quot;normal&quot; xml:id=&quot;p1.1.m1.1.13.2.1&quot; xref=&quot;p1.1.m1.1.13.2.1.cmml&quot;&gt;ℏ&lt;/mi&gt;\n" +
      "                  &lt;mn xml:id=&quot;p1.1.m1.1.13.2.2.1&quot; xref=&quot;p1.1.m1.1.13.2.2.1.cmml&quot;&gt;2&lt;/mn&gt;\n" +
      "                &lt;/msup&gt;\n" +
      "                &lt;mrow xml:id=&quot;p1.1.m1.1.13.3&quot; xref=&quot;p1.1.m1.1.13.3.cmml&quot;&gt;\n" +
      "                  &lt;mn xml:id=&quot;p1.1.m1.1.13.3.1&quot; xref=&quot;p1.1.m1.1.13.3.1.cmml&quot;&gt;2&lt;/mn&gt;\n" +
      "                  &lt;mo xml:id=&quot;p1.1.m1.1.13.3.3&quot; xref=&quot;p1.1.m1.1.13.3.3.cmml&quot;&gt;\u2062&lt;/mo&gt;\n" +
      "                  &lt;mi xml:id=&quot;p1.1.m1.1.13.3.2&quot; xref=&quot;p1.1.m1.1.13.3.2.cmml&quot;&gt;m&lt;/mi&gt;\n" +
      "                &lt;/mrow&gt;\n" +
      "              &lt;/mfrac&gt;\n" +
      "              &lt;mo xml:id=&quot;p1.1.m1.1.36.2.1.1.1&quot; xref=&quot;p1.1.m1.1.36.2.1.1.1.cmml&quot;&gt;\u2062&lt;/mo&gt;\n" +
      "              &lt;mrow xml:id=&quot;p1.1.m1.1.36.2.1.1.2&quot; xref=&quot;p1.1.m1.1.36.2.1.1.2.cmml&quot;&gt;\n" +
      "                &lt;msup xml:id=&quot;p1.1.m1.1.36.2.1.1.2.1&quot; xref=&quot;p1.1.m1.1.36.2.1.1.2.1.cmml&quot;&gt;\n" +
      "                  &lt;mo xml:id=&quot;p1.1.m1.1.14&quot; xref=&quot;p1.1.m1.1.14.cmml&quot;&gt;∇&lt;/mo&gt;\n" +
      "                  &lt;mn xml:id=&quot;p1.1.m1.1.15.1&quot; xref=&quot;p1.1.m1.1.15.1.cmml&quot;&gt;2&lt;/mn&gt;\n" +
      "                &lt;/msup&gt;\n" +
      "                &lt;mo xml:id=&quot;p1.1.m1.1.36.2.1.1.2a&quot; xref=&quot;p1.1.m1.1.36.2.1.1.2.cmml&quot;&gt;\u2061&lt;/mo&gt;\n" +
      "                &lt;mi mathvariant=&quot;normal&quot; xml:id=&quot;p1.1.m1.1.16&quot; xref=&quot;p1.1.m1.1.16.cmml&quot;&gt;Ψ&lt;/mi&gt;\n" +
      "              &lt;/mrow&gt;\n" +
      "              &lt;mo xml:id=&quot;p1.1.m1.1.36.2.1.1.1a&quot; xref=&quot;p1.1.m1.1.36.2.1.1.1.cmml&quot;&gt;\u2062&lt;/mo&gt;\n" +
      "              &lt;mrow xml:id=&quot;p1.1.m1.1.36.2.1.1.3&quot; xref=&quot;p1.1.m1.1.36.2.1.1.3.cmml&quot;&gt;\n" +
      "                &lt;mo xml:id=&quot;p1.1.m1.1.36.2.1.1.3a&quot; xref=&quot;p1.1.m1.1.36.2.1.1.3.cmml&quot;&gt;(&lt;/mo&gt;\n" +
      "                &lt;mrow xml:id=&quot;p1.1.m1.1.36.2.1.1.3b&quot; xref=&quot;p1.1.m1.1.36.2.1.1.3.cmml&quot;&gt;\n" +
      "                  &lt;mi xml:id=&quot;p1.1.m1.1.18&quot; xref=&quot;p1.1.m1.1.18.cmml&quot;&gt;\uD835\uDD63&lt;/mi&gt;\n" +
      "                  &lt;mo separator=&quot;true&quot; xml:id=&quot;p1.1.m1.1.36.2.1.1.3c&quot; xref=&quot;p1.1.m1.1.36.2.1.1.3.cmml&quot;&gt;, &lt;/mo&gt;\n" +
      "                  &lt;mi xml:id=&quot;p1.1.m1.1.21&quot; xref=&quot;p1.1.m1.1.21.cmml&quot;&gt;t&lt;/mi&gt;\n" +
      "                &lt;/mrow&gt;\n" +
      "                &lt;mo xml:id=&quot;p1.1.m1.1.36.2.1.1.3d&quot; xref=&quot;p1.1.m1.1.36.2.1.1.3.cmml&quot;&gt;)&lt;/mo&gt;\n" +
      "              &lt;/mrow&gt;\n" +
      "            &lt;/mrow&gt;\n" +
      "          &lt;/mrow&gt;\n" +
      "          &lt;mo xml:id=&quot;p1.1.m1.1.23&quot; xref=&quot;p1.1.m1.1.23.cmml&quot;&gt;+&lt;/mo&gt;\n" +
      "          &lt;mrow xml:id=&quot;p1.1.m1.1.36.2.2&quot; xref=&quot;p1.1.m1.1.36.2.2.cmml&quot;&gt;\n" +
      "            &lt;mi xml:id=&quot;p1.1.m1.1.24&quot; xref=&quot;p1.1.m1.1.24.cmml&quot;&gt;V&lt;/mi&gt;\n" +
      "            &lt;mo xml:id=&quot;p1.1.m1.1.36.2.2.1&quot; xref=&quot;p1.1.m1.1.36.2.2.1.cmml&quot;&gt;\u2062&lt;/mo&gt;\n" +
      "            &lt;mrow xml:id=&quot;p1.1.m1.1.26&quot; xref=&quot;p1.1.m1.1.26.cmml&quot;&gt;\n" +
      "              &lt;mo xml:id=&quot;p1.1.m1.1.26a&quot; xref=&quot;p1.1.m1.1.26.cmml&quot;&gt;(&lt;/mo&gt;\n" +
      "              &lt;mi xml:id=&quot;p1.1.m1.1.26b&quot; xref=&quot;p1.1.m1.1.26.cmml&quot;&gt;\uD835\uDD63&lt;/mi&gt;\n" +
      "              &lt;mo xml:id=&quot;p1.1.m1.1.26c&quot; xref=&quot;p1.1.m1.1.26.cmml&quot;&gt;)&lt;/mo&gt;\n" +
      "            &lt;/mrow&gt;\n" +
      "            &lt;mo xml:id=&quot;p1.1.m1.1.36.2.2.1a&quot; xref=&quot;p1.1.m1.1.36.2.2.1.cmml&quot;&gt;\u2062&lt;/mo&gt;\n" +
      "            &lt;mi mathvariant=&quot;normal&quot; xml:id=&quot;p1.1.m1.1.28&quot; xref=&quot;p1.1.m1.1.28.cmml&quot;&gt;Ψ&lt;/mi&gt;\n" +
      "            &lt;mo xml:id=&quot;p1.1.m1.1.36.2.2.1b&quot; xref=&quot;p1.1.m1.1.36.2.2.1.cmml&quot;&gt;\u2062&lt;/mo&gt;\n" +
      "            &lt;mrow xml:id=&quot;p1.1.m1.1.36.2.2.2&quot; xref=&quot;p1.1.m1.1.36.2.2.2.cmml&quot;&gt;\n" +
      "              &lt;mo xml:id=&quot;p1.1.m1.1.36.2.2.2a&quot; xref=&quot;p1.1.m1.1.36.2.2.2.cmml&quot;&gt;(&lt;/mo&gt;\n" +
      "              &lt;mrow xml:id=&quot;p1.1.m1.1.36.2.2.2b&quot; xref=&quot;p1.1.m1.1.36.2.2.2.cmml&quot;&gt;\n" +
      "                &lt;mi xml:id=&quot;p1.1.m1.1.30&quot; xref=&quot;p1.1.m1.1.30.cmml&quot;&gt;\uD835\uDD63&lt;/mi&gt;\n" +
      "                &lt;mo separator=&quot;true&quot; xml:id=&quot;p1.1.m1.1.36.2.2.2c&quot; xref=&quot;p1.1.m1.1.36.2.2.2.cmml&quot;&gt;, &lt;/mo&gt;\n" +
      "                &lt;mi xml:id=&quot;p1.1.m1.1.33&quot; xref=&quot;p1.1.m1.1.33.cmml&quot;&gt;t&lt;/mi&gt;\n" +
      "              &lt;/mrow&gt;\n" +
      "              &lt;mo xml:id=&quot;p1.1.m1.1.36.2.2.2d&quot; xref=&quot;p1.1.m1.1.36.2.2.2.cmml&quot;&gt;)&lt;/mo&gt;\n" +
      "            &lt;/mrow&gt;\n" +
      "          &lt;/mrow&gt;\n" +
      "        &lt;/mrow&gt;\n" +
      "      &lt;/mrow&gt;\n" +
      "      &lt;mo xml:id=&quot;p1.1.m1.1.36b&quot; xref=&quot;p1.1.m1.1.36.cmml&quot;&gt;.&lt;/mo&gt;\n" +
      "    &lt;/mrow&gt;\n" +
      "    &lt;annotation-xml xml:id=&quot;p1.1.m1.1.cmml&quot; encoding=&quot;MathML-Content&quot; xref=&quot;p1.1.m1.1&quot;&gt;\n" +
      "      &lt;apply xml:id=&quot;p1.1.m1.1.36.cmml&quot; xref=&quot;p1.1.m1.1.36&quot;&gt;\n" +
      "        &lt;eq xml:id=&quot;p1.1.m1.1.11.cmml&quot; xref=&quot;p1.1.m1.1.11&quot;/&gt;\n" +
      "        &lt;apply xml:id=&quot;p1.1.m1.1.36.1.cmml&quot; xref=&quot;p1.1.m1.1.36.1&quot;&gt;\n" +
      "          &lt;times xml:id=&quot;p1.1.m1.1.36.1.1.cmml&quot; xref=&quot;p1.1.m1.1.36.1.1&quot;/&gt;\n" +
      "          &lt;ci xml:id=&quot;p1.1.m1.1.1.cmml&quot; xref=&quot;p1.1.m1.1.1&quot;&gt;i&lt;/ci&gt;\n" +
      "          &lt;csymbol cd=&quot;latexml&quot; xml:id=&quot;p1.1.m1.1.2.cmml&quot; xref=&quot;p1.1.m1.1.2&quot;&gt;Planck-constant-over-2-pi&lt;/csymbol&gt;\n" +
      "          &lt;apply xml:id=&quot;p1.1.m1.1.3.cmml&quot; xref=&quot;p1.1.m1.1.3&quot;&gt;\n" +
      "            &lt;divide xml:id=&quot;p1.1.m1.1.3.1.cmml&quot;/&gt;\n" +
      "            &lt;partialdiff xml:id=&quot;p1.1.m1.1.3.2.cmml&quot; xref=&quot;p1.1.m1.1.3.2&quot;/&gt;\n" +
      "            &lt;apply xml:id=&quot;p1.1.m1.1.3.3.cmml&quot; xref=&quot;p1.1.m1.1.3.3&quot;&gt;\n" +
      "              &lt;partialdiff xml:id=&quot;p1.1.m1.1.3.3.1.cmml&quot; xref=&quot;p1.1.m1.1.3.3.1&quot;/&gt;\n" +
      "              &lt;ci xml:id=&quot;p1.1.m1.1.3.3.2.cmml&quot; xref=&quot;p1.1.m1.1.3.3.2&quot;&gt;t&lt;/ci&gt;\n" +
      "            &lt;/apply&gt;\n" +
      "          &lt;/apply&gt;\n" +
      "          &lt;ci xml:id=&quot;p1.1.m1.1.4.cmml&quot; xref=&quot;p1.1.m1.1.4&quot;&gt;normal-Ψ&lt;/ci&gt;\n" +
      "          &lt;apply xml:id=&quot;p1.1.m1.1.36.1.2.cmml&quot; xref=&quot;p1.1.m1.1.36.1.2&quot;&gt;\n" +
      "            &lt;interval closure=&quot;open&quot; xml:id=&quot;p1.1.m1.1.36.1.2.1.cmml&quot;/&gt;\n" +
      "            &lt;ci xml:id=&quot;p1.1.m1.1.6.cmml&quot; xref=&quot;p1.1.m1.1.6&quot;&gt;\uD835\uDD63&lt;/ci&gt;\n" +
      "            &lt;ci xml:id=&quot;p1.1.m1.1.9.cmml&quot; xref=&quot;p1.1.m1.1.9&quot;&gt;t&lt;/ci&gt;\n" +
      "          &lt;/apply&gt;\n" +
      "        &lt;/apply&gt;\n" +
      "        &lt;apply xml:id=&quot;p1.1.m1.1.36.2.cmml&quot; xref=&quot;p1.1.m1.1.36.2&quot;&gt;\n" +
      "          &lt;plus xml:id=&quot;p1.1.m1.1.23.cmml&quot; xref=&quot;p1.1.m1.1.23&quot;/&gt;\n" +
      "          &lt;apply xml:id=&quot;p1.1.m1.1.36.2.1.cmml&quot; xref=&quot;p1.1.m1.1.36.2.1&quot;&gt;\n" +
      "            &lt;minus xml:id=&quot;p1.1.m1.1.12.cmml&quot; xref=&quot;p1.1.m1.1.12&quot;/&gt;\n" +
      "            &lt;apply xml:id=&quot;p1.1.m1.1.36.2.1.1.cmml&quot; xref=&quot;p1.1.m1.1.36.2.1.1&quot;&gt;\n" +
      "              &lt;times xml:id=&quot;p1.1.m1.1.36.2.1.1.1.cmml&quot; xref=&quot;p1.1.m1.1.36.2.1.1.1&quot;/&gt;\n" +
      "              &lt;apply xml:id=&quot;p1.1.m1.1.13.cmml&quot; xref=&quot;p1.1.m1.1.13&quot;&gt;\n" +
      "                &lt;divide xml:id=&quot;p1.1.m1.1.13.1.cmml&quot;/&gt;\n" +
      "                &lt;apply xml:id=&quot;p1.1.m1.1.13.2.cmml&quot; xref=&quot;p1.1.m1.1.13.2&quot;&gt;\n" +
      "                  &lt;csymbol cd=&quot;ambiguous&quot; xml:id=&quot;p1.1.m1.1.13.2.3.cmml&quot;&gt;superscript&lt;/csymbol&gt;\n" +
      "                  &lt;csymbol cd=&quot;latexml&quot; xml:id=&quot;p1.1.m1.1.13.2.1.cmml&quot; xref=&quot;p1.1.m1.1.13.2.1&quot;&gt;Planck-constant-over-2-pi&lt;/csymbol&gt;\n" +
      "                  &lt;cn type=&quot;integer&quot; xml:id=&quot;p1.1.m1.1.13.2.2.1.cmml&quot; xref=&quot;p1.1.m1.1.13.2.2.1&quot;&gt;2&lt;/cn&gt;\n" +
      "                &lt;/apply&gt;\n" +
      "                &lt;apply xml:id=&quot;p1.1.m1.1.13.3.cmml&quot; xref=&quot;p1.1.m1.1.13.3&quot;&gt;\n" +
      "                  &lt;times xml:id=&quot;p1.1.m1.1.13.3.3.cmml&quot; xref=&quot;p1.1.m1.1.13.3.3&quot;/&gt;\n" +
      "                  &lt;cn type=&quot;integer&quot; xml:id=&quot;p1.1.m1.1.13.3.1.cmml&quot; xref=&quot;p1.1.m1.1.13.3.1&quot;&gt;2&lt;/cn&gt;\n" +
      "                  &lt;ci xml:id=&quot;p1.1.m1.1.13.3.2.cmml&quot; xref=&quot;p1.1.m1.1.13.3.2&quot;&gt;m&lt;/ci&gt;\n" +
      "                &lt;/apply&gt;\n" +
      "              &lt;/apply&gt;\n" +
      "              &lt;apply xml:id=&quot;p1.1.m1.1.36.2.1.1.2.cmml&quot; xref=&quot;p1.1.m1.1.36.2.1.1.2&quot;&gt;\n" +
      "                &lt;apply xml:id=&quot;p1.1.m1.1.36.2.1.1.2.1.cmml&quot; xref=&quot;p1.1.m1.1.36.2.1.1.2.1&quot;&gt;\n" +
      "                  &lt;csymbol cd=&quot;ambiguous&quot; xml:id=&quot;p1.1.m1.1.36.2.1.1.2.1.1.cmml&quot;&gt;superscript&lt;/csymbol&gt;\n" +
      "                  &lt;ci xml:id=&quot;p1.1.m1.1.14.cmml&quot; xref=&quot;p1.1.m1.1.14&quot;&gt;normal-∇&lt;/ci&gt;\n" +
      "                  &lt;cn type=&quot;integer&quot; xml:id=&quot;p1.1.m1.1.15.1.cmml&quot; xref=&quot;p1.1.m1.1.15.1&quot;&gt;2&lt;/cn&gt;\n" +
      "                &lt;/apply&gt;\n" +
      "                &lt;ci xml:id=&quot;p1.1.m1.1.16.cmml&quot; xref=&quot;p1.1.m1.1.16&quot;&gt;normal-Ψ&lt;/ci&gt;\n" +
      "              &lt;/apply&gt;\n" +
      "              &lt;apply xml:id=&quot;p1.1.m1.1.36.2.1.1.3.cmml&quot; xref=&quot;p1.1.m1.1.36.2.1.1.3&quot;&gt;\n" +
      "                &lt;interval closure=&quot;open&quot; xml:id=&quot;p1.1.m1.1.36.2.1.1.3.1.cmml&quot;/&gt;\n" +
      "                &lt;ci xml:id=&quot;p1.1.m1.1.18.cmml&quot; xref=&quot;p1.1.m1.1.18&quot;&gt;\uD835\uDD63&lt;/ci&gt;\n" +
      "                &lt;ci xml:id=&quot;p1.1.m1.1.21.cmml&quot; xref=&quot;p1.1.m1.1.21&quot;&gt;t&lt;/ci&gt;\n" +
      "              &lt;/apply&gt;\n" +
      "            &lt;/apply&gt;\n" +
      "          &lt;/apply&gt;\n" +
      "          &lt;apply xml:id=&quot;p1.1.m1.1.36.2.2.cmml&quot; xref=&quot;p1.1.m1.1.36.2.2&quot;&gt;\n" +
      "            &lt;times xml:id=&quot;p1.1.m1.1.36.2.2.1.cmml&quot; xref=&quot;p1.1.m1.1.36.2.2.1&quot;/&gt;\n" +
      "            &lt;ci xml:id=&quot;p1.1.m1.1.24.cmml&quot; xref=&quot;p1.1.m1.1.24&quot;&gt;V&lt;/ci&gt;\n" +
      "            &lt;ci xml:id=&quot;p1.1.m1.1.26.cmml&quot; xref=&quot;p1.1.m1.1.26&quot;&gt;\uD835\uDD63&lt;/ci&gt;\n" +
      "            &lt;ci xml:id=&quot;p1.1.m1.1.28.cmml&quot; xref=&quot;p1.1.m1.1.28&quot;&gt;normal-Ψ&lt;/ci&gt;\n" +
      "            &lt;apply xml:id=&quot;p1.1.m1.1.36.2.2.2.cmml&quot; xref=&quot;p1.1.m1.1.36.2.2.2&quot;&gt;\n" +
      "              &lt;interval closure=&quot;open&quot; xml:id=&quot;p1.1.m1.1.36.2.2.2.1.cmml&quot;/&gt;\n" +
      "              &lt;ci xml:id=&quot;p1.1.m1.1.30.cmml&quot; xref=&quot;p1.1.m1.1.30&quot;&gt;\uD835\uDD63&lt;/ci&gt;\n" +
      "              &lt;ci xml:id=&quot;p1.1.m1.1.33.cmml&quot; xref=&quot;p1.1.m1.1.33&quot;&gt;t&lt;/ci&gt;\n" +
      "            &lt;/apply&gt;\n" +
      "          &lt;/apply&gt;\n" +
      "        &lt;/apply&gt;\n" +
      "      &lt;/apply&gt;\n" +
      "    &lt;/annotation-xml&gt;\n" +
      "    &lt;annotation xml:id=&quot;p1.1.m1.1b&quot; encoding=&quot;application/x-tex&quot; xref=&quot;p1.1.m1.1.cmml&quot;&gt;i\\hbar\\frac{\\partial}{\\partial t}\\Psi(\\mathbb{r},\\,t)=-\\frac{\\hbar^{2}}{2m}%\n" +
      "\\nabla^{2}\\Psi(\\mathbb{r},\\,t)+V(\\mathbb{r})\\Psi(\\mathbb{r},\\,t).&lt;/annotation&gt;\n" +
      "  &lt;/semantics&gt;\n" +
      "&lt;/math&gt;";
    assertTrue(getTestResource("augmentendwikitext.xml").contains(rawMath));
  }
  @Test
  public void test() throws Exception {

    final String mathMLExtract = "<math xmlns=\"http://www.w3.org/1998/Math/MathML\" id=\"p1.1.m1\" class=\"ltx_Math\" alttext=\"i\\hbar\\frac{\\partial}{\\partial t}\\Psi(\\mathbb{r},\\,t)=-\\frac{\\hbar^{2}}{2m}%&#10;\\nabla^{2}\\Psi(\\mathbb{r},\\,t)+V(\\mathbb{r})\\Psi(\\mathbb{r},\\,t).\" xml:id=\"p1.1.m1.1\" display=\"inline\" xref=\"p1.1.m1.1.cmml\">\n" +
      "  <semantics xml:id=\"p1.1.m1.1a\" xref=\"p1.1.m1.1.cmml\">\n" +
      "    <mrow xml:id=\"p1.1.m1.1.36\" xref=\"p1.1.m1.1.36.cmml\">\n" +
      "      <mrow xml:id=\"p1.1.m1.1.36a\" xref=\"p1.1.m1.1.36.cmml\">\n" +
      "        <mrow xml:id=\"p1.1.m1.1.36.1\" xref=\"p1.1.m1.1.36.1.cmml\">\n" +
      "          <mi xml:id=\"p1.1.m1.1.1\" xref=\"p1.1.m1.1.1.cmml\">i</mi>\n" +
      "          <mo xml:id=\"p1.1.m1.1.36.1.1\" xref=\"p1.1.m1.1.36.1.1.cmml\">\u2062</mo>\n" +
      "          <mi mathvariant=\"normal\" xml:id=\"p1.1.m1.1.2\" xref=\"p1.1.m1.1.2.cmml\">ℏ</mi>\n" +
      "          <mo xml:id=\"p1.1.m1.1.36.1.1a\" xref=\"p1.1.m1.1.36.1.1.cmml\">\u2062</mo>\n" +
      "          <mfrac xml:id=\"p1.1.m1.1.3\" xref=\"p1.1.m1.1.3.cmml\">\n" +
      "            <mo xml:id=\"p1.1.m1.1.3.2\" xref=\"p1.1.m1.1.3.2.cmml\">∂</mo>\n" +
      "            <mrow xml:id=\"p1.1.m1.1.3.3\" xref=\"p1.1.m1.1.3.3.cmml\">\n" +
      "              <mo xml:id=\"p1.1.m1.1.3.3.1\" xref=\"p1.1.m1.1.3.3.1.cmml\">∂</mo>\n" +
      "              <mo xml:id=\"p1.1.m1.1.3.3a\" xref=\"p1.1.m1.1.3.3.cmml\">\u2061</mo>\n" +
      "              <mi xml:id=\"p1.1.m1.1.3.3.2\" xref=\"p1.1.m1.1.3.3.2.cmml\">t</mi>\n" +
      "            </mrow>\n" +
      "          </mfrac>\n" +
      "          <mo xml:id=\"p1.1.m1.1.36.1.1b\" xref=\"p1.1.m1.1.36.1.1.cmml\">\u2062</mo>\n" +
      "          <mi mathvariant=\"normal\" xml:id=\"p1.1.m1.1.4\" xref=\"p1.1.m1.1.4.cmml\">Ψ</mi>\n" +
      "          <mo xml:id=\"p1.1.m1.1.36.1.1c\" xref=\"p1.1.m1.1.36.1.1.cmml\">\u2062</mo>\n" +
      "          <mrow xml:id=\"p1.1.m1.1.36.1.2\" xref=\"p1.1.m1.1.36.1.2.cmml\">\n" +
      "            <mo xml:id=\"p1.1.m1.1.36.1.2a\" xref=\"p1.1.m1.1.36.1.2.cmml\">(</mo>\n" +
      "            <mrow xml:id=\"p1.1.m1.1.36.1.2b\" xref=\"p1.1.m1.1.36.1.2.cmml\">\n" +
      "              <mi xml:id=\"p1.1.m1.1.6\" xref=\"p1.1.m1.1.6.cmml\">\uD835\uDD63</mi>\n" +
      "              <mo separator=\"true\" xml:id=\"p1.1.m1.1.36.1.2c\" xref=\"p1.1.m1.1.36.1.2.cmml\">, </mo>\n" +
      "              <mi xml:id=\"p1.1.m1.1.9\" xref=\"p1.1.m1.1.9.cmml\">t</mi>\n" +
      "            </mrow>\n" +
      "            <mo xml:id=\"p1.1.m1.1.36.1.2d\" xref=\"p1.1.m1.1.36.1.2.cmml\">)</mo>\n" +
      "          </mrow>\n" +
      "        </mrow>\n" +
      "        <mo xml:id=\"p1.1.m1.1.11\" xref=\"p1.1.m1.1.11.cmml\">=</mo>\n" +
      "        <mrow xml:id=\"p1.1.m1.1.36.2\" xref=\"p1.1.m1.1.36.2.cmml\">\n" +
      "          <mrow xml:id=\"p1.1.m1.1.36.2.1\" xref=\"p1.1.m1.1.36.2.1.cmml\">\n" +
      "            <mo xml:id=\"p1.1.m1.1.12\" xref=\"p1.1.m1.1.12.cmml\">-</mo>\n" +
      "            <mrow xml:id=\"p1.1.m1.1.36.2.1.1\" xref=\"p1.1.m1.1.36.2.1.1.cmml\">\n" +
      "              <mfrac xml:id=\"p1.1.m1.1.13\" xref=\"p1.1.m1.1.13.cmml\">\n" +
      "                <msup xml:id=\"p1.1.m1.1.13.2\" xref=\"p1.1.m1.1.13.2.cmml\">\n" +
      "                  <mi mathvariant=\"normal\" xml:id=\"p1.1.m1.1.13.2.1\" xref=\"p1.1.m1.1.13.2.1.cmml\">ℏ</mi>\n" +
      "                  <mn xml:id=\"p1.1.m1.1.13.2.2.1\" xref=\"p1.1.m1.1.13.2.2.1.cmml\">2</mn>\n" +
      "                </msup>\n" +
      "                <mrow xml:id=\"p1.1.m1.1.13.3\" xref=\"p1.1.m1.1.13.3.cmml\">\n" +
      "                  <mn xml:id=\"p1.1.m1.1.13.3.1\" xref=\"p1.1.m1.1.13.3.1.cmml\">2</mn>\n" +
      "                  <mo xml:id=\"p1.1.m1.1.13.3.3\" xref=\"p1.1.m1.1.13.3.3.cmml\">\u2062</mo>\n" +
      "                  <mi xml:id=\"p1.1.m1.1.13.3.2\" xref=\"p1.1.m1.1.13.3.2.cmml\">m</mi>\n" +
      "                </mrow>\n" +
      "              </mfrac>\n" +
      "              <mo xml:id=\"p1.1.m1.1.36.2.1.1.1\" xref=\"p1.1.m1.1.36.2.1.1.1.cmml\">\u2062</mo>\n" +
      "              <mrow xml:id=\"p1.1.m1.1.36.2.1.1.2\" xref=\"p1.1.m1.1.36.2.1.1.2.cmml\">\n" +
      "                <msup xml:id=\"p1.1.m1.1.36.2.1.1.2.1\" xref=\"p1.1.m1.1.36.2.1.1.2.1.cmml\">\n" +
      "                  <mo xml:id=\"p1.1.m1.1.14\" xref=\"p1.1.m1.1.14.cmml\">∇</mo>\n" +
      "                  <mn xml:id=\"p1.1.m1.1.15.1\" xref=\"p1.1.m1.1.15.1.cmml\">2</mn>\n" +
      "                </msup>\n" +
      "                <mo xml:id=\"p1.1.m1.1.36.2.1.1.2a\" xref=\"p1.1.m1.1.36.2.1.1.2.cmml\">\u2061</mo>\n" +
      "                <mi mathvariant=\"normal\" xml:id=\"p1.1.m1.1.16\" xref=\"p1.1.m1.1.16.cmml\">Ψ</mi>\n" +
      "              </mrow>\n" +
      "              <mo xml:id=\"p1.1.m1.1.36.2.1.1.1a\" xref=\"p1.1.m1.1.36.2.1.1.1.cmml\">\u2062</mo>\n" +
      "              <mrow xml:id=\"p1.1.m1.1.36.2.1.1.3\" xref=\"p1.1.m1.1.36.2.1.1.3.cmml\">\n" +
      "                <mo xml:id=\"p1.1.m1.1.36.2.1.1.3a\" xref=\"p1.1.m1.1.36.2.1.1.3.cmml\">(</mo>\n" +
      "                <mrow xml:id=\"p1.1.m1.1.36.2.1.1.3b\" xref=\"p1.1.m1.1.36.2.1.1.3.cmml\">\n" +
      "                  <mi xml:id=\"p1.1.m1.1.18\" xref=\"p1.1.m1.1.18.cmml\">\uD835\uDD63</mi>\n" +
      "                  <mo separator=\"true\" xml:id=\"p1.1.m1.1.36.2.1.1.3c\" xref=\"p1.1.m1.1.36.2.1.1.3.cmml\">, </mo>\n" +
      "                  <mi xml:id=\"p1.1.m1.1.21\" xref=\"p1.1.m1.1.21.cmml\">t</mi>\n" +
      "                </mrow>\n" +
      "                <mo xml:id=\"p1.1.m1.1.36.2.1.1.3d\" xref=\"p1.1.m1.1.36.2.1.1.3.cmml\">)</mo>\n" +
      "              </mrow>\n" +
      "            </mrow>\n" +
      "          </mrow>\n" +
      "          <mo xml:id=\"p1.1.m1.1.23\" xref=\"p1.1.m1.1.23.cmml\">+</mo>\n" +
      "          <mrow xml:id=\"p1.1.m1.1.36.2.2\" xref=\"p1.1.m1.1.36.2.2.cmml\">\n" +
      "            <mi xml:id=\"p1.1.m1.1.24\" xref=\"p1.1.m1.1.24.cmml\">V</mi>\n" +
      "            <mo xml:id=\"p1.1.m1.1.36.2.2.1\" xref=\"p1.1.m1.1.36.2.2.1.cmml\">\u2062</mo>\n" +
      "            <mrow xml:id=\"p1.1.m1.1.26\" xref=\"p1.1.m1.1.26.cmml\">\n" +
      "              <mo xml:id=\"p1.1.m1.1.26a\" xref=\"p1.1.m1.1.26.cmml\">(</mo>\n" +
      "              <mi xml:id=\"p1.1.m1.1.26b\" xref=\"p1.1.m1.1.26.cmml\">\uD835\uDD63</mi>\n" +
      "              <mo xml:id=\"p1.1.m1.1.26c\" xref=\"p1.1.m1.1.26.cmml\">)</mo>\n" +
      "            </mrow>\n" +
      "            <mo xml:id=\"p1.1.m1.1.36.2.2.1a\" xref=\"p1.1.m1.1.36.2.2.1.cmml\">\u2062</mo>\n" +
      "            <mi mathvariant=\"normal\" xml:id=\"p1.1.m1.1.28\" xref=\"p1.1.m1.1.28.cmml\">Ψ</mi>\n" +
      "            <mo xml:id=\"p1.1.m1.1.36.2.2.1b\" xref=\"p1.1.m1.1.36.2.2.1.cmml\">\u2062</mo>\n" +
      "            <mrow xml:id=\"p1.1.m1.1.36.2.2.2\" xref=\"p1.1.m1.1.36.2.2.2.cmml\">\n" +
      "              <mo xml:id=\"p1.1.m1.1.36.2.2.2a\" xref=\"p1.1.m1.1.36.2.2.2.cmml\">(</mo>\n" +
      "              <mrow xml:id=\"p1.1.m1.1.36.2.2.2b\" xref=\"p1.1.m1.1.36.2.2.2.cmml\">\n" +
      "                <mi xml:id=\"p1.1.m1.1.30\" xref=\"p1.1.m1.1.30.cmml\">\uD835\uDD63</mi>\n" +
      "                <mo separator=\"true\" xml:id=\"p1.1.m1.1.36.2.2.2c\" xref=\"p1.1.m1.1.36.2.2.2.cmml\">, </mo>\n" +
      "                <mi xml:id=\"p1.1.m1.1.33\" xref=\"p1.1.m1.1.33.cmml\">t</mi>\n" +
      "              </mrow>\n" +
      "              <mo xml:id=\"p1.1.m1.1.36.2.2.2d\" xref=\"p1.1.m1.1.36.2.2.2.cmml\">)</mo>\n" +
      "            </mrow>\n" +
      "          </mrow>\n" +
      "        </mrow>\n" +
      "      </mrow>\n" +
      "      <mo xml:id=\"p1.1.m1.1.36b\" xref=\"p1.1.m1.1.36.cmml\">.</mo>\n" +
      "    </mrow>\n" +
      "    <annotation-xml xml:id=\"p1.1.m1.1.cmml\" encoding=\"MathML-Content\" xref=\"p1.1.m1.1\">\n" +
      "      <apply xml:id=\"p1.1.m1.1.36.cmml\" xref=\"p1.1.m1.1.36\">\n" +
      "        <eq xml:id=\"p1.1.m1.1.11.cmml\" xref=\"p1.1.m1.1.11\"/>\n" +
      "        <apply xml:id=\"p1.1.m1.1.36.1.cmml\" xref=\"p1.1.m1.1.36.1\">\n" +
      "          <times xml:id=\"p1.1.m1.1.36.1.1.cmml\" xref=\"p1.1.m1.1.36.1.1\"/>\n" +
      "          <ci xml:id=\"p1.1.m1.1.1.cmml\" xref=\"p1.1.m1.1.1\">i</ci>\n" +
      "          <csymbol cd=\"latexml\" xml:id=\"p1.1.m1.1.2.cmml\" xref=\"p1.1.m1.1.2\">Planck-constant-over-2-pi</csymbol>\n" +
      "          <apply xml:id=\"p1.1.m1.1.3.cmml\" xref=\"p1.1.m1.1.3\">\n" +
      "            <divide xml:id=\"p1.1.m1.1.3.1.cmml\"/>\n" +
      "            <partialdiff xml:id=\"p1.1.m1.1.3.2.cmml\" xref=\"p1.1.m1.1.3.2\"/>\n" +
      "            <apply xml:id=\"p1.1.m1.1.3.3.cmml\" xref=\"p1.1.m1.1.3.3\">\n" +
      "              <partialdiff xml:id=\"p1.1.m1.1.3.3.1.cmml\" xref=\"p1.1.m1.1.3.3.1\"/>\n" +
      "              <ci xml:id=\"p1.1.m1.1.3.3.2.cmml\" xref=\"p1.1.m1.1.3.3.2\">t</ci>\n" +
      "            </apply>\n" +
      "          </apply>\n" +
      "          <ci xml:id=\"p1.1.m1.1.4.cmml\" xref=\"p1.1.m1.1.4\">normal-Ψ</ci>\n" +
      "          <apply xml:id=\"p1.1.m1.1.36.1.2.cmml\" xref=\"p1.1.m1.1.36.1.2\">\n" +
      "            <interval closure=\"open\" xml:id=\"p1.1.m1.1.36.1.2.1.cmml\"/>\n" +
      "            <ci xml:id=\"p1.1.m1.1.6.cmml\" xref=\"p1.1.m1.1.6\">\uD835\uDD63</ci>\n" +
      "            <ci xml:id=\"p1.1.m1.1.9.cmml\" xref=\"p1.1.m1.1.9\">t</ci>\n" +
      "          </apply>\n" +
      "        </apply>\n" +
      "        <apply xml:id=\"p1.1.m1.1.36.2.cmml\" xref=\"p1.1.m1.1.36.2\">\n" +
      "          <plus xml:id=\"p1.1.m1.1.23.cmml\" xref=\"p1.1.m1.1.23\"/>\n" +
      "          <apply xml:id=\"p1.1.m1.1.36.2.1.cmml\" xref=\"p1.1.m1.1.36.2.1\">\n" +
      "            <minus xml:id=\"p1.1.m1.1.12.cmml\" xref=\"p1.1.m1.1.12\"/>\n" +
      "            <apply xml:id=\"p1.1.m1.1.36.2.1.1.cmml\" xref=\"p1.1.m1.1.36.2.1.1\">\n" +
      "              <times xml:id=\"p1.1.m1.1.36.2.1.1.1.cmml\" xref=\"p1.1.m1.1.36.2.1.1.1\"/>\n" +
      "              <apply xml:id=\"p1.1.m1.1.13.cmml\" xref=\"p1.1.m1.1.13\">\n" +
      "                <divide xml:id=\"p1.1.m1.1.13.1.cmml\"/>\n" +
      "                <apply xml:id=\"p1.1.m1.1.13.2.cmml\" xref=\"p1.1.m1.1.13.2\">\n" +
      "                  <csymbol cd=\"ambiguous\" xml:id=\"p1.1.m1.1.13.2.3.cmml\">superscript</csymbol>\n" +
      "                  <csymbol cd=\"latexml\" xml:id=\"p1.1.m1.1.13.2.1.cmml\" xref=\"p1.1.m1.1.13.2.1\">Planck-constant-over-2-pi</csymbol>\n" +
      "                  <cn type=\"integer\" xml:id=\"p1.1.m1.1.13.2.2.1.cmml\" xref=\"p1.1.m1.1.13.2.2.1\">2</cn>\n" +
      "                </apply>\n" +
      "                <apply xml:id=\"p1.1.m1.1.13.3.cmml\" xref=\"p1.1.m1.1.13.3\">\n" +
      "                  <times xml:id=\"p1.1.m1.1.13.3.3.cmml\" xref=\"p1.1.m1.1.13.3.3\"/>\n" +
      "                  <cn type=\"integer\" xml:id=\"p1.1.m1.1.13.3.1.cmml\" xref=\"p1.1.m1.1.13.3.1\">2</cn>\n" +
      "                  <ci xml:id=\"p1.1.m1.1.13.3.2.cmml\" xref=\"p1.1.m1.1.13.3.2\">m</ci>\n" +
      "                </apply>\n" +
      "              </apply>\n" +
      "              <apply xml:id=\"p1.1.m1.1.36.2.1.1.2.cmml\" xref=\"p1.1.m1.1.36.2.1.1.2\">\n" +
      "                <apply xml:id=\"p1.1.m1.1.36.2.1.1.2.1.cmml\" xref=\"p1.1.m1.1.36.2.1.1.2.1\">\n" +
      "                  <csymbol cd=\"ambiguous\" xml:id=\"p1.1.m1.1.36.2.1.1.2.1.1.cmml\">superscript</csymbol>\n" +
      "                  <ci xml:id=\"p1.1.m1.1.14.cmml\" xref=\"p1.1.m1.1.14\">normal-∇</ci>\n" +
      "                  <cn type=\"integer\" xml:id=\"p1.1.m1.1.15.1.cmml\" xref=\"p1.1.m1.1.15.1\">2</cn>\n" +
      "                </apply>\n" +
      "                <ci xml:id=\"p1.1.m1.1.16.cmml\" xref=\"p1.1.m1.1.16\">normal-Ψ</ci>\n" +
      "              </apply>\n" +
      "              <apply xml:id=\"p1.1.m1.1.36.2.1.1.3.cmml\" xref=\"p1.1.m1.1.36.2.1.1.3\">\n" +
      "                <interval closure=\"open\" xml:id=\"p1.1.m1.1.36.2.1.1.3.1.cmml\"/>\n" +
      "                <ci xml:id=\"p1.1.m1.1.18.cmml\" xref=\"p1.1.m1.1.18\">\uD835\uDD63</ci>\n" +
      "                <ci xml:id=\"p1.1.m1.1.21.cmml\" xref=\"p1.1.m1.1.21\">t</ci>\n" +
      "              </apply>\n" +
      "            </apply>\n" +
      "          </apply>\n" +
      "          <apply xml:id=\"p1.1.m1.1.36.2.2.cmml\" xref=\"p1.1.m1.1.36.2.2\">\n" +
      "            <times xml:id=\"p1.1.m1.1.36.2.2.1.cmml\" xref=\"p1.1.m1.1.36.2.2.1\"/>\n" +
      "            <ci xml:id=\"p1.1.m1.1.24.cmml\" xref=\"p1.1.m1.1.24\">V</ci>\n" +
      "            <ci xml:id=\"p1.1.m1.1.26.cmml\" xref=\"p1.1.m1.1.26\">\uD835\uDD63</ci>\n" +
      "            <ci xml:id=\"p1.1.m1.1.28.cmml\" xref=\"p1.1.m1.1.28\">normal-Ψ</ci>\n" +
      "            <apply xml:id=\"p1.1.m1.1.36.2.2.2.cmml\" xref=\"p1.1.m1.1.36.2.2.2\">\n" +
      "              <interval closure=\"open\" xml:id=\"p1.1.m1.1.36.2.2.2.1.cmml\"/>\n" +
      "              <ci xml:id=\"p1.1.m1.1.30.cmml\" xref=\"p1.1.m1.1.30\">\uD835\uDD63</ci>\n" +
      "              <ci xml:id=\"p1.1.m1.1.33.cmml\" xref=\"p1.1.m1.1.33\">t</ci>\n" +
      "            </apply>\n" +
      "          </apply>\n" +
      "        </apply>\n" +
      "      </apply>\n" +
      "    </annotation-xml>\n" +
      "    <annotation xml:id=\"p1.1.m1.1b\" encoding=\"application/x-tex\" xref=\"p1.1.m1.1.cmml\">i\\hbar\\frac{\\partial}{\\partial t}\\Psi(\\mathbb{r},\\,t)=-\\frac{\\hbar^{2}}{2m}%\n" +
      "\\nabla^{2}\\Psi(\\mathbb{r},\\,t)+V(\\mathbb{r})\\Psi(\\mathbb{r},\\,t).</annotation>\n" +
      "  </semantics>\n" +
      "</math>";
    List<RawWikiDocument> docs = readWikiTextDocuments("augmentendwikitext.xml");
    RawWikiDocument schroedingerIn = docs.get(0);
    assertTrue( "the seed math tag was not found", schroedingerIn.text.contains(mathMLExtract) );
    MathTag tag = new MathTag(0,mathMLExtract, WikiTextUtils.MathMarkUpType.MATHML);
    String placeholder = tag.placeholder();
    ParsedWikiDocument shroedingerOut = TEST_INSTANCE.map(schroedingerIn);

    Set<String> identifiers = shroedingerOut.getIdentifiers().elementSet();
    assertTrue(identifiers.containsAll(Arrays.asList("Ψ", "V", "h", "λ", "ρ", "τ")));

    List<Formula> formulas = shroedingerOut.getFormulas();
    Formula formula = null;
    for (Formula f : formulas) {
      if (placeholder.equals(f.getKey())) {
        formula = f;
        break;
      }
    }
    //@TODO: reactivate tests
    assertNotNull("the placeholder was not found", formula);
    assertTrue("the placeholder was not part of the sentence", contains(formula, shroedingerOut.getSentences()));
  }

  private static boolean contains(Formula formula, List<Sentence> sentences) {
    Word mathWord = new Word(formula.getKey(), PosTag.MATH);
    for (Sentence sentence : sentences) {
      List<Word> words = sentence.getWords();
      if (words.contains(mathWord)) {
        return true;
      }
    }
    return false;
  }

  public static Formula randomElement(List<Formula> formulas) {
    int idx = RND.nextInt(formulas.size());
    return formulas.get(idx);
  }

  public static List<RawWikiDocument> readWikiTextDocuments(String testFile) throws Exception {
    String rawImput = getTestResource(testFile);
    String[] pages = rawImput.split("</page>");
    TextExtractorMapper textExtractor = new TextExtractorMapper();

    ListCollector<RawWikiDocument> out = new ListCollector<>();
    for (String page : pages) {
      textExtractor.flatMap(page, out);
    }

    return out.getList();
  }

  private static String getTestResource(String testFile) throws IOException {
    InputStream stream = PatternMatchingRelationFinder.class.getResourceAsStream(testFile);
    return IOUtils.toString(stream,"utf-8");
  }

  private static TextAnnotatorMapper createTestInstance() {
    try {
      TextAnnotatorMapper textAnnotator = new TextAnnotatorMapper(FlinkMlpCommandConfig.test());
      textAnnotator.open(null);
      return textAnnotator;
    } catch (Exception e) {
      throw Throwables.propagate(e);
    }
  }

  @Test
  public void tokenization_formulaSuffexed() throws Exception {
    String text = "The <math>x</math>-axis shows...";
    RawWikiDocument doc = new RawWikiDocument("some doc", 1, text);
    ParsedWikiDocument result = TEST_INSTANCE.map(doc);

    List<Formula> formulas = result.getFormulas();
    assertEquals(1, formulas.size());

    Sentence sentence = result.getSentences().get(0);

    List<Word> expected = Arrays.asList(new Word("The", "DT"), new Word("x", "ID"), new Word("-axis",
      "-SUF"), new Word("shows", "VBZ"), new Word("...", ":"));
    assertEquals(expected, sentence.getWords());
  }

}
