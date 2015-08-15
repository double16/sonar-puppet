/*
 * SonarQube Puppet Plugin
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Iain Adams and David RACODON
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.iadams.sonarqube.puppet.metrics;

import com.iadams.sonarqube.puppet.api.PuppetGrammar;
import com.iadams.sonarqube.puppet.api.PuppetMetric;
import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.Grammar;
import org.sonar.squidbridge.SquidAstVisitor;

public class ComplexityVisitor extends SquidAstVisitor<Grammar> {

  @Override
  public void init() {
    subscribeTo(
      PuppetGrammar.CLASSDEF,
      PuppetGrammar.DEFINITION,

      PuppetGrammar.RESOURCE,
      PuppetGrammar.RESOURCE_OVERRIDE,
      PuppetGrammar.RESOURCE_INST,

      PuppetGrammar.IF_STMT,
      PuppetGrammar.ELSEIF_STMT,
      PuppetGrammar.UNLESS_STMT,
      PuppetGrammar.SELECTVAL,
      PuppetGrammar.CASE_MATCHER,
      PuppetGrammar.BOOL_OPERATOR);
  }

  @Override
  public void visitNode(AstNode node) {
    if (node.is(PuppetGrammar.RESOURCE) && node.getFirstChild(PuppetGrammar.RESOURCE_INST) != null) {
      return;
    }
    getContext().peekSourceCode().add(PuppetMetric.COMPLEXITY, 1);
  }

}
