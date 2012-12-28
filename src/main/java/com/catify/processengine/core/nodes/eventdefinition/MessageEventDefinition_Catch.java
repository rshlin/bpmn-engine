package com.catify.processengine.core.nodes.eventdefinition;

import com.catify.processengine.core.integration.MessageIntegrationSPI;
import com.catify.processengine.core.messages.ActivationMessage;
import com.catify.processengine.core.messages.DeactivationMessage;
import com.catify.processengine.core.messages.TriggerMessage;
import com.catify.processengine.core.nodes.NodeFactory;
import com.catify.processengine.core.processdefinition.jaxb.TMessageIntegration;
import com.catify.processengine.core.services.MessageDispatcherService;

// TODO: Auto-generated Javadoc
/**
 * Each (akka) node that has a catching message event definition instantiates
 * and binds an object of the MessageEventDefinition_Catch class. This class
 * implements the messaging part of the catching message event node. Each time
 * the node gets a trigger message its trigger method saves the payload to the
 * object defined in the process.xml. For instantiation of this node see {@link NodeFactory}.
 */
public class MessageEventDefinition_Catch implements EventDefinition {

	private final String uniqueProcessId;
	private final String uniqueFlowNodeId;
	private final String actorRefString;

	private MessageIntegrationSPI integrationSPI;

	/**
	 * Instantiates a new throwing message event definition.
	 * 
	 * @param uniqueProcessId
	 *            the unique process id
	 * @param uniqueFlowNodeId
	 *            the unique flow node id
	 * @param actorRefString
	 *            the actor reference string
	 * @param messageIntegration
	 *            the message event definition
	 */
	public MessageEventDefinition_Catch(String uniqueProcessId, String uniqueFlowNodeId,
			String actorRefString, TMessageIntegration messageIntegration) {

		this.uniqueProcessId = uniqueProcessId;
		this.uniqueFlowNodeId = uniqueFlowNodeId;
		this.actorRefString = actorRefString;

		if (messageIntegration != null) {
			this.integrationSPI = MessageIntegrationSPI
					.getMessageIntegrationImpl(messageIntegration.getPrefix());
			registerMessageEventDefinition_catch(messageIntegration);
		}
	}

	@Override
	public void acitivate(ActivationMessage message) {
		// activation has already been done at process level (in the
		// constructor)
	}

	@Override
	public void deactivate(DeactivationMessage message) {
		// deactivation is done on process level
	}

	@Override
	public void trigger(TriggerMessage message) {
		// messages are dispatched by MessageDispatcherService (so there is nothing to do)
	}

	/**
	 * Register throwing message event definition.
	 * 
	 * @param messageIntegration
	 *            the jaxb message integration
	 */
	public final void registerMessageEventDefinition_catch(
			TMessageIntegration messageIntegration) {
		// start the message integration implementation for this flow node (like
		// routes etc.)
		integrationSPI.startCatchingIntegrationImplementation(
				this.uniqueFlowNodeId,
				messageIntegration.getIntegrationstring(),
				messageIntegration.getMetaData());

		// add it to the message dispatchers mapping table
		MessageDispatcherService.uniqueFlowNodeIdToActorRefMap.put(
				this.uniqueFlowNodeId, this.actorRefString);
	}

	public String getUniqueProcessId() {
		return uniqueProcessId;
	}
}