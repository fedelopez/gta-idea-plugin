package org.gta;

import org.junit.Assert;
import org.junit.Test;

import static org.gta.GTARunTestType.*;

/** @author fede lopez */
public class GTARunTestTypeTest {

    @Test
    public void valueFromPackageNameTest() {
        Assert.assertEquals(RUN_UNIT_TESTS, valueFromPackageName("rippledown.test"));
        Assert.assertEquals(RUN_FUNCTION_TESTS, valueFromPackageName("rippledown.functiontest"));
        Assert.assertEquals(RUN_LOAD_TESTS, valueFromPackageName("rippledown.loadtest"));
    }

    @Test
    public void valueFromPackageNameWrongArgumentTest() {
        Assert.assertNull("Expected a null value", valueFromPackageName(""));
    }

}
