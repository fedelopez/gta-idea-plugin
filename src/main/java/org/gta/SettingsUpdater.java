package org.gta;

import com.intellij.openapi.util.text.StringUtil;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.Properties;
import java.util.logging.Logger;

/** @author fede lopez */
public class SettingsUpdater {

    private static final Logger LOG = Logger.getLogger(SettingsUpdater.class.getName());

    private final File gtaSettingsFile;
    private final String packageName;
    private final String className;
    private final String methodName;

    private final GTARunTestType runTestType;

    private Properties properties;

    private SettingsUpdater(Builder builder) {
        this.runTestType = GTARunTestType.valueFromPackageName(builder.packageName);
        this.gtaSettingsFile = builder.gtaSettingsFile;
        this.packageName = builder.packageName;
        this.className = builder.className == null ? "" : builder.className;
        this.methodName = builder.methodName == null ? "" : builder.methodName;
    }

    public void update() {
        loadProperties();
        doUpdate();
        saveProperties();
    }

    private void doUpdate() {
        switch (runTestType) {
            case RUN_FUNCTION_TESTS:
                updateClassBlock(className);
                updateRunBlock("f", "t", "f");
                updatePackageBlock(packageName);
                updateMethodBlock("");
                break;
            case RUN_LOAD_TESTS:
                updateClassBlock(className);
                updateRunBlock("f", "f", "t");
                updatePackageBlock(packageName);
                updateMethodBlock("");
                break;
            case RUN_UNIT_TESTS:
                updateClassBlock(resolveClassNameForUnitTests());
                updateRunBlock("t", "f", "f");
                updatePackageBlock(resolvePackageForUnitTests());
                updateMethodBlock(resolveMethodForUnitTests());
                break;
        }
    }

    private String resolveMethodForUnitTests() {
        if (StringUtil.isNotEmpty(methodName) && !methodName.endsWith("Test")) {
            return methodName + "Test";
        }
        return methodName;
    }

    private String resolveClassNameForUnitTests() {
        if (StringUtil.isNotEmpty(className) && !className.endsWith("Test")) {
            return className + "Test";
        }
        return className;
    }

    private String resolvePackageForUnitTests() {
        if (packageName.endsWith(".test")) {
            return packageName.substring(0, packageName.indexOf(".test"));
        }
        return packageName;
    }

    private void updateRunBlock(String unitTests, String functionTests, String loadTests) {
        properties.setProperty(GTAConstants.RUN_UNIT_TESTS, unitTests);
        properties.setProperty(GTAConstants.RUN_FUNCTION_TESTS, functionTests);
        properties.setProperty(GTAConstants.RUN_LOAD_TESTS, loadTests);
    }

    private void updatePackageBlock(String packageName) {
        properties.setProperty(GTAConstants.SINGLE_PACKAGE, packageName);
        properties.setProperty(GTAConstants.FIRST_PACKAGE, "");
        properties.setProperty(GTAConstants.LAST_PACKAGE, "");
    }

    private void updateClassBlock(String className) {
        properties.setProperty(GTAConstants.SINGLE_CLASS, className);
        properties.setProperty(GTAConstants.FIRST_CLASS, "");
        properties.setProperty(GTAConstants.LAST_CLASS, "");
    }

    private void updateMethodBlock(String methodName) {
        properties.setProperty(GTAConstants.SINGLE_METHOD, methodName);
        properties.setProperty(GTAConstants.FIRST_METHOD, "");
        properties.setProperty(GTAConstants.LAST_METHOD, "");
    }

    private void loadProperties() {
        properties = new Properties();
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(gtaSettingsFile.getAbsolutePath());
            properties.load(inputStream);
        } catch (IOException e) {
            LOG.warning("Could not create properties file from GTA Settings file: " + e.getLocalizedMessage());
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    private void saveProperties() {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(gtaSettingsFile);
            properties.store(fileOutputStream, gtaSettingsFile.getAbsolutePath());
        } catch (FileNotFoundException e) {
            LOG.warning("GTA Settings file not found!");
        } catch (IOException e) {
            LOG.warning("Exception while reading GTA Settings file: " + e.getLocalizedMessage());
        } finally {
            IOUtils.closeQuietly(fileOutputStream);
        }
    }

    public static class Builder {
        private File gtaSettingsFile;
        private String packageName;
        private String className;
        private String methodName;

        public SettingsUpdater build() throws IllegalStateException {
            checkInvariants();
            return new SettingsUpdater(this);
        }

        private void checkInvariants() {
            if (GTARunTestType.valueFromPackageName(packageName) == null) {
                throw new IllegalStateException(createWrongElementSuppliedMessage(packageName, "package"));
            }
        }

        private String createWrongElementSuppliedMessage(String elementName, String elementType) {
            return "GTA settings file will not be modified, wrong " + elementType + " supplied: '" + elementName + "'";
        }

        public Builder gtaSettingsFile(@NotNull File gtaSettingsFile) {
            this.gtaSettingsFile = gtaSettingsFile;
            return this;
        }

        public Builder packageName(@NotNull String packageName) {
            this.packageName = packageName;
            return this;
        }

        public Builder className(@NotNull String className) {
            this.className = FilenameUtils.getBaseName(className);
            return this;
        }

        public Builder methodName(@Nullable String methodName) {
            this.methodName = methodName;
            return this;
        }
    }

}
