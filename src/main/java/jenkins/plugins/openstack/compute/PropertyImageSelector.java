package jenkins.plugins.openstack.compute;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import hudson.Extension;
import hudson.model.Descriptor;

import org.kohsuke.stapler.DataBoundConstructor;

import org.openstack4j.model.image.Image;

import jenkins.plugins.openstack.compute.internal.Openstack;

/**
 * @author <a href="mailto:jgriffiths@opsview.com">Joshua Griffiths</a>
 */
public class PropertyImageSelector extends OpenStackImageSelector {

    private List<ImageProperty> imageProperties;

    @DataBoundConstructor
    public PropertyImageSelector(List<ImageProperty> imageProperties) {
        if (imageProperties != null) {
            this.imageProperties = imageProperties;
        } else {
            this.imageProperties = Collections.<ImageProperty>emptyList();
        }
    }

    @Override
    public String getOpenStackImageId(JCloudsCloud cloud) {
        final Openstack os = cloud.getOpenstack();
        List<? extends Image> availableImages = os.getSortedImages();
        // TODO(jgriffiths) Check empty list
images:
        for (Image image : availableImages) {
            Map<String, String> properties = image.getProperties();
            for (ImageProperty propertyFilter : imageProperties) {
                String filterKey = propertyFilter.getPropertyName();
                String filterVal = propertyFilter.getPropertyValue();

                String imageAttrValue = properties.get(filterKey);

                if (imageAttrValue == null)
                    continue images;

                if (!imageAttrValue.equals(filterVal))
                    continue images;

                // Image matches selected filters
                return image.getId();
            }
        }
        return null;
    }

    public List<ImageProperty> getImageProperties() {
        return imageProperties;
    }

    @Extension
    public static class DescriptorImpl 
        extends Descriptor<OpenStackImageSelector> {

        @Override
        public String getDisplayName() {
            return "Select OpenStack Image by Image Properties";
        }
    }

}
