package org.gta;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/** @author fede lopez */
public class SettingsUpdaterTest {

    private File file;

    @Test
    public void update() throws IOException {
        SettingsUpdater sut = createSettingsUpdater("au.com.pks.rippledown.test", "RippledownTest.java", "doItTest");

        sut.update();

        Properties actualProperties = new Properties();
        actualProperties.load(new FileInputStream(file.getAbsolutePath()));

        assertRunBlock(actualProperties, "t", "f", "f");
        assertPackageBlock(actualProperties, "au.com.pks.rippledown", "", "");
        assertClassBlock(actualProperties, "RippledownTest", "", "");
        assertMethodBlock(actualProperties, "doItTest", "", "");
    }

    @Test
    public void updateWithEmptyMethod() throws IOException {
        SettingsUpdater sut = createSettingsUpdater("au.com.pks.rippledown.test", "RippledownTest.java", "");

        sut.update();

        Properties actualProperties = new Properties();
        actualProperties.load(new FileInputStream(file.getAbsolutePath()));

        assertRunBlock(actualProperties, "t", "f", "f");
        assertPackageBlock(actualProperties, "au.com.pks.rippledown", "", "");
        assertClassBlock(actualProperties, "RippledownTest", "", "");
        assertMethodBlock(actualProperties, "", "", "");
    }

    @Test
    public void updateForFunctionTest() throws IOException {
        SettingsUpdater sut = createSettingsUpdater("au.com.pks.rippledown.functiontest", "AttributeEditor.java", null);

        sut.update();

        Properties actualProperties = new Properties();
        actualProperties.load(new FileInputStream(file.getAbsolutePath()));

        assertRunBlock(actualProperties, "f", "t", "f");
        assertPackageBlock(actualProperties, "au.com.pks.rippledown.functiontest", "", "");
        assertClassBlock(actualProperties, "AttributeEditor", "", "");
        assertMethodBlock(actualProperties, "", "", "");
    }

    @Test
    public void updateForFunctionTestWithTestMethod() throws IOException {
        SettingsUpdater sut = createSettingsUpdater("au.com.pks.rippledown.functiontest", "AttributeEditor.java", "performTest");

        sut.update();

        Properties actualProperties = new Properties();
        actualProperties.load(new FileInputStream(file.getAbsolutePath()));

        assertRunBlock(actualProperties, "f", "t", "f");
        assertPackageBlock(actualProperties, "au.com.pks.rippledown.functiontest", "", "");
        assertClassBlock(actualProperties, "AttributeEditor", "", "");
        assertMethodBlock(actualProperties, "", "", "");
    }

    @Test
    public void updateForLoadTest() throws IOException {
        SettingsUpdater sut = createSettingsUpdater("au.com.pks.rippledown.loadtest", "AttributeEditor.java", "performTest");

        sut.update();

        Properties actualProperties = new Properties();
        actualProperties.load(new FileInputStream(file.getAbsolutePath()));

        assertRunBlock(actualProperties, "f", "f", "t");
        assertPackageBlock(actualProperties, "au.com.pks.rippledown.loadtest", "", "");
        assertClassBlock(actualProperties, "AttributeEditor", "", "");
        assertMethodBlock(actualProperties, "", "", "");
    }

    @Test
    public void updateForUnitTestPackageOnly() throws IOException {

        SettingsUpdater sut = createSettingsUpdater("au.com.pks.rippledown.test", null, null);

        sut.update();

        Properties actualProperties = new Properties();
        actualProperties.load(new FileInputStream(file.getAbsolutePath()));

        assertRunBlock(actualProperties, "t", "f", "f");
        assertPackageBlock(actualProperties, "au.com.pks.rippledown", "", "");
        assertClassBlock(actualProperties, "", "", "");
        assertMethodBlock(actualProperties, "", "", "");
    }

    @Test
    public void updateForUnitTestClassNotTestOnly() throws IOException {

        SettingsUpdater sut = createSettingsUpdater("au.com.pks.rippledown", "JackTheRipper", "doRip");

        sut.update();

        Properties actualProperties = new Properties();
        actualProperties.load(new FileInputStream(file.getAbsolutePath()));

        assertRunBlock(actualProperties, "t", "f", "f");
        assertPackageBlock(actualProperties, "au.com.pks.rippledown", "", "");
        assertClassBlock(actualProperties, "JackTheRipperTest", "", "");
        assertMethodBlock(actualProperties, "doRipTest", "", "");
    }

    @Test
    public void updateForFunctionTestPackageOnly() throws IOException {

        SettingsUpdater sut = createSettingsUpdater("au.com.pks.rippledown.functiontest", null, null);

        sut.update();

        Properties actualProperties = new Properties();
        actualProperties.load(new FileInputStream(file.getAbsolutePath()));

        assertRunBlock(actualProperties, "f", "t", "f");
        assertPackageBlock(actualProperties, "au.com.pks.rippledown.functiontest", "", "");
        assertClassBlock(actualProperties, "", "", "");
        assertMethodBlock(actualProperties, "", "", "");
    }

    @Test
    public void updateForLoadTestPackageOnly() throws IOException {

        SettingsUpdater sut = createSettingsUpdater("au.com.pks.rippledown.loadtest", null, null);

        sut.update();

        Properties actualProperties = new Properties();
        actualProperties.load(new FileInputStream(file.getAbsolutePath()));

        assertRunBlock(actualProperties, "f", "f", "t");
        assertPackageBlock(actualProperties, "au.com.pks.rippledown.loadtest", "", "");
        assertClassBlock(actualProperties, "", "", "");
        assertMethodBlock(actualProperties, "", "", "");
    }

    @Test
    public void constructor() {
        SettingsUpdater.Builder builder = new SettingsUpdater.Builder();
        builder.gtaSettingsFile(file).packageName("org.gta.test").className("HelloWorld").methodName("helloWorldTest");

        SettingsUpdater settingsUpdater = builder.build();

        Assert.assertNotNull("Settings updater should not be null", settingsUpdater);
    }

    private SettingsUpdater createSettingsUpdater(String packageName, String className, String methodName) {
        SettingsUpdater.Builder builder = new SettingsUpdater.Builder();
        builder.gtaSettingsFile(file).packageName(packageName);
        if (className != null) {
            builder.className(className);
        }
        if (methodName != null) {
            builder.methodName(methodName);
        }
        return builder.build();
    }

    private static void assertRunBlock(Properties actualProperties, String runUnitTests, String runFunctionTests, String runLoadTests) {
        Assert.assertEquals(runUnitTests, actualProperties.getProperty(GTAConstants.RUN_UNIT_TESTS));
        Assert.assertEquals(runFunctionTests, actualProperties.getProperty(GTAConstants.RUN_FUNCTION_TESTS));
        Assert.assertEquals(runLoadTests, actualProperties.getProperty(GTAConstants.RUN_LOAD_TESTS));
    }

    private static void assertPackageBlock(Properties actualProperties, String single, String first, String last) {
        Assert.assertEquals(single, actualProperties.getProperty(GTAConstants.SINGLE_PACKAGE));
        Assert.assertEquals(first, actualProperties.getProperty(GTAConstants.FIRST_PACKAGE));
        Assert.assertEquals(last, actualProperties.getProperty(GTAConstants.LAST_PACKAGE));
    }

    private static void assertClassBlock(Properties actualProperties, String single, String first, String last) {
        Assert.assertEquals(single, actualProperties.getProperty(GTAConstants.SINGLE_CLASS));
        Assert.assertEquals(first, actualProperties.getProperty(GTAConstants.FIRST_CLASS));
        Assert.assertEquals(last, actualProperties.getProperty(GTAConstants.LAST_CLASS));
    }

    private static void assertMethodBlock(Properties actualProperties, String single, String first, String last) {
        Assert.assertEquals(single, actualProperties.getProperty(GTAConstants.SINGLE_METHOD));
        Assert.assertEquals(first, actualProperties.getProperty(GTAConstants.FIRST_METHOD));
        Assert.assertEquals(last, actualProperties.getProperty(GTAConstants.LAST_METHOD));
    }

    @Before
    public void init() {
        file = new File(getClass().getResource("GTASettings.txt").getPath());
    }
}