package org.gta.idea;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.actionSystem.ex.DataConstantsEx;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiPackage;
import org.apache.commons.io.FilenameUtils;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Files;
import org.gta.SettingsUpdater;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.File;
import java.io.IOException;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author fede lopez
 */
public class SettingsUpdaterActionTest {

    private static final String CLASSES_ROOT_DIR = "C:\\temp";
    private static final String MODULE_DIR = Files.temporaryFolderPath();
    private final String gtaSettingsFilePath = getClass().getResource("GTASettings.txt").getPath();

    private SettingsUpdaterAction action;

    private AnActionEvent actionEvent;
    private DataContext dataContext;

    private PsiJavaFile javaFile;
    private PsiMethod selectedMethod;

    private PsiPackage selectedPackage;
    private SettingsUpdater settingsUpdater;
    private SettingsUpdater.Builder settingsUpdaterBuilder;
    private SettingsApplicationComponent applicationComponent;

    @Test
    public void actionPerformed() throws IOException {
        when(applicationComponent.getSettingsFilePath()).thenReturn(gtaSettingsFilePath);
        when(javaFile.getPackageName()).thenReturn("au.com.pks.rippledown.test");
        when(javaFile.getName()).thenReturn("RippledownTest.java");

        when(settingsUpdaterBuilder.className(anyString())).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.packageName(anyString())).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.methodName("")).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.gtaSettingsFile(any(File.class))).thenReturn(settingsUpdaterBuilder);

        when(settingsUpdaterBuilder.build()).thenReturn(settingsUpdater);

        action.actionPerformed(actionEvent);

        verify(javaFile).getPackageName();
        verify(javaFile).getName();

        verify(settingsUpdaterBuilder).className("RippledownTest.java");
        verify(settingsUpdaterBuilder).packageName("au.com.pks.rippledown.test");
        verify(settingsUpdater).update();
    }

    @Test
    public void actionPerformedWithMethod() throws IOException {
        when(applicationComponent.getSettingsFilePath()).thenReturn(gtaSettingsFilePath);
        when(dataContext.getData(DataConstantsEx.PSI_ELEMENT)).thenReturn(selectedMethod);
        when(javaFile.getPackageName()).thenReturn("au.com.pks.rippledown.test");
        when(javaFile.getName()).thenReturn("RippledownTest.java");
        when(selectedMethod.getName()).thenReturn("doItTest");

        when(settingsUpdaterBuilder.className(anyString())).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.packageName(anyString())).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.methodName(anyString())).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.gtaSettingsFile(any(File.class))).thenReturn(settingsUpdaterBuilder);

        when(settingsUpdaterBuilder.build()).thenReturn(settingsUpdater);

        action.actionPerformed(actionEvent);

        verify(settingsUpdaterBuilder).methodName("doItTest");
        verify(settingsUpdater).update();
    }

    @Test
    public void actionPerformedWithPackage() throws IOException {
        when(applicationComponent.getSettingsFilePath()).thenReturn(gtaSettingsFilePath);
        when(dataContext.getData(DataConstantsEx.PSI_ELEMENT)).thenReturn(selectedPackage);
        when(javaFile.getPackageName()).thenReturn("au.com.pks.rippledown.test");
        when(javaFile.getName()).thenReturn("IgnoreMe.java");

        when(settingsUpdaterBuilder.packageName(anyString())).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.className("")).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.methodName("")).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.gtaSettingsFile(any(File.class))).thenReturn(settingsUpdaterBuilder);

        when(settingsUpdaterBuilder.build()).thenReturn(settingsUpdater);

        action.actionPerformed(actionEvent);

        verify(settingsUpdaterBuilder).packageName("au.com.pks.rippledown.test");
        verify(settingsUpdaterBuilder).className("");
        verify(settingsUpdaterBuilder).methodName("");

        verify(settingsUpdater).update();
    }

    @Test
    public void actionPerformedNullGTAFile() throws IOException {
        when(applicationComponent.getSettingsFilePath()).thenReturn(null);

        when(dataContext.getData(DataConstantsEx.PSI_ELEMENT)).thenReturn(selectedMethod);
        when(javaFile.getPackageName()).thenReturn("au.com.pks.rippledown");
        when(javaFile.getName()).thenReturn("Rippledown.java");
        when(selectedMethod.getName()).thenReturn("doIt");

        when(settingsUpdaterBuilder.className(anyString())).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.packageName(anyString())).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.methodName(anyString())).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.gtaSettingsFile(new File(SettingsUpdaterAction.DEFAULT_GTA_SETTINGS_FILE))).thenReturn(settingsUpdaterBuilder);

        when(settingsUpdaterBuilder.build()).thenReturn(settingsUpdater);

        action.actionPerformed(actionEvent);

        verify(settingsUpdater).update();
    }

    @Test
    public void actionPerformedNonExistingGTAFile() throws IOException {
        when(applicationComponent.getSettingsFilePath()).thenReturn("I-Dont-Exist");

        when(dataContext.getData(DataConstantsEx.PSI_ELEMENT)).thenReturn(selectedMethod);
        when(javaFile.getPackageName()).thenReturn("au.com.pks.rippledown");
        when(javaFile.getName()).thenReturn("Rippledown.java");
        when(selectedMethod.getName()).thenReturn("doIt");

        when(settingsUpdaterBuilder.className(anyString())).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.packageName(anyString())).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.methodName(anyString())).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.gtaSettingsFile(any(File.class))).thenReturn(settingsUpdaterBuilder);

        when(settingsUpdaterBuilder.build()).thenReturn(settingsUpdater);

        action.actionPerformed(actionEvent);

        verify(settingsUpdater).update();
    }

    @Test
    public void actionPerformedModulePathEnvironmentVariableGTAFile() throws IOException {
        when(javaFile.getPackageName()).thenReturn("au.com.pks.rippledown.test");
        when(javaFile.getName()).thenReturn("RippledownTest.java");

        when(settingsUpdaterBuilder.packageName(anyString())).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.className(anyString())).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.methodName(anyString())).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.gtaSettingsFile(any(File.class))).thenReturn(settingsUpdaterBuilder);

        when(applicationComponent.getSettingsFilePath()).thenReturn(SettingsUpdaterAction.MODULE_DIR + "/GTASettings.txt");

        action.actionPerformed(actionEvent);

        verify(settingsUpdaterBuilder).gtaSettingsFile(new File(MODULE_DIR, "GTASettings.txt"));
    }

    @Test
    public void actionPerformedNonTestClass() throws IOException {
        when(applicationComponent.getSettingsFilePath()).thenReturn(gtaSettingsFilePath);
        when(javaFile.getPackageName()).thenReturn("au.com.pks.rippledown");
        when(javaFile.getName()).thenReturn("Rippledown.java");

        when(settingsUpdaterBuilder.packageName("au.com.pks.rippledown")).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.className("Rippledown.java")).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.methodName("")).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.gtaSettingsFile(any(File.class))).thenReturn(settingsUpdaterBuilder);

        when(settingsUpdaterBuilder.build()).thenReturn(settingsUpdater);

        action.actionPerformed(actionEvent);

        verify(javaFile).getPackageName();
        verify(javaFile).getName();

        verify(settingsUpdaterBuilder).packageName("au.com.pks.rippledown");
        verify(settingsUpdaterBuilder).className("Rippledown.java");
        verify(settingsUpdater).update();
    }

    @Test
    public void actionPerformedNonTestMethod() throws IOException {
        when(applicationComponent.getSettingsFilePath()).thenReturn(gtaSettingsFilePath);
        when(dataContext.getData(DataConstantsEx.PSI_ELEMENT)).thenReturn(selectedMethod);
        when(javaFile.getPackageName()).thenReturn("au.com.pks.rippledown");
        when(javaFile.getName()).thenReturn("Rippledown.java");
        when(selectedMethod.getName()).thenReturn("doIt");

        when(settingsUpdaterBuilder.className(anyString())).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.packageName(anyString())).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.methodName(anyString())).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.gtaSettingsFile(any(File.class))).thenReturn(settingsUpdaterBuilder);

        when(settingsUpdaterBuilder.build()).thenReturn(settingsUpdater);

        action.actionPerformed(actionEvent);

        verify(settingsUpdaterBuilder).methodName("doIt");
        verify(settingsUpdater).update();
    }

    @Test
    public void actionPerformedFunctionTest() throws IOException {
        when(applicationComponent.getSettingsFilePath()).thenReturn(gtaSettingsFilePath);
        when(dataContext.getData(DataConstantsEx.PSI_ELEMENT)).thenReturn(selectedMethod);
        when(javaFile.getPackageName()).thenReturn("rippledown.translation.functiontest");
        when(javaFile.getName()).thenReturn("AssignTranslationPermissions.java");
        when(selectedMethod.getName()).thenReturn("runTestUnsafely");

        when(settingsUpdaterBuilder.className(anyString())).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.packageName(anyString())).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.methodName(anyString())).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.gtaSettingsFile(any(File.class))).thenReturn(settingsUpdaterBuilder);

        when(settingsUpdaterBuilder.build()).thenReturn(settingsUpdater);

        action.actionPerformed(actionEvent);

        verify(settingsUpdaterBuilder).className("AssignTranslationPermissions.java");
        verify(settingsUpdater).update();
    }

    @Test
    public void actionPerformedLoadTest() throws IOException {
        when(applicationComponent.getSettingsFilePath()).thenReturn(gtaSettingsFilePath);
        when(dataContext.getData(DataConstantsEx.PSI_ELEMENT)).thenReturn(selectedMethod);
        when(javaFile.getPackageName()).thenReturn("rippledown.translation.loadtest");
        when(javaFile.getName()).thenReturn("AssignTranslationPermissions.java");
        when(selectedMethod.getName()).thenReturn("runTestUnsafely");

        when(settingsUpdaterBuilder.className(anyString())).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.packageName(anyString())).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.methodName(anyString())).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.gtaSettingsFile(any(File.class))).thenReturn(settingsUpdaterBuilder);

        when(settingsUpdaterBuilder.build()).thenReturn(settingsUpdater);

        action.actionPerformed(actionEvent);

        verify(settingsUpdaterBuilder).className("AssignTranslationPermissions.java");
        verify(settingsUpdater).update();
    }

    @Test
    public void shouldHandleBlankClassesOutputDirectory() throws IOException {
        when(applicationComponent.getSettingsFilePath()).thenReturn(gtaSettingsFilePath);

        when(javaFile.getPackageName()).thenReturn("au.com.pks.rippledown");
        when(javaFile.getName()).thenReturn("Rippledown.java");

        when(settingsUpdaterBuilder.packageName(anyString())).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.className(anyString())).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.methodName(anyString())).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.gtaSettingsFile(any(File.class))).thenReturn(settingsUpdaterBuilder);

        when(settingsUpdaterBuilder.build()).thenReturn(settingsUpdater);

        action.actionPerformed(actionEvent);

        verify(settingsUpdaterBuilder).classesDirectory(null);
    }

    @Test
    public void shouldSetProdClassesOutputDirectory() throws IOException {
        when(applicationComponent.getSettingsFilePath()).thenReturn(gtaSettingsFilePath);
        String expected = FilenameUtils.normalizeNoEndSeparator("/home/project-a/classes");
        when(applicationComponent.getProdClassesDirectory()).thenReturn(expected);

        when(javaFile.getPackageName()).thenReturn("au.com.pks.rippledown");
        when(javaFile.getName()).thenReturn("Rippledown.java");

        when(settingsUpdaterBuilder.packageName(anyString())).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.className(anyString())).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.methodName(anyString())).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.gtaSettingsFile(any(File.class))).thenReturn(settingsUpdaterBuilder);

        when(settingsUpdaterBuilder.build()).thenReturn(settingsUpdater);

        action.actionPerformed(actionEvent);

        verify(settingsUpdaterBuilder).prodClassesDirectory(expected);
    }

    @Test
    public void shouldHandleBlankProdClassesOutputDirectory() throws IOException {
        when(applicationComponent.getSettingsFilePath()).thenReturn(gtaSettingsFilePath);

        when(javaFile.getPackageName()).thenReturn("au.com.pks.rippledown");
        when(javaFile.getName()).thenReturn("Rippledown.java");

        when(settingsUpdaterBuilder.packageName(anyString())).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.className(anyString())).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.methodName(anyString())).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.gtaSettingsFile(any(File.class))).thenReturn(settingsUpdaterBuilder);

        when(settingsUpdaterBuilder.build()).thenReturn(settingsUpdater);

        action.actionPerformed(actionEvent);

        verify(settingsUpdaterBuilder).prodClassesDirectory(null);
    }

    @Test
    public void shouldSetCustomTestClassesOutputDirectory() throws IOException {
        String expected = FilenameUtils.normalizeNoEndSeparator("C:/home/joe/project-a/classes");
        when(applicationComponent.getClassesDirectory()).thenReturn(expected);
        when(applicationComponent.getSettingsFilePath()).thenReturn(gtaSettingsFilePath);

        when(javaFile.getPackageName()).thenReturn("au.com.pks.rippledown");
        when(javaFile.getName()).thenReturn("Rippledown.java");

        when(settingsUpdaterBuilder.packageName(anyString())).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.className(anyString())).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.methodName(anyString())).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.gtaSettingsFile(any(File.class))).thenReturn(settingsUpdaterBuilder);

        when(settingsUpdaterBuilder.build()).thenReturn(settingsUpdater);

        action.actionPerformed(actionEvent);

        verify(settingsUpdaterBuilder).classesDirectory(FilenameUtils.normalizeNoEndSeparator(expected));
    }

    @Test
    public void shouldReplaceModuleDirConstantOnClassesOutputDirectory() throws IOException {
        when(applicationComponent.getSettingsFilePath()).thenReturn(gtaSettingsFilePath);
        when(applicationComponent.getClassesDirectory()).thenReturn("$MODULE_DIR$/classes");

        when(javaFile.getPackageName()).thenReturn("au.com.pks.rippledown");
        when(javaFile.getName()).thenReturn("Rippledown.java");

        when(settingsUpdaterBuilder.packageName(anyString())).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.className(anyString())).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.methodName(anyString())).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.gtaSettingsFile(any(File.class))).thenReturn(settingsUpdaterBuilder);

        when(settingsUpdaterBuilder.build()).thenReturn(settingsUpdater);

        action.actionPerformed(actionEvent);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(settingsUpdaterBuilder).classesDirectory(captor.capture());
        Assertions.assertThat(captor.getValue()).isEqualTo(new File(MODULE_DIR, "classes").getAbsolutePath());
    }

    @Test
    public void shouldReplaceModuleDirConstantOnProdClassesOutputDirectory() throws IOException {
        when(applicationComponent.getSettingsFilePath()).thenReturn(gtaSettingsFilePath);
        when(applicationComponent.getProdClassesDirectory()).thenReturn("$MODULE_DIR$/classes");

        when(javaFile.getPackageName()).thenReturn("au.com.pks.rippledown");
        when(javaFile.getName()).thenReturn("Rippledown.java");

        when(settingsUpdaterBuilder.packageName(anyString())).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.className(anyString())).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.methodName(anyString())).thenReturn(settingsUpdaterBuilder);
        when(settingsUpdaterBuilder.gtaSettingsFile(any(File.class))).thenReturn(settingsUpdaterBuilder);

        when(settingsUpdaterBuilder.build()).thenReturn(settingsUpdater);

        action.actionPerformed(actionEvent);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(settingsUpdaterBuilder).prodClassesDirectory(captor.capture());
        Assertions.assertThat(captor.getValue()).isEqualTo(new File(MODULE_DIR, "classes").getAbsolutePath());
    }

    @Before
    public void init() {
        action = new SettingsUpdaterAction();
        javaFile = mock(PsiJavaFile.class);
        dataContext = mock(DataContext.class);
        when(dataContext.getData(DataConstantsEx.PSI_FILE)).thenReturn(javaFile);
        selectedMethod = mock(PsiMethod.class);
        selectedPackage = mock(PsiPackage.class);
        settingsUpdater = mock(SettingsUpdater.class);
        settingsUpdaterBuilder = mock(SettingsUpdater.Builder.class);
        actionEvent = new AnActionEvent(null, dataContext, "", new Presentation(), null, 0);
        applicationComponent = mock(SettingsApplicationComponent.class);

        action.setGTAApplicationComponent(applicationComponent);
        action.setSettingsUpdaterBuilder(settingsUpdaterBuilder);

        ModuleMock module = new ModuleMock(MODULE_DIR, CLASSES_ROOT_DIR);
        when(dataContext.getData(DataConstantsEx.MODULE)).thenReturn(module);
    }

}
