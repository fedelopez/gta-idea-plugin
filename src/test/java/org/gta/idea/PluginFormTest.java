package org.gta.idea;

import org.assertj.swing.core.GenericTypeMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.edt.GuiTask;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JTextComponentFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.swing.finder.WindowFinder.findFrame;
import static org.assertj.swing.launcher.ApplicationLauncher.application;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Martin Varga and FedericoL
 */
public class PluginFormTest extends AssertJSwingJUnitTestCase {

    private JTextComponentFixture gtaSettingsPath;
    private JTextComponentFixture prodClassesPath;
    private JTextComponentFixture testClassesPath;

    @Test
    public void shouldApplyIsModifiedWhenGTASettingsFileChanges() throws Exception {
        assertThat(getPluginForm().isModified()).isFalse();
        gtaSettingsPath.setText("/usr/home");
        assertThat(getPluginForm().isModified()).isTrue();
    }

    @Test
    public void shouldApplyIsModifiedWhenTestClassesChanges() throws Exception {
        assertThat(getPluginForm().isModified()).isFalse();
        testClassesPath.setText("/usr/home");
        assertThat(getPluginForm().isModified()).isTrue();
    }

    @Test
    public void shouldApplyIsModifiedWhenProdClassesPathChanges() throws Exception {
        assertThat(getPluginForm().isModified()).isFalse();
        prodClassesPath.setText("/usr/home");
        assertThat(getPluginForm().isModified()).isTrue();
    }

    @Test
    public void shouldSetTheProdClassesPathData() {
        final SettingsApplicationComponent settingsUpdaterAction = mock(SettingsApplicationComponent.class);
        when(settingsUpdaterAction.getProdClassesDirectory()).thenReturn("/usr/test");
        GuiActionRunner.execute(new GuiTask() {
            @Override protected void executeInEDT() throws Throwable {
                getPluginForm().setData(settingsUpdaterAction);
            }
        });
        assertThat(prodClassesPath.text()).isEqualTo("/usr/test");
    }

    @Test
    public void shouldSetTheTestClassesPathData() {
        final SettingsApplicationComponent settingsUpdaterAction = mock(SettingsApplicationComponent.class);
        when(settingsUpdaterAction.getClassesDirectory()).thenReturn("/usr/test");
        GuiActionRunner.execute(new GuiTask() {
            @Override protected void executeInEDT() throws Throwable {
                getPluginForm().setData(settingsUpdaterAction);
            }
        });
        assertThat(testClassesPath.text()).isEqualTo("/usr/test");
    }

    @Test
    public void shouldSetTheGTASettingsPathData() {
        final SettingsApplicationComponent settingsUpdaterAction = mock(SettingsApplicationComponent.class);
        when(settingsUpdaterAction.getSettingsFilePath()).thenReturn("/usr/test");
        GuiActionRunner.execute(new GuiTask() {
            @Override protected void executeInEDT() throws Throwable {
                getPluginForm().setData(settingsUpdaterAction);
            }
        });
        assertThat(gtaSettingsPath.text()).isEqualTo("/usr/test");
    }

    @Test
    public void shouldGetTheProdClassPathData() throws Exception {
        prodClassesPath.setText("usr/bin");
        final SettingsApplicationComponent settingsUpdaterAction = mock(SettingsApplicationComponent.class);

        GuiActionRunner.execute(new GuiTask() {
            @Override protected void executeInEDT() throws Throwable {
                getPluginForm().getData(settingsUpdaterAction);
            }
        });

        verify(settingsUpdaterAction).setProdClassesDirectory("usr/bin");
    }

    @Test
    public void shouldGetTheTestClassPathData() throws Exception {
        testClassesPath.setText("usr/bin");
        final SettingsApplicationComponent settingsUpdaterAction = mock(SettingsApplicationComponent.class);

        GuiActionRunner.execute(new GuiTask() {
            @Override protected void executeInEDT() throws Throwable {
                getPluginForm().getData(settingsUpdaterAction);
            }
        });

        verify(settingsUpdaterAction).setClassesDirectory("usr/bin");
    }

    @Test
    public void shouldGetTheGTASettingsPathData() throws Exception {
        gtaSettingsPath.setText("usr/bin");
        final SettingsApplicationComponent settingsUpdaterAction = mock(SettingsApplicationComponent.class);

        GuiActionRunner.execute(new GuiTask() {
            @Override protected void executeInEDT() throws Throwable {
                getPluginForm().getData(settingsUpdaterAction);
            }
        });

        verify(settingsUpdaterAction).setSettingsFilePath("usr/bin");
    }

    @Override protected void onSetUp() {
        mainPane = null;
        application(PluginFormTest.class).start();
        FrameFixture frame = findFrame(new GenericTypeMatcher<Frame>(Frame.class) {
            protected boolean isMatching(Frame frame) {
                return "Test frame".equals(frame.getTitle()) && frame.isShowing();
            }
        }).using(robot());
        gtaSettingsPath = frame.textBox("txtFilePath");
        prodClassesPath = frame.textBox("prodClassesPath");
        testClassesPath = frame.textBox("classesPath");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() {
                JFrame frame = new JFrame("Test frame");
                frame.setSize(400, 500);
                frame.setContentPane(getPluginForm().getContentPane());
                frame.setVisible(true);
            }
        });
    }

    private static PluginForm mainPane;

    private static PluginForm getPluginForm() {
        if (mainPane == null) {
            mainPane = new PluginForm();
        }
        return mainPane;
    }

}
