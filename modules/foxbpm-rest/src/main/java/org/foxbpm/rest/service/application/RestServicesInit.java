package org.foxbpm.rest.service.application;

import org.foxbpm.rest.service.api.model.DeploymentCollectionResource;
import org.foxbpm.rest.service.api.model.DeploymentResource;
import org.foxbpm.rest.service.api.model.ProcessDefinitionCollectionResouce;
import org.foxbpm.rest.service.api.model.ProcessDefinitionResouce;
import org.restlet.routing.Router;

public class RestServicesInit {

	public static void attachResources(Router router){
		router.attach("/model/deployments", DeploymentCollectionResource.class);
	    router.attach("/model/deployments/{deploymentId}", DeploymentResource.class);
	    router.attach("/process-definition/{processDefinitionId}", ProcessDefinitionResouce.class);
	    router.attach("/process-definitions", ProcessDefinitionCollectionResouce.class);
	}
}
