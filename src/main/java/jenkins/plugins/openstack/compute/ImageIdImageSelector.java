package jenkins.plugins.openstack.compute;

import java.util.logging.Level;
import java.util.logging.Logger;

import hudson.Extension;
import hudson.model.Descriptor;
import hudson.RelativePath;
import hudson.Util;
import hudson.util.ListBoxModel;

import jenkins.plugins.openstack.compute.internal.Openstack;

import org.kohsuke.accmod.Restricted;
import org.kohsuke.accmod.restrictions.DoNotUse;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

import org.openstack4j.api.exceptions.AuthenticationException;
import org.openstack4j.model.image.Image;

/**
 * @author <a href="mailto:jgriffiths@opsview.com">Joshua Griffiths</a>
 */
public class ImageIdImageSelector extends OpenStackImageSelector {

    private static final Logger LOGGER = 
        Logger.getLogger(ImageIdImageSelector.class.getName());

    private String imageId;

    @DataBoundConstructor
    public ImageIdImageSelector(String imageId) {
        this.imageId = imageId;
    }

    @Override
    public String getOpenStackImageId(JCloudsCloud cloud) {
        return imageId;
    }

    public String getImageId() {
        return imageId;
    }

    protected Object readResolve() {
        int i;
        if ((i = imageId.indexOf('/')) != -1) {
            imageId = imageId.substring(i + 1);
        }
        return this;
    }

    @Extension
    public static class DescriptorImpl 
        extends Descriptor<OpenStackImageSelector> {

        @Override
        public String getDisplayName() {
            return "Select OpenStack Image by ID";
        }

        @Restricted(DoNotUse.class)
        public ListBoxModel doFillImageIdItems(
                @QueryParameter String imageId,
                @RelativePath("../..") @QueryParameter String endPointUrl,
                @RelativePath("../..") @QueryParameter String identity,
                @RelativePath("../..") @QueryParameter String credential,
                @RelativePath("../..") @QueryParameter String zone) {

            ListBoxModel listBox = new ListBoxModel();
            listBox.add("None specified", "");

            try {
                final Openstack openstack = 
                    JCloudsCloud.getOpenstack(endPointUrl, 
                                              identity, 
                                              credential, 
                                              zone);

                for (Image image : openstack.getSortedImages()) {
                    listBox.add(String.format("%s (%s)", image.getName(), image.getId()), image.getId());
                }
                return listBox;

            } catch (AuthenticationException _) {
                // Incorrect credentials - noop
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            }

            if (Util.fixEmpty(imageId) != null) {
                listBox.add(imageId);
            }

            return listBox;
        }
    }

}
