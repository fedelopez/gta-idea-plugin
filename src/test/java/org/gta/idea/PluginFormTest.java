package org.gta.idea;

import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.edt.GuiQuery;
import org.assertj.swing.edt.GuiTask;
import org.assertj.swing.fixture.FrameFixture;
import org.assertj.swing.fixture.JTextComponentFixture;
import org.assertj.swing.junit.testcase.AssertJSwingJUnitTestCase;
import org.junit.Test;

import javax.swing.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Martin Varga and FedericoL
 */
public class PluginFormTest extends AssertJSwingJUnitTestCase {

    private PluginForm pluginForm;

    private JTextComponentFixture gtaSettingsPath;
    private JTextComponentFixture prodClassesPath;
    private JTextComponentFixture testClassesPath;

    @Test
    public void shouldApplyIsModifiedWhenGTASettingsFileChanges() throws Exception {
        assertThat(pluginForm.isModified()).isFalse();
        gtaSettingsPath.setText("/usr/home");
        assertThat(pluginForm.isModified()).isTrue();
    }

    @Test
    public void shouldApplyIsModifiedWhenTestClassesChanges() throws Exception {
        assertThat(pluginForm.isModified()).isFalse();
        testClassesPath.setText("/usr/home");
        assertThat(pluginForm.isModified()).isTrue();
    }

    @Test
    public void shouldApplyIsModifiedWhenProdClassesPathChanges() throws Exception {
        assertThat(pluginForm.isModified()).isFalse();
        prodClassesPath.setText("/usr/home");
        assertThat(pluginForm.isModified()).isTrue();
    }

    @Test
    public void shouldSetTheProdClassesPathData() {
        final SettingsApplicationComponent settingsUpdaterAction = mock(SettingsApplicationComponent.class);
        when(settingsUpdaterAction.getProdClassesDirectory()).thenReturn("/usr/test");
        GuiActionRunner.execute(new GuiTask() {
            @Override protected void executeInEDT() throws Throwable {
                pluginForm.setData(settingsUpdaterAction);
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
                pluginForm.setData(settingsUpdaterAction);
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
                pluginForm.setData(settingsUpdaterAction);
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
                pluginForm.getData(settingsUpdaterAction);
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
                pluginForm.getData(settingsUpdaterAction);
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
                pluginForm.getData(settingsUpdaterAction);
            }
        });

        verify(settingsUpdaterAction).setSettingsFilePath("usr/bin");
    }

    @Override
    protected void onSetUp() {
        pluginForm = GuiActionRunner.execute(new GuiQuery<PluginForm>() {
            @Override protected PluginForm executeInEDT() throws Throwable {
                return new PluginForm();
            }
        });
        FrameFixture frame = new FrameFixture(robot(), new TestFrame().createFrame(pluginForm));
        gtaSettingsPath = frame.textBox("txtFilePath");
        prodClassesPath = frame.textBox("prodClassesPath");
        testClassesPath = frame.textBox("classesPath");
    }

    private class TestFrame {
        public JFrame createFrame(final PluginForm pluginForm) {
            return GuiActionRunner.execute(new GuiQuery<JFrame>() {
                @Override protected JFrame executeInEDT() throws Throwable {
                    JFrame frame = new JFrame();
                    frame.setSize(400, 500);
                    frame.setContentPane(pluginForm.getContentPane());
                    frame.setVisible(true);
                    return frame;
                }
            });
        }
    }
}
