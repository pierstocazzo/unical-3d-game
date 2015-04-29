# Regole di scrittura codice #

  * Non risparmiare nei nomi delle variabili:
> `fVariab` NO<br>
<blockquote><code>firstVariable</code> SI</blockquote>

<ul><li>Spaziare gli operatori dai nomi delle variabili:<br>
</li></ul><blockquote><code>firstVariable=secondVariable</code> NO<br>
<code>firstVariable = secondVariable</code> SI</blockquote>

<ul><li>Evitare scorciatoie di operatori:<br>
</li></ul><blockquote><code>firstVariable += secondVariable</code> NO<br>
<code>firstVariable = firstVariable + secondVariable</code> SI</blockquote>

<ul><li>Spaziare i parametri passati ad una funzione tra di loro e anche dalle parentesi:<br>
</li></ul><blockquote><code>function(Type firstParam,Type secondParam)</code> NO<br>
<code>function( Type firstParam, Type secondParam )</code> SI</blockquote>

<ul><li>Usare <b>this</b> e <b>super</b> quando ci si riferisce, rispettivamente, ad un campo della classe e della superclasse, se si è in un contesto dove bisogna sottolienarne la differenza:<br>
</li></ul><blockquote><code>classField = firstParam;</code><br>
<code>superClassField = secondParam;</code>  NO<br>
<blockquote>
</blockquote><code>this.classField = firstParam;</code><br>
<code>super.superClassField = secondParam;</code>  SI<br>
<br>
<b>Javadoc Template:</b>
<pre><code>/** Function &lt;code&gt;functionName&lt;/code&gt; ... short description ...<br>
 *  <br>
 * @param firstParam - (FirstType) ... short description ...<br>
 * @param secondParam - (SecondType) ... short description ...<br>
 *     ...<br>
 *     ...<br>
 * @param nthParam - (NthType) ... short description ...<br>
 *<br>
 * @returns (returnType) ... short description ...<br>
 *<br>
 */<br>
modifier returnType functionName( FirstType firstParam, SecondType secondParam, ... NthType nthParam ) {<br>
     /* <br>
      * function code<br>
      */<br>
}<br>
</code></pre>