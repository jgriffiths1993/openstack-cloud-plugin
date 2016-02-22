package jenkins.plugins.openstack.compute;

import hudson.Extension;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import hudson.Util;

import org.kohsuke.stapler.DataBoundConstructor;

/**
 * @author <a href="mailto:jgriffiths@opsview.com">Joshua Griffiths</a>
 */
public class ImageProperty extends AbstractDescribableImpl<ImageProperty> {

    private final String propertyName;
    private final String propertyValue;

    @DataBoundConstructor
    public ImageProperty(String propertyName, String propertyValue) {
        this.propertyName = Util.fixEmptyAndTrim(propertyName);
        this.propertyValue = Util.fixEmptyAndTrim(propertyValue);
    }

    public String getPropertyName() {
        return propertyName;
    }

    public String getPropertyValue() {
        return propertyValue;
    }

    @Extension
    public static class DescriptorImpl extends Descriptor<ImageProperty> {

        @Override
        public String getDisplayName() {
            return "Image Property";
        }
    }
}
