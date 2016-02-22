package jenkins.plugins.openstack.compute;

import hudson.ExtensionPoint;
import hudson.model.AbstractDescribableImpl;

/**
 * @author <a href="mailto:jgriffiths@opsview.com">Joshua Griffiths</a>
 */
public abstract class OpenStackImageSelector
    extends AbstractDescribableImpl<OpenStackImageSelector>
    implements ExtensionPoint {

    public abstract String getOpenStackImageId(JCloudsCloud cloud);
}
