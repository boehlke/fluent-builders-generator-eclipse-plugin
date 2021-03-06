/**
 * Copyright (c) 2009-2010 fluent-builder-generator for Eclipse commiters.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Sabre Polska sp. z o.o. - initial implementation during Hackday
 */

package com.sabre.buildergenerator.functionaltest;

import com.sabre.buildergenerator.sourcegenerator.BuilderGenerator;
import com.sabre.buildergenerator.sourcegenerator.MethodConsumer;
import com.sabre.buildergenerator.sourcegenerator.MethodProvider;

public abstract class BuilderClassGeneratorTestBase<Type, TypeParameter, Method, JavaModelException extends Exception, CompilationUnit, File> extends TestBase<Type, TypeParameter, Method, JavaModelException, CompilationUnit, File> {
    private BuilderGenerator<Type, TypeParameter, Method, JavaModelException> generator;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        generator = createGenerator();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGenerateEmptyBuilder() throws Exception {
        // given
        Type buildClass = buildJavaSource().forPackage("testpkg").forClassName("MyClass")
            .withSourceLine("package testpkg;")
            .withSourceLine("")
            .withSourceLine("public class MyClass {")
            .withSourceLine("}")
            .buildType();

        // when
        String builderSource = generator.generateSource(buildClass, "builderpkg", "GeneratedBuilder", null, "with", "withAdded", "end");

        // then
        buildJavaSource().forPackage("builderpkg").forClassName("GeneratedBuilder")
            .withSourceLine(builderSource)
            .buildType();

        Type mainClass = buildJavaSource().forPackage("test").forClassName("MainClass")
            .withSourceLine("package test;")
            .withSourceLine("")
            .withSourceLine("import testpkg.MyClass;")
            .withSourceLine("import builderpkg.GeneratedBuilder;")
            .withSourceLine("")
            .withSourceLine("public class MainClass {")
            .withSourceLine("    public static void main(String[] args) {")
            .withSourceLine("        MyClass obj = GeneratedBuilder.myClass().build();")
            .withSourceLine("        assert obj != null;")
            .withSourceLine("    }")
            .withSourceLine("}")
            .buildType();

        assertEquals("Internal test failed for builder:\n" + builderSource, 0, runJavaFile(mainClass));
    }

    public void testGenerateSimplePrimitiveSetter() throws Exception {
        // given
        Type buildClass = buildJavaSource().forPackage("testpkg").forClassName("MyClass")
            .withSourceLine("package testpkg;")
            .withSourceLine("")
            .withSourceLine("public class MyClass {")
            .withSourceLine("    private int field;")
            .withSourceLine("")
            .withSourceLine("    public int getField() {")
            .withSourceLine("        return field;")
            .withSourceLine("    }")
            .withSourceLine("")
            .withSourceLine("    public void setField(int aField) {")
            .withSourceLine("        field = aField;")
            .withSourceLine("    }")
            .withSourceLine("}")
            .buildType();

        // when
        String builderSource = generator.generateSource(buildClass, "builderpkg", "GeneratedBuilder", null, "with", "withAdded", "end");

        // then
        buildJavaSource().forPackage("builderpkg").forClassName("GeneratedBuilder")
            .withSourceLine(builderSource)
            .buildType();

        Type mainClass = buildJavaSource().forPackage("test").forClassName("MainClass")
            .withSourceLine("package test;")
            .withSourceLine("")
            .withSourceLine("import testpkg.MyClass;")
            .withSourceLine("import builderpkg.GeneratedBuilder;")
            .withSourceLine("")
            .withSourceLine("public class MainClass {")
            .withSourceLine("    public static void main(String[] args) {")
            .withSourceLine("        MyClass obj = GeneratedBuilder.myClass().withField(5).build();")
            .withSourceLine("        assert obj.getField() == 5;")
            .withSourceLine("    }")
            .withSourceLine("}")
            .buildType();

        assertEquals("Internal test failed for builder:\n" + builderSource, 0, runJavaFile(mainClass));
    }

    public void testGenerateSimpleObjectSetter() throws Exception {
        // given
        Type buildClass = buildJavaSource().forPackage("testpkg").forClassName("MyClass")
            .withSourceLine("package testpkg;")
            .withSourceLine("")
            .withSourceLine("public class MyClass {")
            .withSourceLine("    private String field;")
            .withSourceLine("")
            .withSourceLine("    public String getField() {")
            .withSourceLine("        return field;")
            .withSourceLine("    }")
            .withSourceLine("")
            .withSourceLine("    public void setField(String aField) {")
            .withSourceLine("        field = aField;")
            .withSourceLine("    }")
            .withSourceLine("}")
            .buildType();

        // when
        String builderSource = generator.generateSource(buildClass, "builderpkg", "GeneratedBuilder", null, "with", "withAdded", "end");

        // then
        buildJavaSource().forPackage("builderpkg").forClassName("GeneratedBuilder")
            .withSourceLine(builderSource)
            .buildType();

        Type mainClass = buildJavaSource().forPackage("test").forClassName("MainClass")
            .withSourceLine("package test;")
            .withSourceLine("")
            .withSourceLine("import testpkg.MyClass;")
            .withSourceLine("import builderpkg.GeneratedBuilder;")
            .withSourceLine("")
            .withSourceLine("public class MainClass {")
            .withSourceLine("    public static void main(String[] args) {")
            .withSourceLine("        MyClass obj = GeneratedBuilder.myClass().withField(\"string\").build();")
            .withSourceLine("        assert obj.getField().equals(\"string\");")
            .withSourceLine("    }")
            .withSourceLine("}")
            .buildType();

        assertEquals("Internal test failed for builder:\n" + builderSource, 0, runJavaFile(mainClass));
    }

    public void testGenerateSimpleObjectSetterWithForeignPackage() throws Exception {
        // given
        Type buildClass = buildJavaSource().forPackage("testpkg").forClassName("MyClass")
            .withSourceLine("package testpkg;")
            .withSourceLine("")
            .withSourceLine("public class MyClass {")
            .withSourceLine("    private testpkg2.MyData field;")
            .withSourceLine("")
            .withSourceLine("    public testpkg2.MyData getField() {")
            .withSourceLine("        return field;")
            .withSourceLine("    }")
            .withSourceLine("")
            .withSourceLine("    public void setField(testpkg2.MyData aField) {")
            .withSourceLine("        field = aField;")
            .withSourceLine("    }")
            .withSourceLine("}")
            .buildType();
        buildJavaSource().forPackage("testpkg2").forClassName("MyData")
            .withSourceLine("package testpkg2;")
            .withSourceLine("")
            .withSourceLine("public class MyData {")
            .withSourceLine("}")
            .buildFile();

        // when
        String builderSource = generator.generateSource(buildClass, "builderpkg", "GeneratedBuilder", null, "with", "withAdded", "end");

        // then
        buildJavaSource().forPackage("builderpkg").forClassName("GeneratedBuilder")
            .withSourceLine(builderSource)
            .buildType();

        Type mainClass = buildJavaSource().forPackage("test").forClassName("MainClass")
            .withSourceLine("package test;")
            .withSourceLine("")
            .withSourceLine("import testpkg.MyClass;")
            .withSourceLine("import testpkg2.MyData;")
            .withSourceLine("import builderpkg.GeneratedBuilder;")
            .withSourceLine("")
            .withSourceLine("public class MainClass {")
            .withSourceLine("    public static void main(String[] args) {")
            .withSourceLine("        MyData data = new MyData();")
            .withSourceLine("        MyClass obj = GeneratedBuilder.myClass().withField(data).build();")
            .withSourceLine("        assert obj.getField() == data;")
            .withSourceLine("    }")
            .withSourceLine("}")
            .buildType();

        assertEquals("Internal test failed for builder:\n" + builderSource, 0, runJavaFile(mainClass));
    }

    public void testGenerateCollectionSetter() throws Exception {
        // given
        Type builderClass = buildJavaSource().forPackage("testpkg").forClassName("MyClass")
            .withSourceLine("package testpkg;")
            .withSourceLine("import java.util.List;")
            .withSourceLine("")
            .withSourceLine("public class MyClass {")
            .withSourceLine("    private List<String> fields;")
            .withSourceLine("")
            .withSourceLine("    public List<String> getFields() {")
            .withSourceLine("        return fields;")
            .withSourceLine("    }")
            .withSourceLine("")
            .withSourceLine("    public void setFields(List<String> aFields) {")
            .withSourceLine("        fields = aFields;")
            .withSourceLine("    }")
            .withSourceLine("}")
            .buildType();

        // when
        String builderSource = generator.generateSource(builderClass, "builderpkg", "GeneratedBuilder", null, "with", "withAdded", "end");

        // then
        buildJavaSource().forPackage("builderpkg").forClassName("GeneratedBuilder")
            .withSourceLine(builderSource)
            .buildType();

        Type mainClass = buildJavaSource().forPackage("test").forClassName("MainClass")
            .withSourceLine("package test;")
            .withSourceLine("")
            .withSourceLine("import testpkg.MyClass;")
            .withSourceLine("import builderpkg.GeneratedBuilder;")
            .withSourceLine("import java.util.Arrays;")
            .withSourceLine("")
            .withSourceLine("public class MainClass {")
            .withSourceLine("    public static void main(String[] args) {")
            .withSourceLine("        MyClass obj1 = GeneratedBuilder.myClass().withFields(Arrays.asList(\"field1\", \"field2\")).build();")
            .withSourceLine("        MyClass obj2 = GeneratedBuilder.myClass().withAddedField(\"field1\").withAddedField(\"field2\").build();")
            .withSourceLine("        assert obj1.getFields().size() == 2;")
            .withSourceLine("        assert obj1.getFields().get(0).equals(\"field1\");")
            .withSourceLine("        assert obj1.getFields().get(1).equals(\"field2\");")
            .withSourceLine("        assert obj2.getFields().size() == 2;")
            .withSourceLine("        assert obj2.getFields().get(0).equals(\"field1\");")
            .withSourceLine("        assert obj2.getFields().get(1).equals(\"field2\");")
            .withSourceLine("    }")
            .withSourceLine("}")
            .buildType();

        assertEquals("Internal test failed for builder:\n" + builderSource, 0, runJavaFile(mainClass));
    }

    public void testGenerateFieldBuilder() throws Exception {
        // given
        Type builderClass = buildJavaSource().forPackage("testpkg").forClassName("MyClass")
            .withSourceLine("package testpkg;")
            .withSourceLine("")
            .withSourceLine("public class MyClass {")
            .withSourceLine("    private MyData field;")
            .withSourceLine("")
            .withSourceLine("    public MyData getField() {")
            .withSourceLine("        return field;")
            .withSourceLine("    }")
            .withSourceLine("")
            .withSourceLine("    public void setField(MyData aField) {")
            .withSourceLine("        field = aField;")
            .withSourceLine("    }")
            .withSourceLine("}")
            .buildType();
        buildJavaSource().forPackage("testpkg").forClassName("MyData")
            .withSourceLine("package testpkg;")
            .withSourceLine("")
            .withSourceLine("public class MyData {")
            .withSourceLine("}")
            .buildFile();

        // when
        String builderSource = generator.generateSource(builderClass, "builderpkg", "GeneratedBuilder", null, "with", "withAdded", "end");

        // then
        buildJavaSource().forPackage("builderpkg").forClassName("GeneratedBuilder")
            .withSourceLine(builderSource)
            .buildType();

        Type mainClass = buildJavaSource().forPackage("test").forClassName("MainClass")
            .withSourceLine("package test;")
            .withSourceLine("")
            .withSourceLine("import testpkg.MyClass;")
            .withSourceLine("import testpkg.MyData;")
            .withSourceLine("import builderpkg.GeneratedBuilder;")
            .withSourceLine("")
            .withSourceLine("public class MainClass {")
            .withSourceLine("    public static void main(String[] args) {")
            .withSourceLine("        MyClass obj = GeneratedBuilder.myClass().withField().endField().build();")
            .withSourceLine("        assert obj.getField() != null;")
            .withSourceLine("    }")
            .withSourceLine("}")
            .buildType();

        assertEquals("Internal test failed for builder:\n" + builderSource, 0, runJavaFile(mainClass));
    }

    public void testGenerateCollectionFieldBuilder() throws Exception {
        // given
        Type builderClass = buildJavaSource().forPackage("testpkg").forClassName("MyClass")
            .withSourceLine("package testpkg;")
            .withSourceLine("import java.util.List;")
            .withSourceLine("")
            .withSourceLine("public class MyClass {")
            .withSourceLine("    private List<? extends MyData> fields;")
            .withSourceLine("")
            .withSourceLine("    public List<? extends MyData> getFields() {")
            .withSourceLine("        return fields;")
            .withSourceLine("    }")
            .withSourceLine("")
            .withSourceLine("    public void setFields(List<? extends MyData> aFields) {")
            .withSourceLine("        fields = aFields;")
            .withSourceLine("    }")
            .withSourceLine("}")
            .buildType();
        buildJavaSource().forPackage("testpkg").forClassName("MyData")
            .withSourceLine("package testpkg;")
            .withSourceLine("")
            .withSourceLine("public class MyData {")
            .withSourceLine("    private String dataField;")
            .withSourceLine("")
            .withSourceLine("    public String getDataField() {")
            .withSourceLine("        return dataField;")
            .withSourceLine("    }")
            .withSourceLine("")
            .withSourceLine("    public void setDataField(String aField) {")
            .withSourceLine("        dataField = aField;")
            .withSourceLine("    }")
            .withSourceLine("}")
            .buildFile();

        // when
        String builderSource = generator.generateSource(builderClass, "builderpkg", "GeneratedBuilder", null, "with", "withAdded", "end");

        // then
        buildJavaSource().forPackage("builderpkg").forClassName("GeneratedBuilder")
            .withSourceLine(builderSource)
            .buildType();

        Type mainClass = buildJavaSource().forPackage("test").forClassName("MainClass")
            .withSourceLine("package test;")
            .withSourceLine("")
            .withSourceLine("import testpkg.MyClass;")
            .withSourceLine("import testpkg.MyData;")
            .withSourceLine("import builderpkg.GeneratedBuilder;")
            .withSourceLine("")
            .withSourceLine("public class MainClass {")
            .withSourceLine("    public static void main(String[] args) {")
            .withSourceLine("        MyData field1 = new MyData();")
            .withSourceLine("        MyData field2 = new MyData();")
            .withSourceLine("        MyClass obj1 = GeneratedBuilder.myClass().withAddedField(field1)")
            .withSourceLine("                                                 .withAddedField(field2)")
            .withSourceLine("                                       .build();")
            .withSourceLine("        MyClass obj2 = GeneratedBuilder.myClass().withAddedField().withDataField(\"dataField1\").endField()")
            .withSourceLine("                                                .withAddedField().withDataField(\"dataField2\").endField()")
            .withSourceLine("                                       .build();")
            .withSourceLine("        assert obj1.getFields().size() == 2;")
            .withSourceLine("        assert obj1.getFields().get(0) == field1;")
            .withSourceLine("        assert obj1.getFields().get(1) == field2;")
            .withSourceLine("        assert obj2.getFields().size() == 2;")
            .withSourceLine("        assert obj2.getFields().get(0).getDataField().equals(\"dataField1\");")
            .withSourceLine("        assert obj2.getFields().get(1).getDataField().equals(\"dataField2\");")
            .withSourceLine("    }")
            .withSourceLine("}")
            .buildType();

        assertEquals("Internal test failed for builder:\n" + builderSource, 0, runJavaFile(mainClass));
    }

    public void testGenerateInnerBuilderWithSimpleSetter() throws Exception {
        // given
        Type builderClass = buildJavaSource().forPackage("testpkg").forClassName("MyClass")
            .withSourceLine("package testpkg;")
            .withSourceLine("")
            .withSourceLine("public class MyClass {")
            .withSourceLine("    private MyData field;")
            .withSourceLine("")
            .withSourceLine("    public MyData getField() {")
            .withSourceLine("        return field;")
            .withSourceLine("    }")
            .withSourceLine("")
            .withSourceLine("    public void setField(MyData aField) {")
            .withSourceLine("        field = aField;")
            .withSourceLine("    }")
            .withSourceLine("}")
            .buildType();
        buildJavaSource().forPackage("testpkg").forClassName("MyData")
            .withSourceLine("package testpkg;")
            .withSourceLine("")
            .withSourceLine("public class MyData {")
            .withSourceLine("    private String dataField;")
            .withSourceLine("")
            .withSourceLine("    public String getDataField() {")
            .withSourceLine("        return dataField;")
            .withSourceLine("    }")
            .withSourceLine("")
            .withSourceLine("    public void setDataField(String aField) {")
            .withSourceLine("        dataField = aField;")
            .withSourceLine("    }")
            .withSourceLine("}")
            .buildFile();

        // when
        String builderSource = generator.generateSource(builderClass, "builderpkg", "GeneratedBuilder", null, "with", "withAdded", "end");

        // then
        buildJavaSource().forPackage("builderpkg").forClassName("GeneratedBuilder")
            .withSourceLine(builderSource)
            .buildType();

        Type mainClass = buildJavaSource().forPackage("test").forClassName("MainClass")
            .withSourceLine("package test;")
            .withSourceLine("")
            .withSourceLine("import testpkg.MyClass;")
            .withSourceLine("import testpkg.MyData;")
            .withSourceLine("import builderpkg.GeneratedBuilder;")
            .withSourceLine("")
            .withSourceLine("public class MainClass {")
            .withSourceLine("    public static void main(String[] args) {")
            .withSourceLine("        MyClass obj = GeneratedBuilder.myClass().withField().withDataField(\"dataField\").endField().build();")
            .withSourceLine("        assert obj.getField().getDataField().equals(\"dataField\");")
            .withSourceLine("    }")
            .withSourceLine("}")
            .buildType();

        assertEquals("Internal test failed for builder:\n" + builderSource, 0, runJavaFile(mainClass));
    }

    public void testGenerateInnerBuilderWithCollectionSetter() throws Exception {
        // given
        Type builderClass = buildJavaSource().forPackage("testpkg").forClassName("MyClass")
            .withSourceLine("package testpkg;")
            .withSourceLine("")
            .withSourceLine("public class MyClass {")
            .withSourceLine("    private MyData field;")
            .withSourceLine("")
            .withSourceLine("    public MyData getField() {")
            .withSourceLine("        return field;")
            .withSourceLine("    }")
            .withSourceLine("")
            .withSourceLine("    public void setField(MyData aField) {")
            .withSourceLine("        field = aField;")
            .withSourceLine("    }")
            .withSourceLine("}")
            .buildType();
        buildJavaSource().forPackage("testpkg").forClassName("MyData")
            .withSourceLine("package testpkg;")
            .withSourceLine("import java.util.List;")
            .withSourceLine("")
            .withSourceLine("public class MyData {")
            .withSourceLine("    private List<String> dataFields;")
            .withSourceLine("")
            .withSourceLine("    public List<String> getDataFields() {")
            .withSourceLine("        return dataFields;")
            .withSourceLine("    }")
            .withSourceLine("")
            .withSourceLine("    public void setDataFields(List<String> aFields) {")
            .withSourceLine("        dataFields = aFields;")
            .withSourceLine("    }")
            .withSourceLine("}")
            .buildFile();

        // when
        String builderSource = generator.generateSource(builderClass, "builderpkg", "GeneratedBuilder", null, "with", "withAdded", "end");

        // then
        buildJavaSource().forPackage("builderpkg").forClassName("GeneratedBuilder")
            .withSourceLine(builderSource)
            .buildType();

        Type mainClass = buildJavaSource().forPackage("test").forClassName("MainClass")
            .withSourceLine("package test;")
            .withSourceLine("")
            .withSourceLine("import java.util.Arrays;")
            .withSourceLine("import testpkg.MyClass;")
            .withSourceLine("import testpkg.MyData;")
            .withSourceLine("import builderpkg.GeneratedBuilder;")
            .withSourceLine("")
            .withSourceLine("public class MainClass {")
            .withSourceLine("    public static void main(String[] args) {")
            .withSourceLine("        MyClass obj1 = GeneratedBuilder.myClass().withField()")
            .withSourceLine("                .withAddedDataField(\"dataField1\")")
            .withSourceLine("                .withAddedDataField(\"dataField2\")")
            .withSourceLine("                .endField().build();")
            .withSourceLine("        MyClass obj2 = GeneratedBuilder.myClass().withField()")
            .withSourceLine("                .withDataFields(Arrays.asList(\"dataField1\", \"dataField2\"))")
            .withSourceLine("                .endField().build();")
            .withSourceLine("        assert obj1.getField().getDataFields().size() == 2;")
            .withSourceLine("        assert obj1.getField().getDataFields().get(0).equals(\"dataField1\");")
            .withSourceLine("        assert obj1.getField().getDataFields().get(1).equals(\"dataField2\");")
            .withSourceLine("        assert obj2.getField().getDataFields().size() == 2;")
            .withSourceLine("        assert obj2.getField().getDataFields().get(0).equals(\"dataField1\");")
            .withSourceLine("        assert obj2.getField().getDataFields().get(1).equals(\"dataField2\");")
            .withSourceLine("    }")
            .withSourceLine("}")
            .buildType();

        assertEquals("Internal test failed for builder:\n" + builderSource, 0, runJavaFile(mainClass));
    }

    public void testGenerateInnerBuilderWithFieldBuilder() throws Exception {
        // given
        Type builderClass = buildJavaSource().forPackage("testpkg").forClassName("MyClass")
            .withSourceLine("package testpkg;")
            .withSourceLine("")
            .withSourceLine("public class MyClass {")
            .withSourceLine("    private MyData field;")
            .withSourceLine("")
            .withSourceLine("    public MyData getField() {")
            .withSourceLine("        return field;")
            .withSourceLine("    }")
            .withSourceLine("")
            .withSourceLine("    public void setField(MyData aField) {")
            .withSourceLine("        field = aField;")
            .withSourceLine("    }")
            .withSourceLine("}")
            .buildType();
        buildJavaSource().forPackage("testpkg").forClassName("MyData")
            .withSourceLine("package testpkg;")
            .withSourceLine("")
            .withSourceLine("public class MyData {")
            .withSourceLine("    private MyData2 dataField;")
            .withSourceLine("")
            .withSourceLine("    public MyData2 getDataField() {")
            .withSourceLine("        return dataField;")
            .withSourceLine("    }")
            .withSourceLine("")
            .withSourceLine("    public void setDataField(MyData2 aField) {")
            .withSourceLine("        dataField = aField;")
            .withSourceLine("    }")
            .withSourceLine("}")
            .buildFile();
        buildJavaSource().forPackage("testpkg").forClassName("MyData2")
            .withSourceLine("package testpkg;")
            .withSourceLine("")
            .withSourceLine("public class MyData2 {")
            .withSourceLine("    private String data2Field;")
            .withSourceLine("")
            .withSourceLine("    public String getData2Field() {")
            .withSourceLine("        return data2Field;")
            .withSourceLine("    }")
            .withSourceLine("")
            .withSourceLine("    public void setData2Field(String aField) {")
            .withSourceLine("        data2Field = aField;")
            .withSourceLine("    }")
            .withSourceLine("}")
            .buildFile();

        // when
        String builderSource = generator.generateSource(builderClass, "builderpkg", "GeneratedBuilder", null, "with", "withAdded", "end");

        // then
        buildJavaSource().forPackage("builderpkg").forClassName("GeneratedBuilder")
            .withSourceLine(builderSource)
            .buildType();

        Type mainClass = buildJavaSource().forPackage("test").forClassName("MainClass")
            .withSourceLine("package test;")
            .withSourceLine("")
            .withSourceLine("import testpkg.MyClass;")
            .withSourceLine("import testpkg.MyData;")
            .withSourceLine("import testpkg.MyData2;")
            .withSourceLine("import builderpkg.GeneratedBuilder;")
            .withSourceLine("")
            .withSourceLine("public class MainClass {")
            .withSourceLine("    public static void main(String[] args) {")
            .withSourceLine("        MyData2 data = new MyData2();")
            .withSourceLine("        MyClass obj1 = GeneratedBuilder.myClass().withField()")
            .withSourceLine("                .withDataField(data)")
            .withSourceLine("                .endField().build();")
            .withSourceLine("        MyClass obj2 = GeneratedBuilder.myClass().withField()")
            .withSourceLine("                .withDataField().withData2Field(\"data2Field\").endDataField()")
            .withSourceLine("                .endField().build();")
            .withSourceLine("        assert obj1.getField().getDataField() == data;")
            .withSourceLine("        assert obj2.getField().getDataField().getData2Field().equals(\"data2Field\");")
            .withSourceLine("    }")
            .withSourceLine("}")
            .buildType();

        assertEquals("Internal test failed for builder:\n" + builderSource, 0, runJavaFile(mainClass));
    }

    public void testGenerateInnerBuilderWithCollectionFieldBuilder() throws Exception {
        // given
        Type builderClass = buildJavaSource().forPackage("testpkg").forClassName("MyClass")
            .withSourceLine("package testpkg;")
            .withSourceLine("")
            .withSourceLine("public class MyClass {")
            .withSourceLine("    private MyData field;")
            .withSourceLine("")
            .withSourceLine("    public MyData getField() {")
            .withSourceLine("        return field;")
            .withSourceLine("    }")
            .withSourceLine("")
            .withSourceLine("    public void setField(MyData aField) {")
            .withSourceLine("        field = aField;")
            .withSourceLine("    }")
            .withSourceLine("}")
            .buildType();
        buildJavaSource().forPackage("testpkg").forClassName("MyData")
            .withSourceLine("package testpkg;")
            .withSourceLine("import java.util.List;")
            .withSourceLine("")
            .withSourceLine("public class MyData {")
            .withSourceLine("    private List<MyData2> dataFields;")
            .withSourceLine("")
            .withSourceLine("    public List<MyData2> getDataFields() {")
            .withSourceLine("        return dataFields;")
            .withSourceLine("    }")
            .withSourceLine("")
            .withSourceLine("    public void setDataFields(List<MyData2> aFields) {")
            .withSourceLine("        dataFields = aFields;")
            .withSourceLine("    }")
            .withSourceLine("}")
            .buildFile();
        buildJavaSource().forPackage("testpkg").forClassName("MyData2")
            .withSourceLine("package testpkg;")
            .withSourceLine("")
            .withSourceLine("public class MyData2 {")
            .withSourceLine("    private String data2Field;")
            .withSourceLine("")
            .withSourceLine("    public String getData2Field() {")
            .withSourceLine("        return data2Field;")
            .withSourceLine("    }")
            .withSourceLine("")
            .withSourceLine("    public void setData2Field(String aField) {")
            .withSourceLine("        data2Field = aField;")
            .withSourceLine("    }")
            .withSourceLine("}")
            .buildFile();

        // when
        String builderSource = generator.generateSource(builderClass, "builderpkg", "GeneratedBuilder", null, "with", "withAdded", "end");

        // then
        buildJavaSource().forPackage("builderpkg").forClassName("GeneratedBuilder")
            .withSourceLine(builderSource)
            .buildType();

        Type mainClass = buildJavaSource().forPackage("test").forClassName("MainClass")
            .withSourceLine("package test;")
            .withSourceLine("")
            .withSourceLine("import testpkg.MyClass;")
            .withSourceLine("import testpkg.MyData;")
            .withSourceLine("import testpkg.MyData2;")
            .withSourceLine("import builderpkg.GeneratedBuilder;")
            .withSourceLine("")
            .withSourceLine("public class MainClass {")
            .withSourceLine("    public static void main(String[] args) {")
            .withSourceLine("        MyData2 data1 = new MyData2();")
            .withSourceLine("        MyData2 data2 = new MyData2();")
            .withSourceLine("        MyClass obj1 = GeneratedBuilder.myClass().withField()")
            .withSourceLine("                                       .withAddedDataField(data1)")
            .withSourceLine("                                       .withAddedDataField(data2)")
            .withSourceLine("                                       .endField().build();")
            .withSourceLine("        MyClass obj2 = GeneratedBuilder.myClass().withField()")
            .withSourceLine("                                       .withAddedDataField().withData2Field(\"dataField1\").endDataField()")
            .withSourceLine("                                       .withAddedDataField().withData2Field(\"dataField2\").endDataField()")
            .withSourceLine("                                       .endField().build();")
            .withSourceLine("        assert obj1.getField().getDataFields().size() == 2;")
            .withSourceLine("        assert obj1.getField().getDataFields().get(0) == data1;")
            .withSourceLine("        assert obj1.getField().getDataFields().get(1) == data2;")
            .withSourceLine("        assert obj2.getField().getDataFields().size() == 2;")
            .withSourceLine("        assert obj2.getField().getDataFields().get(0).getData2Field().equals(\"dataField1\");")
            .withSourceLine("        assert obj2.getField().getDataFields().get(1).getData2Field().equals(\"dataField2\");")
            .withSourceLine("    }")
            .withSourceLine("}")
            .buildType();

        assertEquals("Internal test failed for builder:\n" + builderSource, 0, runJavaFile(mainClass));
    }

    public void testGenerateBuilderForClassExtendingGeneric() throws Exception {
        buildJavaSource().forPackage("testpkg").forClassName("MyException")
            .withSourceLine("package testpkg;")
            .withSourceLine("")
            .withSourceLine("public class MyException extends Exception {")
            .withSourceLine("}")
            .buildFile();
        buildJavaSource().forPackage("testpkg").forClassName("MyBase")
            .withSourceLine("package testpkg;")
            .withSourceLine("")
            .withSourceLine("public class MyBase<K, L extends Exception> {")
            .withSourceLine("    private K field;")
            .withSourceLine("")
            .withSourceLine("    public K getField() {")
            .withSourceLine("        return field;")
            .withSourceLine("    }")
            .withSourceLine("")
            .withSourceLine("    public void setField(K aField) throws L {")
            .withSourceLine("            field = aField;")
            .withSourceLine("    }")
            .withSourceLine("}")
            .buildFile();
        Type builderClass = buildJavaSource().forPackage("testpkg").forClassName("MyClass")
            .withSourceLine("package testpkg;")
            .withSourceLine("")
            .withSourceLine("public class MyClass extends MyBase<String, MyException> {")
            .withSourceLine("}")
            .buildType();

        // when
        String builderSource = generator.generateSource(builderClass, "builderpkg", "GeneratedBuilder", null, "with", "withAdded", "end");

        // then
        buildJavaSource().forPackage("builderpkg").forClassName("GeneratedBuilder")
            .withSourceLine(builderSource)
            .buildType();

        Type mainClass = buildJavaSource().forPackage("test").forClassName("MainClass")
            .withSourceLine("package test;")
            .withSourceLine("")
            .withSourceLine("import testpkg.MyClass;")
            .withSourceLine("import builderpkg.GeneratedBuilder;")
            .withSourceLine("")
            .withSourceLine("public class MainClass {")
            .withSourceLine("    public static void main(String[] args) throws Exception {")
            .withSourceLine("        MyClass obj = GeneratedBuilder.myClass().withField(\"string\").build();")
            .withSourceLine("        assert obj.getField().equals(\"string\");")
            .withSourceLine("    }")
            .withSourceLine("}")
            .buildType();

        assertEquals("Internal test failed for builder:\n" + builderSource, 0, runJavaFile(mainClass));
    }

    public void testGenerateParametrizedTypeFieldSetter() throws Exception {
        buildJavaSource().forPackage("testpkg").forClassName("Generic")
            .withSourceLine("package testpkg;")
            .withSourceLine("")
            .withSourceLine("public class Generic<T> {")
            .withSourceLine("    private T field;")
            .withSourceLine("")
            .withSourceLine("    public T getGenericField() {")
            .withSourceLine("        return field;")
            .withSourceLine("    }")
            .withSourceLine("")
            .withSourceLine("    public void setGenericField(T aField) {")
            .withSourceLine("        field = aField;")
            .withSourceLine("    }")
            .withSourceLine("}")
            .buildFile();
        Type builderClass = buildJavaSource().forPackage("testpkg").forClassName("MyClass")
            .withSourceLine("package testpkg;")
            .withSourceLine("")
            .withSourceLine("public class MyClass {")
            .withSourceLine("    private Generic<String> field;")
            .withSourceLine("")
            .withSourceLine("    public Generic<String> getField() {")
            .withSourceLine("        return field;")
            .withSourceLine("    }")
            .withSourceLine("")
            .withSourceLine("    public void setField(Generic<String> aField) {")
            .withSourceLine("        field = aField;")
            .withSourceLine("    }")
            .withSourceLine("}")
            .buildType();

        // when
        String builderSource = generator.generateSource(builderClass, "builderpkg", "GeneratedBuilder", null, "with", "withAdded", "end");

        // then
        buildJavaSource().forPackage("builderpkg").forClassName("GeneratedBuilder")
            .withSourceLine(builderSource)
            .buildType();

        Type mainClass = buildJavaSource().forPackage("test").forClassName("MainClass")
            .withSourceLine("package test;")
            .withSourceLine("")
            .withSourceLine("import testpkg.MyClass;")
            .withSourceLine("import testpkg.Generic;")
            .withSourceLine("import builderpkg.GeneratedBuilder;")
            .withSourceLine("")
            .withSourceLine("public class MainClass {")
            .withSourceLine("    public static void main(String[] args) {")
            .withSourceLine("        Generic<String> field = new Generic<String>();")
            .withSourceLine("        MyClass obj1 = GeneratedBuilder.myClass().withField(field).build();")
            .withSourceLine("        assert obj1.getField() == field;")
            .withSourceLine("        MyClass obj2 = GeneratedBuilder.myClass().withField().withGenericField(\"generic\").endField().build();")
            .withSourceLine("        assert obj2.getField().getGenericField().equals(\"generic\");")
            .withSourceLine("    }")
            .withSourceLine("}")
            .buildType();

        assertEquals("Internal test failed for builder:\n" + builderSource, 0, runJavaFile(mainClass));
    }

    public void testGenerateBuilderForParametrizedTypes() throws Exception {
        // given
        Type builderClass = buildJavaSource().forPackage("testpkg").forClassName("MyClass")
            .withSourceLine("package testpkg;")
            .withSourceLine("")
            .withSourceLine("public class MyClass {")
            .withSourceLine("    private MyData field;")
            .withSourceLine("")
            .withSourceLine("    public MyData getField() {")
            .withSourceLine("        return field;")
            .withSourceLine("    }")
            .withSourceLine("")
            .withSourceLine("    public void setField(MyData aField) {")
            .withSourceLine("        field = aField;")
            .withSourceLine("    }")
            .withSourceLine("}")
            .buildType();
        buildJavaSource().forPackage("testpkg").forClassName("MyData")
            .withSourceLine("package testpkg;")
            .withSourceLine("import java.util.List;")
            .withSourceLine("")
            .withSourceLine("public class MyData {")
            .withSourceLine("    private List<MyData2<String>> dataFields;")
            .withSourceLine("")
            .withSourceLine("    public List<MyData2<String>> getDataFields() {")
            .withSourceLine("        return dataFields;")
            .withSourceLine("    }")
            .withSourceLine("")
            .withSourceLine("    public void setDataFields(List<MyData2<String>> aFields) {")
            .withSourceLine("        dataFields = aFields;")
            .withSourceLine("    }")
            .withSourceLine("}")
            .buildFile();
        buildJavaSource().forPackage("testpkg").forClassName("MyData2")
            .withSourceLine("package testpkg;")
            .withSourceLine("")
            .withSourceLine("public class MyData2<C extends CharSequence> {")
            .withSourceLine("    private C data2Field;")
            .withSourceLine("")
            .withSourceLine("    public C getData2Field() {")
            .withSourceLine("        return data2Field;")
            .withSourceLine("    }")
            .withSourceLine("")
            .withSourceLine("    public void setData2Field(C aField) {")
            .withSourceLine("        data2Field = aField;")
            .withSourceLine("    }")
            .withSourceLine("}")
            .buildFile();

        // when
        String builderSource = generator.generateSource(builderClass, "builderpkg", "GeneratedBuilder", null, "with", "withAdded", "end");

        // then
        buildJavaSource().forPackage("builderpkg").forClassName("GeneratedBuilder")
            .withSourceLine(builderSource)
            .buildType();

        Type mainClass = buildJavaSource().forPackage("test").forClassName("MainClass")
            .withSourceLine("package test;")
            .withSourceLine("")
            .withSourceLine("import testpkg.MyClass;")
            .withSourceLine("import testpkg.MyData;")
            .withSourceLine("import testpkg.MyData2;")
            .withSourceLine("import builderpkg.GeneratedBuilder;")
            .withSourceLine("")
            .withSourceLine("public class MainClass {")
            .withSourceLine("    public static void main(String[] args) {")
            .withSourceLine("        MyData2 data1 = new MyData2();")
            .withSourceLine("        MyData2 data2 = new MyData2();")
            .withSourceLine("        MyClass obj1 = GeneratedBuilder.myClass().withField()")
            .withSourceLine("                                       .withAddedDataField(data1)")
            .withSourceLine("                                       .withAddedDataField(data2)")
            .withSourceLine("                                       .endField().build();")
            .withSourceLine("        MyClass obj2 = GeneratedBuilder.myClass().withField()")
            .withSourceLine("                                       .withAddedDataField().withData2Field(\"dataField1\").endDataField()")
            .withSourceLine("                                       .withAddedDataField().withData2Field(\"dataField2\").endDataField()")
            .withSourceLine("                                       .endField().build();")
            .withSourceLine("        assert obj1.getField().getDataFields().size() == 2;")
            .withSourceLine("        assert obj1.getField().getDataFields().get(0) == data1;")
            .withSourceLine("        assert obj1.getField().getDataFields().get(1) == data2;")
            .withSourceLine("        assert obj2.getField().getDataFields().size() == 2;")
            .withSourceLine("        assert obj2.getField().getDataFields().get(0).getData2Field().equals(\"dataField1\");")
            .withSourceLine("        assert obj2.getField().getDataFields().get(1).getData2Field().equals(\"dataField2\");")
            .withSourceLine("    }")
            .withSourceLine("}")
            .buildType();

        assertEquals("Internal test failed for builder:\n" + builderSource, 0, runJavaFile(mainClass));
    }

    public void testShouldBuildAllRequestedSetters() throws Exception {
        // given
        final Type companyClass = buildJavaSource().forPackage("testpkg").forClassName("Company")
            .withSourceLine("package testpkg;")
            .withSourceLine("import java.util.List;")
            .withSourceLine("")
            .withSourceLine("public class Company {")
            .withSourceLine("    private String name;")
            .withSourceLine("    private Address location;")
            .withSourceLine("    private List<Person> employees;")
            .withSourceLine("")
            .withSourceLine("    public String getName() {")
            .withSourceLine("        return name;")
            .withSourceLine("    }")
            .withSourceLine("")
            .withSourceLine("    public void setName(String aName) {")
            .withSourceLine("        name = aName;")
            .withSourceLine("    }")
            .withSourceLine("")
            .withSourceLine("    public Address getLocation() {")
            .withSourceLine("        return location;")
            .withSourceLine("    }")
            .withSourceLine("")
            .withSourceLine("    public void setLocation(Address aLocation) {")
            .withSourceLine("        location = aLocation;")
            .withSourceLine("    }")
            .withSourceLine("")
            .withSourceLine("    public List<Person> getEmployees() {")
            .withSourceLine("        return employees;")
            .withSourceLine("    }")
            .withSourceLine("")
            .withSourceLine("    public void setEmployees(List<Person> aEmployees) {")
            .withSourceLine("        employees = aEmployees;")
            .withSourceLine("    }")
            .withSourceLine("")
            .withSourceLine("}")
            .buildType();
        final Type personClass = buildJavaSource().forPackage("testpkg").forClassName("Person")
            .withSourceLine("package testpkg;")
            .withSourceLine("")
            .withSourceLine("public class Person {")
            .withSourceLine("    private String firstName;")
            .withSourceLine("    private String lastName;")
            .withSourceLine("    private Address address;")
            .withSourceLine("")
            .withSourceLine("    public String getFirstName() {")
            .withSourceLine("        return firstName;")
            .withSourceLine("    }")
            .withSourceLine("")
            .withSourceLine("    public void setFirstName(String aFirstName) {")
            .withSourceLine("        firstName = aFirstName;")
            .withSourceLine("    }")
            .withSourceLine("")
            .withSourceLine("    public String getLastName() {")
            .withSourceLine("        return lastName;")
            .withSourceLine("    }")
            .withSourceLine("")
            .withSourceLine("    public void setLastName(String aLastName) {")
            .withSourceLine("        lastName = aLastName;")
            .withSourceLine("    }")
            .withSourceLine("")
            .withSourceLine("    public Address getAddress() {")
            .withSourceLine("        return address;")
            .withSourceLine("    }")
            .withSourceLine("")
            .withSourceLine("    public void setAddress(Address aAddress) {")
            .withSourceLine("        address = aAddress;")
            .withSourceLine("    }")
            .withSourceLine("")
            .withSourceLine("}")
            .buildType();
        final Type addressClass = buildJavaSource().forPackage("testpkg").forClassName("Address")
            .withSourceLine("package testpkg;")
            .withSourceLine("")
            .withSourceLine("public class Address {")
            .withSourceLine("    private String city;")
            .withSourceLine("    private String street;")
            .withSourceLine("    private int number;")
            .withSourceLine("")
            .withSourceLine("    public String getCity() {")
            .withSourceLine("        return city;")
            .withSourceLine("    }")
            .withSourceLine("")
            .withSourceLine("    public void setCity(String aCity) {")
            .withSourceLine("        city = aCity;")
            .withSourceLine("    }")
            .withSourceLine("")
            .withSourceLine("    public String getStreet() {")
            .withSourceLine("        return street;")
            .withSourceLine("    }")
            .withSourceLine("")
            .withSourceLine("    public void setStreet(String aStreet) {")
            .withSourceLine("        street = aStreet;")
            .withSourceLine("    }")
            .withSourceLine("")
            .withSourceLine("    public int getNumber() {")
            .withSourceLine("        return number;")
            .withSourceLine("    }")
            .withSourceLine("")
            .withSourceLine("    public void setNumber(int aNumber) {")
            .withSourceLine("        number = aNumber;")
            .withSourceLine("    }")
            .withSourceLine("}")
            .buildType();
        MethodProvider<Type, Method> methodProvider = new MethodProvider<Type, Method>() {

            public void process(MethodConsumer<Type, Method> consumer) {
                consumer.nextMethod(companyClass, findMethod(companyClass, "setName", new String[]{"Qjava.lang.String;"}));
                consumer.nextMethod(companyClass, findMethod(companyClass, "setLocation", new String[]{"Qtestpkg.Address;"}));
                consumer.nextMethod(companyClass, findMethod(companyClass, "setEmployees", new String[]{"Qjava.util.List<Qtestpkg.Person;>;"}));

                consumer.nextMethod(personClass, findMethod(personClass, "setFirstName", new String[]{"Qjava.lang.String;"}));
                consumer.nextMethod(personClass, findMethod(personClass, "setLastName", new String[]{"Qjava.lang.String;"}));
                consumer.nextMethod(personClass, findMethod(personClass, "setAddress", new String[]{"Qtestpkg.Address;"}));

                consumer.nextMethod(addressClass, findMethod(addressClass, "setCity", new String[]{"Qjava.lang.String;"}));
                consumer.nextMethod(addressClass, findMethod(addressClass, "setStreet", new String[]{"Qjava.lang.String;"}));
                consumer.nextMethod(addressClass, findMethod(addressClass, "setNumber", new String[]{"I"}));
            }

        };

        // when
        String builderSource = generator.generateSource(companyClass, "builderpkg", "GeneratedBuilder", methodProvider, "with", "withAdded", "end");

        // then
        buildJavaSource().forPackage("builderpkg").forClassName("GeneratedBuilder")
            .withSourceLine(builderSource)
            .buildType();

        Type mainClass = buildJavaSource().forPackage("test").forClassName("MainClass")
            .withSourceLine("package test;")
            .withSourceLine("")
            .withSourceLine("import java.util.List;")
            .withSourceLine("")
            .withSourceLine("import testpkg.Address;")
            .withSourceLine("import testpkg.Person;")
            .withSourceLine("import testpkg.Company;")
            .withSourceLine("")
            .withSourceLine("import builderpkg.GeneratedBuilder;")
            .withSourceLine("")
            .withSourceLine("public class MainClass {")
            .withSourceLine("    public static void main(String[] args) {")
            .withSourceLine("        Object companyBuilder = GeneratedBuilder.company();")
            .withSourceLine("        assert objectHasMethod(companyBuilder, \"withName\", String.class);")
            .withSourceLine("        assert objectHasMethod(companyBuilder, \"withLocation\", Address.class);")
            .withSourceLine("        assert objectHasMethod(companyBuilder, \"withLocation\");")
            .withSourceLine("        assert objectHasMethod(companyBuilder, \"withEmployees\", List.class);")
            .withSourceLine("        assert objectHasMethod(companyBuilder, \"withAddedEmployee\", Person.class);")
            .withSourceLine("        assert objectHasMethod(companyBuilder, \"withAddedEmployee\");")
            .withSourceLine("")
            .withSourceLine("        Object locationBuilder = GeneratedBuilder.company().withLocation();")
            .withSourceLine("        assert objectHasMethod(locationBuilder, \"withCity\", String.class);")
            .withSourceLine("        assert objectHasMethod(locationBuilder, \"withStreet\", String.class);")
            .withSourceLine("        assert objectHasMethod(locationBuilder, \"withNumber\", Integer.TYPE);")
            .withSourceLine("")
            .withSourceLine("        Object employeeBuilder = GeneratedBuilder.company().withAddedEmployee();")
            .withSourceLine("        assert objectHasMethod(employeeBuilder, \"withFirstName\", String.class);")
            .withSourceLine("        assert objectHasMethod(employeeBuilder, \"withLastName\", String.class);")
            .withSourceLine("        assert objectHasMethod(employeeBuilder, \"withAddress\", Address.class);")
            .withSourceLine("        assert objectHasMethod(employeeBuilder, \"withAddress\");")
            .withSourceLine("")
            .withSourceLine("        Object addressBuilder = GeneratedBuilder.company().withAddedEmployee().withAddress();")
            .withSourceLine("        assert objectHasMethod(addressBuilder, \"withCity\", String.class);")
            .withSourceLine("        assert objectHasMethod(addressBuilder, \"withStreet\", String.class);")
            .withSourceLine("        assert objectHasMethod(addressBuilder, \"withNumber\", Integer.TYPE);")
            .withSourceLine("    }")
            .withSourceLine("")
            .withSourceLine("    private static boolean objectHasMethod(Object object, String methodName, Class... parameterTypes) {")
            .withSourceLine("        try {")
            .withSourceLine("            return object.getClass().getMethod(methodName, parameterTypes) != null;")
            .withSourceLine("        } catch (NoSuchMethodException ex) {")
            .withSourceLine("            return false;")
            .withSourceLine("        }")
            .withSourceLine("    }")
            .withSourceLine("}")
            .buildType();

        assertEquals("Internal test failed for builder:\n" + builderSource, 0, runJavaFile(mainClass));
    }

    public void testGenerateBuilderForGeneric() throws Exception {
        // given
        Type builderClass = buildJavaSource().forPackage("testpkg").forClassName("Generic")
            .withSourceLine("package testpkg;")
            .withSourceLine("import java.util.List;")
            .withSourceLine("")
            .withSourceLine("public class Generic<T> {")
            .withSourceLine("    private List<T> elements;")
            .withSourceLine("")
            .withSourceLine("    public List<T> getElements() {")
            .withSourceLine("        return elements;")
            .withSourceLine("    }")
            .withSourceLine("")
            .withSourceLine("    public void setElements(List<T> aElements) {")
            .withSourceLine("        elements = aElements;")
            .withSourceLine("    }")
            .withSourceLine("}")
            .buildType();

        // when
        String builderSource = generator.generateSource(builderClass, "builderpkg", "GeneratedBuilder", null, "with", "withAdded", "end");

        // then
        buildJavaSource().forPackage("builderpkg").forClassName("GeneratedBuilder")
            .withSourceLine(builderSource)
            .buildType();

        Type mainClass = buildJavaSource().forPackage("test").forClassName("MainClass")
            .withSourceLine("package test;")
            .withSourceLine("")
            .withSourceLine("import testpkg.Generic;")
            .withSourceLine("import builderpkg.GeneratedBuilder;")
            .withSourceLine("")
            .withSourceLine("public class MainClass {")
            .withSourceLine("    public static void main(String[] args) {")
            .withSourceLine("        Generic<String> o = GeneratedBuilder.<String>generic().withAddedElement(\"abc\").build();")
            .withSourceLine("        assert o.getElements().get(0).equals(\"abc\");")
            .withSourceLine("    }")
            .withSourceLine("}")
            .buildType();

        assertEquals("Internal test failed for builder:\n" + builderSource, 0, runJavaFile(mainClass));
    }

    public void testGenerateBuilderForGenericComplex() throws Exception {
        // given
        buildJavaSource().forPackage("testpkg").forClassName("Room")
            .withSourceLine("package testpkg;")
            .withSourceLine("import java.util.List;")
            .withSourceLine("")
            .withSourceLine("public class Room<P extends Property, RP extends RatePlan> {")
            .withSourceLine("    private P property;")
            .withSourceLine("    private List<RP> ratePlans;")
            .withSourceLine("")
            .withSourceLine("    public P getProperty() {")
            .withSourceLine("        return property;")
            .withSourceLine("    }")
            .withSourceLine("")
            .withSourceLine("    public void setProperty(P aProperty) {")
            .withSourceLine("        property = aProperty;")
            .withSourceLine("    }")
            .withSourceLine("")
            .withSourceLine("    public List<RP> getRatePlans() {")
            .withSourceLine("        return ratePlans;")
            .withSourceLine("    }")
            .withSourceLine("")
            .withSourceLine("    public void setRatePlans(List<RP> aRatePlans) {")
            .withSourceLine("        ratePlans = aRatePlans;")
            .withSourceLine("    }")
            .withSourceLine("}")
            .buildType();
        buildJavaSource().forPackage("testpkg").forClassName("RatePlan")
            .withSourceLine("package testpkg;")
            .withSourceLine("")
            .withSourceLine("public class RatePlan<R extends Room> {")
            .withSourceLine("    private R room;")
            .withSourceLine("")
            .withSourceLine("    public R getRoom() {")
            .withSourceLine("        return room;")
            .withSourceLine("    }")
            .withSourceLine("")
            .withSourceLine("    public void setRoom(R aRoom) {")
            .withSourceLine("        room = aRoom;")
            .withSourceLine("    }")
            .withSourceLine("}")
            .buildType();
        Type builderClass = buildJavaSource().forPackage("testpkg").forClassName("Property")
            .withSourceLine("package testpkg;")
            .withSourceLine("import java.util.List;")
            .withSourceLine("")
            .withSourceLine("public class Property<R extends Room, RP extends RatePlan> {")
            .withSourceLine("    private List<R> rooms;")
            .withSourceLine("")
            .withSourceLine("    public List<R> getRooms() {")
            .withSourceLine("        return rooms;")
            .withSourceLine("    }")
            .withSourceLine("")
            .withSourceLine("    public void setRooms(List<R> aRooms) {")
            .withSourceLine("        rooms = aRooms;")
            .withSourceLine("    }")
            .withSourceLine("}")
            .buildType();

        // when
        String builderSource = generator.generateSource(builderClass, "builderpkg", "GeneratedBuilder", null, "with", "withAdded", "end");

        // then
        buildJavaSource().forPackage("builderpkg").forClassName("GeneratedBuilder")
            .withSourceLine(builderSource)
            .buildType();

        Type mainClass = buildJavaSource().forPackage("test").forClassName("MainClass")
            .withSourceLine("package test;")
            .withSourceLine("")
            .withSourceLine("import testpkg.Property;")
            .withSourceLine("import testpkg.Room;")
            .withSourceLine("import testpkg.RatePlan;")
            .withSourceLine("import builderpkg.GeneratedBuilder;")
            .withSourceLine("")
            .withSourceLine("public class MainClass {")
            .withSourceLine("    public static void main(String[] args) {")
            .withSourceLine("        Property<?, ?> property = GeneratedBuilder.property().withAddedRoom(null).build();")
//            .withSourceLine("        Property property = GeneratedBuilder.property().withAddedRoom()")
//            .withSourceLine("                                            .withAddedRatePlan()")
//            .withSourceLine("                                            .endRatePlan()")
//            .withSourceLine("                                            .endRoom().build();")
//            .withSourceLine("        assert property.getRooms().size() == 1;")
//            .withSourceLine("        assert property.getRooms().get(0).getName().equals(\"room1\");")
//            .withSourceLine("        assert property.getRooms().get(0).getRatePlans().size() == 1;")
//            .withSourceLine("        assert property.getRooms().get(0).getRatePlans().get(0).getName().equals(\"ratePlan1\");")
            .withSourceLine("    }")
            .withSourceLine("}")
            .buildType();

        assertEquals("Internal test failed for builder:\n" + builderSource, 0, runJavaFile(mainClass));
    }

    public void testGenerateBuilderForCyclicReference() throws Exception {
        // given
        Type builderClass = buildJavaSource().forPackage("testpkg").forClassName("MyClass")
            .withSourceLine("package testpkg;")
            .withSourceLine("")
            .withSourceLine("public class MyClass {")
            .withSourceLine("    private MyClass field;")
            .withSourceLine("")
            .withSourceLine("    public MyClass getField() {")
            .withSourceLine("        return field;")
            .withSourceLine("    }")
            .withSourceLine("")
            .withSourceLine("    public void setField(MyClass aField) {")
            .withSourceLine("        field = aField;")
            .withSourceLine("    }")
            .withSourceLine("}")
            .buildType();

        // when
        String builderSource = generator.generateSource(builderClass, "builderpkg", "GeneratedBuilder", null, "with", "withAdded", "end");

        // then
        buildJavaSource().forPackage("builderpkg").forClassName("GeneratedBuilder")
            .withSourceLine(builderSource)
            .buildType();

        Type mainClass = buildJavaSource().forPackage("test").forClassName("MainClass")
            .withSourceLine("package test;")
            .withSourceLine("")
            .withSourceLine("import testpkg.MyClass;")
            .withSourceLine("import builderpkg.GeneratedBuilder;")
            .withSourceLine("")
            .withSourceLine("public class MainClass {")
            .withSourceLine("    public static void main(String[] args) {")
            .withSourceLine("        MyClass obj = GeneratedBuilder.myClass().withField().withField()")
            .withSourceLine("                .endField().endField().build();")
            .withSourceLine("        assert obj.getField().getField() != null;")
            .withSourceLine("    }")
            .withSourceLine("}")
            .buildType();

        assertEquals("Internal test failed for builder:\n" + builderSource, 0, runJavaFile(mainClass));
    }
}
